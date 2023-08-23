package com.ha.oos.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Minio 文件上传属性配置文件
 *
 * @author cjx
 */
@Data
@ConfigurationProperties(prefix = "ha-oos.minio")
public class MinioOssProperties {

    /**
     * Minio EndPoint
     */
    private String endPoint;

    /**
     * Minio accessKey
     */
    private String accessKey;

    /**
     * Minio secretKey
     */
    private String secretKey;

    /**
     * Minio BucketName
     */
    private String bucketName;

    /**
     * 路径前缀
     */
    private String prefix;

    /**
     * 是否启用
     */
    private Boolean enable = false;
}
