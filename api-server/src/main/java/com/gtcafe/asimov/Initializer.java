package com.gtcafe.asimov;

import javax.sql.DataSource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.utils.Slogan;

@Service
public class Initializer implements ApplicationRunner  {

    @Autowired
    private Slogan utils;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private final Environment env;

    public Initializer(Environment env) {
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // logger.info("ApplicationRunner.run()");
        // 1. show environment variables of application
        System.out.printf("JAVA_HOME: [%s]\n", env.getProperty("JAVA_HOME"));
        // System.out.printf("APP_NAME: [%s]\n", env.getProperty("APP_NAME"));
        // System.out.printf("app.name: [%s]\n", env.getProperty("app.name"));

        // 2. show config path from ...
        System.out.printf("Datasource: [%s]\n", dataSource.getConnection().toString());
        System.out.printf("MessageQueue: [%s}]\n", rabbitTemplate.toString());

        // 3. show slogan, and tell user the application is ready.
        System.out.println(utils.slogan());

    }

}

