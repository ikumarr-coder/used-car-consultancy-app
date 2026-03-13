package com.kik.usedcarconsultancy.controller;

import com.kik.usedcarconsultancy.entity.Car;
import com.kik.usedcarconsultancy.service.CarService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CarService carService;
    private final List<String> variant;

    public AdminController(CarService carService, @Qualifier("indianCarVariants") List<String> variant) {
        this.carService = carService;
        this.variant = variant;
    }

    @GetMapping
    public String admin(Model model) {
        model.addAttribute("cars", carService.findAll());
        return "admin/index";
    }

    @GetMapping("/new")
    public String newCarForm(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("variants", variant);
        return "admin/car-form";
    }

    @PostMapping("/save")
    public String saveCar(
            @RequestParam(required = false) Long id,
            @RequestParam String model,
            @RequestParam String price,
            @RequestParam String variant,
            @RequestParam String variantOther,
            @RequestParam String proprietorName,
            @RequestParam String proprietorMobile,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            RedirectAttributes ra
    ) {
        String finalVariant = "Other".equals(variant) && variantOther != null && !variantOther.isBlank() ? variantOther.trim() : variant;
        Car car;
        if(id != null) {
            car = carService.findById(id).orElse(new Car());
        } else {
            car = new Car();
        }

        car.setModel(model);
        car.setPrice(price);
        car.setVariant(finalVariant);
        car.setProprietorName(proprietorName);
        car.setProprietorMobile(proprietorMobile.replaceAll("\\s", ""));
        carService.save(car, images != null ? images : List.of());
        ra.addFlashAttribute("message", "Car saved successfully");
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editCar(@PathVariable Long id, Model model) {
        Car car = carService.findById(id).orElse(null);
        if(car == null) return "redirect:/admin";
        model.addAttribute("car", car);
        model.addAttribute("variants", variant);
        return "admin/car-form";
    }

    @PostMapping("/add-images/{id}")
    public String addImage(@PathVariable Long id, @RequestParam("images") List<MultipartFile> images ,RedirectAttributes ra) {
        carService.addImages(id, images != null ? images : List.of());
        ra.addFlashAttribute("message", "Car Images added successfully");
        return "redirect:/admin/edit/"+id;
    }

    @PostMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id, RedirectAttributes ra) {
        carService.deleteCar(id);
        ra.addFlashAttribute("message", "Car deleted successfully");
        return "redirect:/admin";
    }

    @PostMapping("/delete-image")
    public String deleteImage(@PathVariable Long carId, @RequestParam Long imageId, RedirectAttributes ra) {
        carService.deleteImage(carId, imageId);
        ra.addFlashAttribute("message", "Car Image deleted successfully");
        return "redirect:/admin/edit/"+carId;
    }

}
