package ar.edu.utn.frc.tup.lciii.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Country {
    private String name;
    private long population;
    private double area;
    private String code;
    private String region;
    private List<String> borders;
    private Map<String, String> languages;
}