package com.thinksincode.tailstreamer.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

@Configuration
public class TomcatConfig {
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(@Value("${server.port:8080}") final int port,
        @Value("${ssl.keystore.file:null}") final String keystoreFile, @Value("${ssl.keystore.password:null}") final String keystorePassword,
        @Value("${ssl.keystore.type:null}") final String keystoreType, @Value("${ssl.keystore.alias:null}") final String keystoreAlias,
        @Value("${ssl.enable:false}") final boolean useSSL
    ) throws FileNotFoundException {
        final String absoluteKeystoreFile = ResourceUtils.getFile(keystoreFile).getAbsolutePath();

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) container;
                containerFactory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
                    @Override
                    public void customize(Connector connector) {
                        if (useSSL) {
                            connector.setSecure(true);
                            connector.setScheme("https");
                            connector.setAttribute("keystoreFile", absoluteKeystoreFile);
                            connector.setAttribute("keystorePass", keystorePassword);
                            connector.setAttribute("keystoreType", keystoreType);
                            connector.setAttribute("keyAlias", keystoreAlias);
                            connector.setAttribute("clientAuth", "false");
                            connector.setAttribute("sslProtocol", "TLS");
                            connector.setAttribute("SSLEnabled", true);
                        }
                        connector.setPort(port);
                    }
                });
            }
        };
//
////        return (ConfigurableEmbeddedServletContainer factory) -> {
//              return new ConfigurableEmbeddedServletContainer() {
//              @Override
//              public void customize(Con)
//            TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) factory;
//            containerFactory.addConnectorCustomizers((TomcatConnectorCustomizer) (Connector connector) -> {
//                connector.setSecure(true);
//                connector.setScheme("https");
//                connector.setAttribute("keystoreFile", absoluteKeystoreFile);
//                connector.setAttribute("keystorePass", keystorePassword);
//                connector.setAttribute("keystoreType", keystoreType);
//                connector.setAttribute("keyAlias", keystoreAlias);
//                connector.setAttribute("clientAuth", "false");
//                connector.setAttribute("sslProtocol", "TLS");
//                connector.setAttribute("SSLEnabled", true);
//            });
//        };
    }
}
