package com.kik.usedcarconsultancy.service;

import com.kik.usedcarconsultancy.entity.Car;
import com.kik.usedcarconsultancy.entity.CarImage;
import com.kik.usedcarconsultancy.repository.CarImageRepository;
import com.kik.usedcarconsultancy.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarImageRepository carImageRepository;

    @Transactional(readOnly = true)
    public List<Car> findAll() {
        // return carRepository.findAllWithImagesOrderByCreatedDesc();
        List<Car> list = carRepository.findAllWithImages();
        list.sort(Comparator.comparing(Car::getCreateAt).reversed());
        return list;
    }

    @Transactional(readOnly = true)
    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    @Transactional
    public Car save(Car car, List<MultipartFile> imageFiles) {
        if(imageFiles != null && !imageFiles.isEmpty()) {
            int order = 0;
            for(MultipartFile file : imageFiles) {
                if(file == null || file.isEmpty()) continue;
                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")) continue;
                try {
                    CarImage img = CarImage.builder()
                            .car(car)
                            .data(file.getBytes())
                            .contentType(contentType)
                            .sortOrder(order++)
                            .build();
                    car.getImages().add(img);
                } catch (Exception ignored) {}
            }
        }
        return carRepository.save(car);
    }

    @Transactional
    public void addImages(Long carId, List<MultipartFile> imageFiles) {
        Car car = carRepository.findById(carId).orElse(null);
        if(car == null || imageFiles == null) return;
        int maxOrder = car.getImages().stream().mapToInt(CarImage::getSortOrder).max().orElse(-1);
        int order = maxOrder + 1;
        for(MultipartFile file : imageFiles) {
            if(file == null || file.isEmpty()) continue;
            String contentType = file.getContentType();
            if(contentType == null || !contentType.startsWith("image/")) continue;
            try {
                CarImage img = CarImage.builder()
                        .car(car)
                        .data(file.getBytes())
                        .contentType(contentType)
                        .sortOrder(order++)
                        .build();
                car.getImages().add(img);
            } catch (Exception ignored) {}

        }
        carRepository.save(car);
    }

    @Transactional
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Transactional
    public void deleteImage(Long carId, Long imageId) {
        Car car = carRepository.findById(carId).orElse(null);
        if(car == null) return;
        car.getImages().removeIf(img -> img.getId().equals(imageId));
        carRepository.save(car);
    }

    @Transactional(readOnly = true)
    public Optional<CarImage> getImage(Long id) {
        return carImageRepository.findById(id);
    }


}
