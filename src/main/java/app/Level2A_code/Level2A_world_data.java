package app.Level2A_code;

public class Level2A_world_data 
{
    public int year;
    public double average_temperature;
    public int population;
    public double world_temperature;
    public long world_population;
    public Level2A_world_data() {}

    public Level2A_world_data(int year, double average_temperature, int population, double world_temperature, long world_population) {
        this.year = year;
        this.population = population;
        this.average_temperature = average_temperature;
        this.world_temperature = world_temperature;
        this.world_population = world_population;
    }
}
