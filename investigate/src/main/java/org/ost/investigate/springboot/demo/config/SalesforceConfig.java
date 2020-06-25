package org.ost.investigate.springboot.demo.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@NoArgsConstructor
@ToString
@ConfigurationProperties(prefix = "salesforce.application.host")
public class SalesforceConfig {
    private String acc;
    private String app;
}
