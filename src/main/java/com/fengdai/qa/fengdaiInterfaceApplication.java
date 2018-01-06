package com.fengdai.qa;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 *
 * @author hzweisc
 *
 */
@SpringBootApplication
@ServletComponentScan
public class fengdaiInterfaceApplication {
    public static void main(String[] args) {
       SpringApplication.run(fengdaiInterfaceApplication.class, args);
    }
}