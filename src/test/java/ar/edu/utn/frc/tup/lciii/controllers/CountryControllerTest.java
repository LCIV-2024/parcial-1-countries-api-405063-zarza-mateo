package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.model.CountryEntity;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CountryControllerTest {
    private MockMvc mockMvc;

    @Mock
    CountryService countryService;

    @InjectMocks
    private CountryController countryController;

    Country country;
    List<Country> countries;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();
        country = new Country();
        country.setRegion("Americas");
        countries = List.of(country);

    }


    @Test
    void getCouontries() throws Exception {
        when(countryService.getAllCountries(null, null)).thenReturn(countries);
        mockMvc.perform(get("/countries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(countryService, times(1)).getAllCountries(null, null);


    }

    @Test
    void getCountryByContinent() throws Exception {
        String continente = "Americas";
        doReturn(countries).when(countryService).getCountryByContinent(continente);
        mockMvc.perform(get("/countries/{continent}/continent", continente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(countryService, times(1)).getCountryByContinent(continente);


    }

    @Test
    void getCountriesByLanguage() {
    }

    @Test
    void getCountryMostBorders() {
    }
}