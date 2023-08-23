package com.ha.oos.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 腾讯云文件上传属性配置文件
 *
 * @author cjx
 */
@Data
@ConfigurationProperties(prefix = "ha-oos.tencent")
public class TencentOssProperties {

    /**
     * 腾讯云绑定的域名
     */
    private String domain;

    /**
     * 腾讯云AppId
     */
    private String appId;

    /**
     * 腾讯云SecretId
     */
    private String secretId;

    /**
     * 腾讯云SecretKey
     */
    private String secretKey;

    /**
     * 腾讯云BucketName
     */
    private String bucketName;

    /**
     * 腾讯云COS所属地区
     */
    private String region;

    /**
     * 路径前缀
     */
    private String prefix;

    /**
     * 是否分块上传 简单上传的方式只支持最大5GB的文件上传，而通过分块上传的方式可上传大于5GB的文件
     */
    private Boolean block = false;

    /**
     * 是否启用
     */
    private Boolean enable = false;
}
