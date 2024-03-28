package org.servlet;

import org.graph.CFGBuilder;
import org.graph.CFGConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.parser.*;

import java.util.Map;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.out.println("Application, Start!");
        SpringApplication.run(Application.class, args);
        System.out.println("End of Application !");
    }

}
