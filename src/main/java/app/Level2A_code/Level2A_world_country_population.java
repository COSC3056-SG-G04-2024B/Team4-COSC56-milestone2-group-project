package app.Level2A_code;

public class Level2A_world_country_population {
    public String country_name;
    public int year;
    public int population;
    public int population_raw_change;
    public double population_proportional_change;
    public Level2A_world_country_population(){}

    public Level2A_world_country_population(String country_name, int year, int population, int population_raw_change, double population_proportional_change) {
        this.country_name = country_name;
        this.year = year;
        this.population = population;
        this.population_raw_change = population_raw_change;
        this.population_proportional_change = population_proportional_change;
    }
}