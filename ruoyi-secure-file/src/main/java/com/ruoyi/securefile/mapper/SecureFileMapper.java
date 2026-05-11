package com.ruoyi.securefile.mapper;

import java.util.List;
import com.ruoyi.securefile.domain.SecureFile;
import com.ruoyi.securefile.dto.SecureFileQuery;

/**
 * 安全文件数据层
 *
 * @author AI.Coding
 */
public interface SecureFileMapper
{
    /**
     * 新增安全文件记录。
     *
     * @param secureFile 文件记录
     * @return 影响行数
     */
    int insertSecureFile(SecureFile secureFile);

    /**
     * 按文件 ID 查询记录。
     *
     * @param fileId 文件 ID
     * @return 文件记录
     */
    SecureFile selectSecureFileById(Long fileId);

    /**
     * 查询安全文件列表。
     *
     * @param query 查询条件
     * @return 文件列表
     */
    List<SecureFile> selectSecureFileList(SecureFileQuery query);

    /**
     * 更新文件状态。
     *
     * @param secureFile 文件记录
     * @return 影响行数
     */
    int updateSecureFileStatus(SecureFile secureFile);
}
