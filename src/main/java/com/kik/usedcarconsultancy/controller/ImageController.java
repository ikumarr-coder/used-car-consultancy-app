package com.kik.usedcarconsultancy.controller;

import com.kik.usedcarconsultancy.entity.CarImage;
import com.kik.usedcarconsultancy.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final CarService carService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id){
        Optional<CarImage> img = carService.getImage(id);
        if(img.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        CarImage carImage = img.get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(carImage.getContentType()))
                .header(HttpHeaders.CACHE_CONTROL, "max-age=86400")
                .body(carImage.getData());
    }
}
