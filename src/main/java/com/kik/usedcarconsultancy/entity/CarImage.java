package com.kik.usedcarconsultancy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="car_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarImage {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="car_id", nullable = false)
    private Car car;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @Column(name="content_type", nullable=false, length=100)
    private String contentType;

    @Column(name="sort_order")
    private int sortOrder;

}
