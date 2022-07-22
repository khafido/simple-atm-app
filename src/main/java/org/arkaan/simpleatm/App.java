package org.arkaan.simpleatm;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.arkaan.simpleatm.controller.AccountController;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class App {

    public static void main(String[] args) throws LifecycleException {
        Map<String, Object> properties = new HashMap<>();

        ApplicationContext context = new SpringApplicationBuilder(App.class)
                .properties(properties)
                .build()
                .run(args);

        runTomcat(context);
    }

    static void runTomcat(ApplicationContext context) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8082);

        Context webapp = tomcat.addWebapp("", new File(".").getAbsolutePath());

        AccountController accountController = context.getBean(AccountController.class);

        tomcat.addServlet("", "AccountServlet", accountController);

        webapp.addServletMappingDecoded("/account", "AccountServlet");

        tomcat.start();
        tomcat.getConnector();
        tomcat.getServer().await();
    }
}
