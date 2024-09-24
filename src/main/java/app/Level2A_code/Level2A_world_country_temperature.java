package app.Level2A_code;

public class Level2A_world_country_temperature {
    public int year;
    public double average_temperature;
    public String country;
    public double temperature_raw_change;
    public double temperature_proportional_change;
    
    public Level2A_world_country_temperature() {}

    public Level2A_world_country_temperature(int year, double average_temperature, double max_temperature, String country, double temperature_raw_change, double temperature_proportional_change) {
        this.year = year;
        this.average_temperature = average_temperature;
        this.country = country;
        this.temperature_raw_change = temperature_raw_change;
        this.temperature_proportional_change= temperature_proportional_change;
    }
}
