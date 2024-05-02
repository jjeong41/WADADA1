package org.api.wadada.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;
import javax.net.ssl.TrustManagerFactory;

@Configuration
@EnableElasticsearchRepositories(basePackages = "org.api.wadada.multi.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${ELS_USERNAME}")
    private String username;

    @Value("${spring.elasticsearch.password}")
    private String password;

    @Value("${spring.elasticsearch.uris}")
    private String[] esHost;

    @Value("classpath:http.p12")
    private Resource elasticsearchCert;

    @Bean
    public SSLContext sslContext() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        char[] keyStorePassword = "-FDQ0vqATd6sm5lWRUMHIQ".toCharArray();
        try (InputStream trustStoreInputStream = elasticsearchCert.getInputStream()) {
            keyStore.load(trustStoreInputStream, keyStorePassword);
        }
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;
    }


    @Override
    public ClientConfiguration clientConfiguration() {
        try {
            return ClientConfiguration.builder()
                    .connectedTo(esHost)
                    .usingSsl(sslContext())  // Use the SSLContext with the loaded CA certificate
                    .withBasicAuth(username, password)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
