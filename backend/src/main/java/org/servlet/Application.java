package org.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.parser.*;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Parser parser = new Parser();

        System.out.println("Application, Start!");
        SpringApplication.run(Application.class, args);
        System.out.println("End of Application !");
    }

}
