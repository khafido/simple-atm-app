package org.arkaan.simpleatm;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.arkaan.simpleatm.config.AuthFilter;
import org.arkaan.simpleatm.controller.AccountServlet;
import org.arkaan.simpleatm.controller.IndexServlet;
import org.arkaan.simpleatm.controller.WithdrawServlet;
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

        Context webapp = tomcat.addWebapp("", new File("src/main/webapp/").getAbsolutePath());

        webapp.setParentClassLoader(App.class.getClassLoader());

        AccountServlet accountController = context.getBean(AccountServlet.class);
        IndexServlet indexController = context.getBean(IndexServlet.class);
        WithdrawServlet withdrawServlet = context.getBean(WithdrawServlet.class);
        tomcat.addServlet("", "AccountServlet", accountController);
        tomcat.addServlet("", "index", indexController);
        tomcat.addServlet("", "WithdrawServlet", withdrawServlet);

        webapp.addServletMappingDecoded("/account", "AccountServlet");
        webapp.addServletMappingDecoded("", "index");
        webapp.addServletMappingDecoded("/withdraw", "WithdrawServlet");
        
        FilterDef authFilter = new FilterDef();
        authFilter.setFilterName("AUTH_FILTER");
        authFilter.setFilterClass(AuthFilter.class.getName());
        webapp.addFilterDef(authFilter);
        
        FilterMap filterMap = new FilterMap();
        filterMap.addServletName("index");
        filterMap.addServletName("WithdrawServlet");
        filterMap.setFilterName("AUTH_FILTER");
        webapp.addFilterMap(filterMap);

        tomcat.start();
        tomcat.getConnector();
        tomcat.getServer().await();
    }
}
