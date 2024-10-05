package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.CountryDTO;
import ar.edu.utn.frc.tup.lciii.exception.CountryNotFoundException;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

    @Autowired
    private final CountryRepository countryRepository;

    @Autowired
    private final RestTemplate restTemplate;


    public List<Country> getAllCountries(String code, String name) {
        String url = UriComponentsBuilder.fromHttpUrl("https://restcountries.com/v3.1/all")
                .queryParam("cca3", code)
                .queryParam("name", name)
                .toUriString();

        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

        if (code != null) {
            List<Country> countries = response.stream().map(this::mapToCountry).collect(Collectors.toList());
            List<Country> countriesFinal = new ArrayList<>();
            for (Country country : countries) {
                if (Objects.equals(country.getCode(), code))
                    countriesFinal.add(country);
            }
            return countriesFinal;
        }

        if (name != null) {
            List<Country> countries = response.stream().map(this::mapToCountry).collect(Collectors.toList());
            List<Country> countriesFinal = new ArrayList<>();
            for (Country country : countries) {
                if (Objects.equals(country.getName(), name))
                    countriesFinal.add(country);
            }
            return countriesFinal;
        }

        return response.stream().map(this::mapToCountry).collect(Collectors.toList());
//        Country[] countries = restTemplate.getForEntity(url, Country[].class).getBody();
//
//        return List.of(countries);
    }



    public List<CountryDTO> getCountryByContinent(String region) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl("https://restcountries.com/v3.1/all")
                    .queryParam("region", region)
                    .toUriString();

            List<String> continents = List.of("Africa", "Americas", "Asia", "Europe", "Oceania", "Antarctic");
            if (!continents.contains(region)) {
                throw new CountryNotFoundException("That region does not exist or it was misspelled. ");
            }

            List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
            List<Country> countryListHelper = response.stream().map(this::mapToCountry).collect(Collectors.toList());
            List<CountryDTO> countryListFinal = new ArrayList<>();
            for (Country country : countryListHelper) {
                if (Objects.equals(country.getRegion(), region)) {

                    countryListFinal.add(mapToDTO(country));
                }
            }

            return countryListFinal;


        } catch (
                RestClientException e) {
            throw new CountryNotFoundException("Error trying to get the countries");

        }
    }

//    public List<Country> getCountryByLanguage(String language) {
//        try {
//            String url = UriComponentsBuilder.fromHttpUrl("https://restcountries.com/v3.1/all")
//                    .queryParam("language", language)
//                    .toUriString();
//
//            List<String> languages = List.of("English", "Spanish", "French", "German",
//                    "Portuguese", "Chinese", "Arabic", "Russian", "Hindi", "Swahili");
//            if (!languages.contains(language)) {
//                throw new CountryNotFoundException("That language does not exist or it was misspelled. ");
//            }
//
//            List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
//            List<Country> countryListHelper = response.stream().map(this::mapToCountry).collect(Collectors.toList());
//            List<Country> countryListFinal = new ArrayList<>();
//            for (Country country : countryListHelper) {
//                if (country.getLanguages()==null)
//                    countryListHelper.remove(country);
//                if (country.getLanguages().containsValue(language))
//                    countryListFinal.add(country);
//            }
//
//            return countryListFinal;
//
//
//        } catch (
//                RestClientException e) {
//            throw new CountryNotFoundException("Error trying to get the countries");
//
//        }
//    }

    /**
     * No funciono el de arriba
     */
    public List<CountryDTO> getCountryByLanguage(String language) {
        try {
            List<String> languagesList = List.of("English", "Spanish", "French", "German",
                    "Portuguese", "Chinese", "Arabic", "Russian", "Hindi", "Swahili");
            if (!languagesList.contains(language)) {
                throw new CountryNotFoundException("That language does not exist or it was misspelled. ");
            }


            return getAllCountries(null, null).stream()
                    .filter(country -> {
                        Map<String, String> languages = country.getLanguages();
                        return languages != null && languages.containsValue(language);
                    })
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (
                RestClientException e) {
            throw new CountryNotFoundException("Error trying to get the countries");

        }
    }

    public CountryDTO getCountryMostBorders() {
        List<Country> countries = getAllCountries(null, null);
        return countries.stream()
                .filter(country -> country.getBorders() != null && !country.getBorders().isEmpty())
                .max(Comparator.comparingInt(country -> country.getBorders().size()))
                .map(this::mapToDTO)
                .orElseThrow(() -> new CountryNotFoundException("An error has been found getting the countrise."));
    }


    /**
     * Agregar mapeo de campo cca3 (String)
     * Agregar mapeo campos borders ((List<String>))
     */
    private Country mapToCountry(Map<String, Object> countryData) {
        Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");

        return Country.builder()
                .name((String) nameData.get("common"))
                .code((String) countryData.get("cca3"))
                .borders((List<String>) countryData.get("borders"))
                .population(((Number) countryData.get("population")).longValue())
                .area(((Number) countryData.get("area")).doubleValue())
                .region((String) countryData.get("region"))
                .languages((Map<String, String>) countryData.get("languages"))
                .build();
    }


    private CountryDTO mapToDTO(Country country) {
        return new CountryDTO(country.getCode(), country.getName());
    }

}