package com.spring.boot.demo.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * @author joe.ly
 * @date 2022/10/23
 */
@Component("conditionalBean")
@ConditionalOnExpression("T(org.springframework.util.StringUtils).isEmpty('${bcp.view.resolver.oss.provider:}') "
    + "OR '${bcp.view.resolver.oss.provider}'.equals('OSS') "
    + "OR '${bcp.view.resolver.oss.provider}'.equals('S3')")
public class ConditionalBean {
    @Value("${bcp.view.resolver.oss.ak}")
    private String provider;

    private String value;

    public String getProvider() {
        return this.provider;
    }

    public String getValue() {
        // generate 3 random floats
        return this.value;
    }
}
