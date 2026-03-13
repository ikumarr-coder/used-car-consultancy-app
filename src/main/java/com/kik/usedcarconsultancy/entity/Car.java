package com.kik.usedcarconsultancy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private String variant;

    @Column(nullable = false, name="proprietor_name")
    private String proprietorName;

    @Column(nullable = false, name="proprietor_mobile")
    private String proprietorMobile;

    @Column(name="create_at", nullable = false, updatable = false)
    private Instant createAt;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder")
    @Builder.Default
    private List<CarImage> images = new ArrayList<>();

    public String getProprietorMobileDigits() {
        if(proprietorMobile == null) return "";
        String digits = proprietorMobile.replaceAll("[^0-9]", "");
        if(digits.startsWith("91") && digits.length() > 10) digits = digits.substring(2);
        if(digits.startsWith("0")) digits = digits.substring(1);
        return digits;
    }

    @PrePersist
    void createAt() {
        if(this.createAt == null) this.createAt = Instant.now();
    }
}
