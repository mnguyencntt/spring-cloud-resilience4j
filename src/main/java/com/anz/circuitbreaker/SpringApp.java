package com.anz.circuitbreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringApp {
  @Autowired
  private AlbumService albumService;

  @GetMapping("/albums")
  public String albums() {
    return albumService.getAlbumList();
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringApp.class, args);
  }
}
