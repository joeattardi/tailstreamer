package com.thinksincode.tailstreamer.config;

import org.apache.catalina.connector.Connector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {
    private final static Log logger = LogFactory.getLog(TomcatConfig.class);

    @Autowired
    private SSLConfig sslConfig;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();

        if (sslConfig != null && sslConfig.isEnable()) {
            tomcat.addAdditionalTomcatConnectors(createSslConnector());
        }

        return tomcat;
    }

    private Connector createSslConnector() {
        if (sslConfig == null) {
            throw new IllegalStateException("SSL configuration not specified");
        }

        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        connector.setScheme("https");
        connector.setSecure(true);
        connector.setPort(sslConfig.getPort());
        protocol.setSSLEnabled(sslConfig.isEnable());
        protocol.setKeystoreFile(sslConfig.getKeystore());
        protocol.setKeystorePass(sslConfig.getKeystorePassword());
        protocol.setKeyAlias(sslConfig.getKeyAlias());

        logger.info(String.format("Initializing SSL connector on port %d", sslConfig.getPort()));
        return connector;
    }
}
