package org.arkaan.simpleatm;

import org.arkaan.simpleatm.controller.AccountController;
import org.arkaan.simpleatm.dto.request.AuthDto;
import org.arkaan.simpleatm.dto.response.AccountDto;
import org.arkaan.simpleatm.dto.response.Response;
import org.arkaan.simpleatm.model.Account;
import org.arkaan.simpleatm.model.Status;
import org.arkaan.simpleatm.repository.Repository.*;
import org.arkaan.simpleatm.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.*;


@SpringBootApplication
public class App {

    public static void main(String[] args) {
        Map<String, Object> properties = new HashMap<>();

        new SpringApplicationBuilder(App.class)
                .properties(properties)
                .build()
                .run(args);
    }

    @Bean
    public CommandLineRunner runner(ApplicationContext context) {
        return args -> {
            AccountController accountController = context.getBean(AccountController.class);

            AuthDto authDto = new AuthDto(776643, 123456);

            System.out.println("test login");
            Thread.sleep(2000);

            Response<AccountDto> authenticate = accountController.authenticate(authDto);

            if (authenticate.getStatus() == Status.SUCCESS) {
                System.out.println("wow you're in");
            } else {
                System.out.println(authenticate.getMsg());
            }
        };
    }
}
