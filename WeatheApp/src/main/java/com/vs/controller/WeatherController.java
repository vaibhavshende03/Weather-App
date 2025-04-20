package com.vs.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.vs.Model.WeatherResponse;



@Controller
public class WeatherController {
	@Value("${api.key}")
	private String apiKey;

	@GetMapping("/")
	public String getIndex() {
		return "index";
	}
	@GetMapping("/weather")
	public String getWeather(@RequestParam("city") String city,Model model) {
		String url="https://api.openweathermap.org/data/2.5/weather?q="+city+"+&appid="+apiKey+"&units=metric";
				RestTemplate restTemplate=new RestTemplate();
				 try {
			            WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);

			            if (weatherResponse != null) {
			                model.addAttribute("city", weatherResponse.getName());
			                model.addAttribute("country", weatherResponse.getSys().getCountry());
			                model.addAttribute("weatherDescription", weatherResponse.getWeather().get(0).getDescription());
			                model.addAttribute("temperature", weatherResponse.getMain().getTemp());
			                model.addAttribute("humidity", weatherResponse.getMain().getHumidity());
			                model.addAttribute("windSpeed", weatherResponse.getWind().getSpeed());

			                String weatherIcon = "wi wi-owm-" + weatherResponse.getWeather().get(0).getId();
			                model.addAttribute("weatherIcon", weatherIcon);
			            }

			        } catch (HttpClientErrorException.NotFound e) {
			            model.addAttribute("error", "City not found. Please check the name and try again.");
			        } catch (Exception e) {
			            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
			            e.printStackTrace();
			        }
				return "weather";
	}
}
