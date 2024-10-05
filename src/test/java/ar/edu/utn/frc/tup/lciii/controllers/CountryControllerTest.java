package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.CountryDTO;
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
        String continent = "Americas";
        doReturn(countries).when(countryService).getCountryByContinent(continent);
        mockMvc.perform(get("/countries/{continent}/continent", continent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(countryService, times(1)).getCountryByContinent(continent);

    }

    @Test
    void getCountriesByLanguage() throws Exception {
        String language = "Spanish";
        doReturn(countries).when(countryService).getCountryByContinent(language);
        mockMvc.perform(get("/countries/{language}/language", language)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(countryService, times(1)).getCountryByLanguage(language);

    }

        @Test
    void getCountryMostBorders() throws Exception {
            CountryDTO countryDTO = new CountryDTO("CN", "China");
            doReturn(countryDTO).when(countryService).getCountryMostBorders();
            mockMvc.perform(get("/countries/most-borders")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(countryService, times(1)).getCountryMostBorders();


        }
}