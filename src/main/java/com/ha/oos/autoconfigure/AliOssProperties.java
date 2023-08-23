package com.ha.oos.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云文件上传属性配置文件
 *
 * @author cjx
 */
@Data
@ConfigurationProperties(prefix = "ha-oos.ali")
public class AliOssProperties {

    /**
     * 阿里云绑定的域名
     */
    private String domain;

    /**
     * 阿里云EndPoint
     */
    private String endPoint;

    /**
     * 阿里云AccessKeyId
     */
    private String accessKeyId;

    /**
     * 阿里云AccessKeySecret
     */
    private String accessKeySecret;

    /**
     * 阿里云BucketName
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
