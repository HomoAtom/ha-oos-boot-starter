package com.ha.oos.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 本地上传属性配置文件
 *
 * @author cjx
 */
@Data
@ConfigurationProperties(prefix = "ha-oos.local")
public class LocalOssProperties {

    /**
     * 本地上传绑定的域名
     */
    public String domain;

    /**
     * 本地上传存储目录
     */
    public String path;

    /**
     * 路径前缀
     */
    private String prefix;

    /**
     * 是否启用
     */
    public Boolean enable = false;
}
