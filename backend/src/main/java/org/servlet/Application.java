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
        Parser parser = new Parser();

//        CFGBuilder builder = new CFGBuilder();
//        builder.buildCFGs("backend/examples/Simple.java");
//        // Convert the globalCFGMap to a JSON string using CFGConverter
//        String convertedCFGsJSON = CFGConverter.convertAllCFGs(builder.globalCFGMap);
//        System.out.println(convertedCFGsJSON);

        System.out.println("Application, Start!");
        SpringApplication.run(Application.class, args);
        System.out.println("End of Application !");
    }

}
