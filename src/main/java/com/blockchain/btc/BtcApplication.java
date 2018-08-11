package com.blockchain.btc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Scanner;

@SpringBootApplication
public class BtcApplication {
    public static String port;
    public static void main(String[] args) {

//        SpringApplication.run(BtcApplication.class, args);

        Scanner scanner = new Scanner(System.in);
        port= scanner.nextLine();
        new SpringApplicationBuilder(BtcApplication.class).properties("server.port="+port).run(args);

    }
}
