package com.ha.oos.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FastDFS文件上传属性配置文件
 *
 * @author cjx
 */
@Data
@ConfigurationProperties(prefix = "ha-oos.fastdfs")
public class FastDFSOssProperties {

    /**
     * FastDFS绑定的域名
     */
    private String domain;

    /**
     * 是否启用
     */
    private Boolean enable = false;
}
