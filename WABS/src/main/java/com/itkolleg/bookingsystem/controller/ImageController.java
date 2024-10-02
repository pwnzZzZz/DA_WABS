package com.itkolleg.bookingsystem.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;

@Controller
public class ImageController {

    @GetMapping(value = "/web/static/img/Grundriss.svg", produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> getGrundrissSvg() throws IOException {
        Resource resource = new ClassPathResource("static/img/Grundriss.svg");
        byte[] svgBytes = Files.readAllBytes(resource.getFile().toPath());
        return ResponseEntity.ok().body(svgBytes);
    }
}