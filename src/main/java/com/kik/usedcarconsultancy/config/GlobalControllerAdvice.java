package com.kik.usedcarconsultancy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Value("${usedcar.consultancy.name:Ram Reddy Car Consultancy}")
    private String consultancyName;

    @Value("${usedcar.consultancy.mobiles:}")
    private String consultancyMobilesStr;

    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
    }

    @ModelAttribute("consultancyName")
    public String getConsultancyName() {
        return consultancyName != null ? consultancyName : "Ram Reddy Car Consultancy";
    }

    @ModelAttribute("consultancyMobiles")
    public List<String> consultancyMobiles() {
        if(consultancyMobilesStr == null || consultancyMobilesStr.isBlank()) {
            return List.of();
        }
        return Arrays.stream(consultancyMobilesStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    @ModelAttribute("consultancyMobileDigits")
    public String getConsultancyMobileDigits() {
        List<String> list = consultancyMobiles();
        if(list == null || list.isEmpty()) return  "";
        String digits = list.get(0).replaceAll("[^0-9]", "");
        if(digits.startsWith("91") && digits.length() > 10) digits = digits.substring(2);
        if(digits.startsWith("0")) digits = digits.substring(1);
        return digits;
    }
}
