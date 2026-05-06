package com.ruoyi.transfer.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.transfer.config.TransferProperties;
import com.ruoyi.transfer.domain.TransferFileInfo;
import com.ruoyi.transfer.domain.TransferStoredFile;
import com.ruoyi.transfer.service.ITransferFileService;

/**
 * 基于本地文件系统的在线文件传输服务实现。
 *
 * <p>by AI.Coding</p>
 *
 * @author AI.Coding
 */
@Service
public class LocalTransferFileServiceImpl implements ITransferFileService
{
    /** 存储键与原始文件名的分隔符 */
    private static final String FILE_NAME_SEPARATOR = "__";

    /** 文件名非法字符替换规则 */
    private static final Pattern INVALID_FILE_NAME_PATTERN = Pattern.compile("[\\\\/:*?\"<>|]");

    /** 传输模块配置 */
    private final TransferProperties transferProperties;

    /**
     * 构造本地传输服务。
     *
     * @param transferProperties 传输模块配置
     */
    public LocalTransferFileServiceImpl(TransferProperties transferProperties)
    {
        this.transferProperties = transferProperties;
    }

    /**
     * 初始化存储目录。
     *
     * @throws IOException 目录创建异常
     */
    @PostConstruct
    public void initStorageDirectory() throws IOException
    {
        // 启动时提前创建目录，避免首次上传时才暴露环境问题。
        Files.createDirectories(getStorageRoot());
    }

