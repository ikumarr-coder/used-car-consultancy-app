package com.kik.usedcarconsultancy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class VariantConfig {

    @Bean("indianCarVariants")
    public List<String> indianCarVariants() {
        return List.of(
                "Base", "LXI", "VXI", "ZXI", "VX", "Other"
        );
    }
}
