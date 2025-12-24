package com.acesoft.aceoffix7springbootsimple;

import com.acesoftcorp.aceoffix.aceserver.AceWContextListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
public class Aceoffix7SpringbootSimpleApplication {
    @Value("${aceSysPath}")
    private String aceSysPath;
    public static void main(String[] args) {
        SpringApplication.run(Aceoffix7SpringbootSimpleApplication.class, args);
    }
    @Bean
    public ServletRegistrationBean aceoffixRegistrationBean() {
        com.acesoftcorp.aceoffix.aceserver.Server aceserver = new com.acesoftcorp.aceoffix.aceserver.Server();
        //Set the directory where the license.lic file is stored after successful registration of Aceoffix.
        aceserver.setSysPath(aceSysPath);
        ServletRegistrationBean srb = new ServletRegistrationBean(aceserver);
        srb.addUrlMappings("/server.ace");
        srb.addUrlMappings("/aceclient");
        srb.addUrlMappings("/aceoffix.js");
        return srb;
    }
    /**
     *Enable WebSocket configuration for Aceoffix. Required for Aceoffix v7.3.1.1 and above.
     *@return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        ServerEndpointExporter exporter = new ServerEndpointExporter();
        exporter.setAnnotatedEndpointClasses(
                com.acesoftcorp.aceoffix.aceserver.WServer.class
        );
        return exporter;
    }

    /**
     *Aceoffix acewserver configure cross-domain. Required for Aceoffix v7.3.1.1 and above.
     *
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean acewContextListener() {
        return new ServletListenerRegistrationBean<>(new AceWContextListener());
    }

    @Bean
    public ServletContextInitializer aceoffixContextParams() {
        /*
         * Aceoffix acewserver Cross-Domain Security Configuration:
         * 1. Using "*" is not recommended in production. It is advisable to explicitly specify allowed domain(s)/IP(s).
         * 2. Format: Multiple addresses should be separated by commas, e.g., "domain1,domain2,ip".
         *    Note: Local development environment addresses (localhost, 127.0.0.1) must also be included in this configuration.
         * 3. Examples:
         *    - For Frontend/Backend Separation: "frontend-domain-address, frontend-ip-address, backend-address"
         *      (e.g., "ui.example.com,192.168.1.1,localhost")
         *    - For Monolithic Multi-Entry Applications: "domain, ip"
         *      (e.g., "www.oa.com,192.168.1.100")
         */
        return servletContext ->
                servletContext.setInitParameter("acewserver-allowedOrigins", "*");
    }
}
