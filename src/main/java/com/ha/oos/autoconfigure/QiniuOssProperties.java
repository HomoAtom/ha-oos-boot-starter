package com.ha.oos.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 七牛云文件上传属性配置文件
 *
 * @author cjx
 */
@Data
@ConfigurationProperties(prefix = "ha-oos.qiniu")
public class QiniuOssProperties {

    /**
     * 七牛云绑定的域名
     */
    private String domain;

    /**
     * 七牛云ACCESS_KEY
     */
    private String accessKey;

    /**
     * 七牛云SECRET_KEY
     */
    private String secretKey;

    /**
     * 七牛云存储空间名
     */
    private String bucketname;

    /**
     * 路径前缀
     */
    private String prefix;

    /**
     * 是否启用
     */
    private Boolean enable = false;
}
