package com.sapient.weather.service;

import com.sapient.weather.handler.OWMResponseErrorHandler;
import com.sapient.weather.model.Weather;
import com.sapient.weather.model.WeatherForecast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

@Service
public class WeatherService {
    private static final String WEATHER_URL =
            "http://api.openweathermap.org/data/2.5/weather?q={city},{country}&APPID={key}";

    private static final String FORECAST_URL =
            "http://api.openweathermap.org/data/2.5/forecast?q={city},{country}&APPID={key}";

    private String units = "imperial"; // metric
    private String lang = "en";
    public final static Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value("${app.weather.api.url}")
    private String apiUrl;

    @Value("${app.weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(new OWMResponseErrorHandler());
    }

    @Cacheable("weather")
    public Weather getWeather(String country, String city) {
        logger.info("Requesting current weather for {}/{}", country, city);
        URI url = new UriTemplate(WEATHER_URL).expand(city, country, this.apiKey);
        return invoke(url, Weather.class);
    }

    @Cacheable("forecast")
    public WeatherForecast getWeatherForecast(String country, String city) {
        logger.info("Requesting weather forecast for {}/{}", country, city);
        URI url = new UriTemplate(FORECAST_URL).expand(city, country, this.apiKey);
        return invoke(url, WeatherForecast.class);
    }

    private boolean validParameters(String city) {
        return city != null && !"".equals(city) && apiKey != null && !"".equals(apiKey) && apiUrl != null && !"".equals(apiUrl);
    }

    private <T> T invoke(URI url, Class<T> responseType) {
        T weather = null;
        try {
            RequestEntity<?> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
            ResponseEntity<T> exchange = this.restTemplate.exchange(request, responseType);
            weather = exchange.getBody();
        } catch (Exception e) {
            logger.error("An error occurred while calling openweathermap.org API endpoint:  " + e.getMessage());
        }
        return weather;
    }
}
