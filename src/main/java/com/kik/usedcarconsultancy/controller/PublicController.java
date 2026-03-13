package com.kik.usedcarconsultancy.controller;

import com.kik.usedcarconsultancy.entity.Car;
import com.kik.usedcarconsultancy.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final CarService carService;

    @GetMapping("/")
    public String home(Model model){
        List<Car> cars = carService.findAll();
        model.addAttribute("cars", cars);
        return "index";
    }

    @GetMapping("/cars")
    public String listCars(Model model){
        List<Car> cars = carService.findAll();
        model.addAttribute("cars", cars);
        return "cars";
    }

    @GetMapping("/cars/{id}")
    public String carDetail(@PathVariable Long id, Model model){
        Optional<Car> car = carService.findById(id);
        if(car.isEmpty()) return "redirect:/cars";
        model.addAttribute("car", car.get());
        return "car-detail";
    }
}