    /**
     * 保存上传文件。
     *
     * @param file 上传文件
     * @return 文件展示信息
     * @throws IOException 文件保存异常
     */
    @Override
    public TransferFileInfo saveFile(MultipartFile file) throws IOException
    {
        validateUploadFile(file);
        String originalFilename = sanitizeOriginalFilename(file.getOriginalFilename());
        String key = buildStorageKey(originalFilename);
        Path targetPath = resolveTargetPath(key);

        // 使用覆盖写入保证同键重试时行为明确，但正常情况下键不会重复。
        try (InputStream inputStream = file.getInputStream())
        {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
        return buildFileInfo(targetPath);
    }

    /**
     * 查询当前可下载文件列表。
     *
     * @return 文件列表
     * @throws IOException 文件读取异常
     */
    @Override
    public List<TransferFileInfo> listFiles() throws IOException
    {
        Path storageRoot = getStorageRoot();
        if (!Files.exists(storageRoot))
        {
            return java.util.Collections.emptyList();
        }

        // 按最后修改时间倒序返回，保证新上传文件优先展示。
        try (Stream<Path> pathStream = Files.list(storageRoot))
        {
            return pathStream.filter(Files::isRegularFile)
                    .sorted(Comparator.comparing(this::getLastModifiedTimeSafely).reversed())
                    .map(this::buildFileInfoSafely)
                    .collect(Collectors.toList());
        }
        catch (UncheckedIOException e)
        {
            throw e.getCause();
        }
    }

    /**
     * 按文件键加载下载实体。
     *
     * @param key 文件键
     * @return 下载实体
     * @throws IOException 文件读取异常
     */
    @Override
    public TransferStoredFile loadFile(String key) throws IOException
    {
        Path targetPath = resolveExistingPath(key);
        return new TransferStoredFile(key, extractOriginalFilename(key), targetPath, Files.size(targetPath));
    }

    /**
     * 删除指定文件。
     *
     * @param key 文件键
     * @throws IOException 删除异常
     */
    @Override
    public void deleteFile(String key) throws IOException
    {
        Path targetPath = resolveExistingPath(key);
        Files.delete(targetPath);
    }

    /**
     * 获取最终存储根目录。
     *
     * @return 存储根目录
     */
    protected Path getStorageRoot()
    {
        return Paths.get(transferProperties.resolveStorageRoot()).toAbsolutePath().normalize();
    }

    /**
     * 校验上传文件是否满足最基本要求。
     *
     * @param file 上传文件
     */
    protected void validateUploadFile(MultipartFile file)
    {
        // 明确拒绝空文件，避免页面出现无效占位记录。
        if (file == null || file.isEmpty())
        {
            throw new IllegalArgumentException("请选择需要上传的文件");
        }
    }

    /**
     * 规范化原始文件名。
     *
     * @param originalFilename 原始文件名
     * @return 可安全落盘的原始文件名
     */
    protected String sanitizeOriginalFilename(String originalFilename)
    {
        String safeFileName = FilenameUtils.getName(Objects.toString(originalFilename, ""));
        if (StringUtils.isEmpty(safeFileName))
        {
            safeFileName = "未命名文件";
        }

        // 仅替换路径类和系统保留字符，尽量保留用户可识别的名称。
        safeFileName = INVALID_FILE_NAME_PATTERN.matcher(safeFileName).replaceAll("_");
        return safeFileName.replace(FILE_NAME_SEPARATOR, "_");
    }

    /**
     * 生成最终存储键。
     *
     * @param originalFilename 原始文件名
     * @return 存储键
     */
    protected String buildStorageKey(String originalFilename)
    {
        return System.currentTimeMillis() + "_" + IdUtils.fastSimpleUUID() + FILE_NAME_SEPARATOR + originalFilename;
    }

    /**
     * 从存储键中提取原始文件名。
     *
     * @param key 存储键
     * @return 原始文件名
     */
    protected String extractOriginalFilename(String key)
    {
        int separatorIndex = key.indexOf(FILE_NAME_SEPARATOR);
        if (separatorIndex < 0 || separatorIndex + FILE_NAME_SEPARATOR.length() >= key.length())
        {
            return key;
        }
        return key.substring(separatorIndex + FILE_NAME_SEPARATOR.length());
    }

    /**
     * 解析待写入的目标路径。
     *
     * @param key 存储键
     * @return 目标路径
     * @throws IOException 路径解析异常
     */
    protected Path resolveTargetPath(String key) throws IOException
    {
        Path storageRoot = getStorageRoot();
        Files.createDirectories(storageRoot);
        Path targetPath = storageRoot.resolve(key).normalize();

        // 强制路径必须落在传输目录下，阻断目录穿越。
        if (!targetPath.startsWith(storageRoot))
        {
            throw new IllegalArgumentException("文件路径非法");
        }
        return targetPath;
    }

    /**
     * 解析已存在文件路径。
     *
     * @param key 存储键
     * @return 已存在文件路径
     * @throws IOException 文件读取异常
     */
    protected Path resolveExistingPath(String key) throws IOException
    {
        if (StringUtils.isEmpty(key) || key.contains("/") || key.contains("\\"))
        {
            throw new IllegalArgumentException("文件标识非法");
        }

        Path targetPath = resolveTargetPath(key);
        if (!Files.exists(targetPath) || !Files.isRegularFile(targetPath))
        {
            throw new NoSuchFileException(key);
        }
        return targetPath;
    }

    /**
     * 构造页面展示信息。
     *
     * @param path 文件路径
     * @return 展示信息
     * @throws IOException 读取异常
     */
    protected TransferFileInfo buildFileInfo(Path path) throws IOException
    {
        String key = path.getFileName().toString();
        long uploadTime = Files.getLastModifiedTime(path).toMillis();
        return new TransferFileInfo(key, extractOriginalFilename(key), Files.size(path), uploadTime,
                "/transfer/files/download?key=" + key);
    }

    /**
     * 安全构造页面展示信息。
     *
     * @param path 文件路径
     * @return 展示信息
     */
    protected TransferFileInfo buildFileInfoSafely(Path path)
    {
        try
        {
            return buildFileInfo(path);
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 安全获取文件修改时间。
     *
     * @param path 文件路径
     * @return 修改时间
     */
    protected long getLastModifiedTimeSafely(Path path)
    {
        try
        {
            return Files.getLastModifiedTime(path).toMillis();
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }
}
