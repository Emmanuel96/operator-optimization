package com.wailo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties specific to Operator Optimisation.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    // jhipster-needle-application-properties-property
    // jhipster-needle-application-properties-property-getter
    // jhipster-needle-application-properties-property-class

    private String ezOperationsBaseUrl;
    private String ezOperationsBearerToken;
    private String ezOperationsXsrfToken;

    public String getEzOperationsBaseUrl() {
        return ezOperationsBaseUrl;
    }

    public String getEzOperationsBearerToken() {
        return ezOperationsBearerToken;
    }

    public String getEzOperationsXsrfToken() {
        return ezOperationsXsrfToken;
    }

    public void setEzOperationsBaseUrl(String ezOperationsBaseUrl) {
        this.ezOperationsBaseUrl = ezOperationsBaseUrl;
    }

    public void setEzOperationsBearerToken(String ezOperationsBearerToken) {
        this.ezOperationsBearerToken = ezOperationsBearerToken;
    }

    public void setEzOperationsXsrfToken(String ezOperationsXsrfToken) {
        this.ezOperationsXsrfToken = ezOperationsXsrfToken;
    }
}
