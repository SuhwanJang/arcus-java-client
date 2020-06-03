package com.jam2in.arcus.admin.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  // -Dspring.profiles.active=dev
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
