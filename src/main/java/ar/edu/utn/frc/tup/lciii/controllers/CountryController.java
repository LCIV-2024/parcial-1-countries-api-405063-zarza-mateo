package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.CountryRequest;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CountryController {

    @Autowired
    private final CountryService countryService;

    @GetMapping("/countries")
    List<Country> getCouontries(@RequestParam(value = "countryName", required = false) String name,
                                @RequestParam(value = "countryCode", required = false) String code) {
        return countryService.getAllCountries(code, name);
    }

    @GetMapping("/countries/{continent}/continent")
    List<CountryDTO> getCountryByContinent(@PathVariable String region){
        return  countryService.getCountryByContinent(region);
    }

    @GetMapping("/countries/{language}/language")
    List<CountryDTO> getCountriesByLanguage(@PathVariable String language){
        return  countryService.getCountryByLanguage(language);
    }

    @GetMapping("/countries/most-borders")
    CountryDTO GetCountryMostBorders(){
        return  countryService.getCountryMostBorders();
    }

    @PostMapping("/countries")
    public List<CountryDTO> saveRandomCountries(@RequestBody CountryRequest request) {
        int amountToSave = request.getAmountOfCountryToSave();
        if (amountToSave > 10) {
            throw new IllegalArgumentException("The maximum numberof countries to save is 10.");
        }

        return countryService.saveRandomCountries(amountToSave);
    }

}