package com.sapient.weather.controller;

import com.sapient.weather.model.Weather;
import com.sapient.weather.model.WeatherEntry;
import com.sapient.weather.model.WeatherForecast;
import com.sapient.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/openWeatherAPI")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @RequestMapping("/now/{country}/{city}")
    public Weather getWeather(@PathVariable String country,
                              @PathVariable String city) {
        return this.weatherService.getWeather(country, city);
    }

    @GetMapping("/weekly/{country}/{city}")
    public WeatherForecast getWeatherForecast(@PathVariable String country,
                                              @PathVariable String city) {
        WeatherForecast weatherForecast = this.weatherService.getWeatherForecast(country, city);
        List<WeatherEntry> weatherEntryList = weatherForecast.getEntries();
        for (WeatherEntry weatherEntry : weatherEntryList) {
            weatherEntry.getTimestamp();
            weatherEntry.getTemperature();
        }
        return weatherForecast;
    }
}
