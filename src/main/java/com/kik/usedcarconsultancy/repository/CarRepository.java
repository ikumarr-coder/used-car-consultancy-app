package com.kik.usedcarconsultancy.repository;

import com.kik.usedcarconsultancy.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    /*@Query("SELECT DISTINCT c FROM Car c LEFT JOIN FETCH c.images ORDER BY c.createdAt DESC")
    List<Car> findAllWithImagesOrderByCreatedDesc();*/

    @Query("SELECT DISTINCT c FROM Car c LEFT JOIN FETCH c.images")
    List<Car> findAllWithImages();

}
