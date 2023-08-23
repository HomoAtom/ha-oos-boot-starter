package com.ha.oos.autoconfigure;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ReflectUtil;
import com.ha.oos.service.*;
import com.ha.oos.service.impl.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通用文件上传自动装配类
 *
 * @author cjx
 */
@Configuration
@EnableConfigurationProperties({QiniuOssProperties.class, AliOssProperties.class
        , TencentOssProperties.class, FastDFSOssProperties.class, MinioOssProperties.class, LocalOssProperties.class})
public class OosAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ha-oos.qiniu", name = "enable", havingValue = "true")
    public QiniuOosService qiniuOosService(QiniuOssProperties qiniuOssProperties) {
        QiniuOosServiceImpl qiniuOosService = new QiniuOosServiceImpl();
        qiniuOosService.config(getProperties(qiniuOssProperties, QiniuOssProperties.class));
        return qiniuOosService;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ha-oos.ali", name = "enable", havingValue = "true")
    public AliOosService aliOosService(AliOssProperties aliOssProperties) {
        AliOosServiceImpl aliOosService = new AliOosServiceImpl();
        aliOosService.config(getProperties(aliOssProperties, AliOssProperties.class));
        return aliOosService;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ha-oos.tencent", name = "enable", havingValue = "true")
    public TencentOosService tencentOosService(TencentOssProperties tencentOssProperties) {
        TencentOosServiceImpl tencentOosService = new TencentOosServiceImpl();
        tencentOosService.config(getProperties(tencentOssProperties, TencentOssProperties.class));
        return tencentOosService;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ha-oos.fastdfs", name = "enable", havingValue = "true")
    public FastDFSOosService fastDFSOosService(FastDFSOssProperties fastDFSOssProperties) {
        FastDFSOosServiceImpl fastDFSOosService = new FastDFSOosServiceImpl();
        fastDFSOosService.config(getProperties(fastDFSOssProperties, FastDFSOssProperties.class));
        return fastDFSOosService;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ha-oos.minio", name = "enable", havingValue = "true")
    public MinioOosService minioOosService(MinioOssProperties minioOssProperties) {
        MinioOosServiceImpl minioOosService = new MinioOosServiceImpl();
        minioOosService.config(getProperties(minioOssProperties, MinioOssProperties.class));
        return minioOosService;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ha-oos.local", name = "enable", havingValue = "true")
    public LocalOssService localOssService(LocalOssProperties localOssProperties) {
        LocalOssServiceImpl localOssService = new LocalOssServiceImpl();
        localOssService.config(getProperties(localOssProperties, LocalOssProperties.class));
        return localOssService;
    }

    /**
     * 获取属性配置文件，加此方法是为了保留默认的配置
     *
     * @param properties 属性配置文件
     * @param clazz      属性配置文件的类型，因为泛型擦除机制，只能加个这个参数了
     * @param <T>
     * @return 修改后的属性配置文件
     */
    <T> T getProperties(T properties, Class clazz) {
        try {
            Object obj = ReflectUtil.newInstance(clazz);
            BeanUtil.copyProperties(properties, obj, CopyOptions.create().setIgnoreNullValue(true));
            return (T) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

}
