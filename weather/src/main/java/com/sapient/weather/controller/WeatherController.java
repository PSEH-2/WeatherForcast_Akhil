package com.sapient.weather.controller;

import com.sapient.weather.model.WeatherForecast;
import com.sapient.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/v1/openWeatherAPI")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weekly/{country}/{city}")
    public WeatherForecast getWeatherForecast(@PathVariable String country,
                                              @PathVariable String city) {
        WeatherForecast weatherForecast = this.weatherService.getWeatherForecast(country, city);
        weatherForecast.getEntries();
        return weatherForecast;
    }
}
