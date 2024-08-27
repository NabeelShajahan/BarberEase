package com.example.barberease;

import java.util.List;

public class WeatherResponse {
    private Main main;
    private List<Weather> weather;

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public class Main {
        private double temp;

        public double getTemp() {
            return temp;
        }
    }

    public class Weather {
        private String description;

        public String getDescription() {
            return description;
        }
    }
}
