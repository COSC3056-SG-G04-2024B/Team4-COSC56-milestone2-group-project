package app.Level2A_code;

import java.util.ArrayList;
import io.javalin.http.Context;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

public class Level2A_JDBC {

    public static final String DATABASE = "jdbc:sqlite:database/dataset_Climate.db";

    public Level2A_JDBC() {
        System.out.println("Created Level2A JDBC connection object");
    }

// 1. GET THE COUNTRY LIST  *** DONE ***
    public ArrayList<String> getCountryList()
    {
        ArrayList<String> countryNames = new ArrayList<String>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String query = """
                           SELECT DISTINCT country
                           FROM GlobalYearlyLandTempByCountry
                           ORDER BY country ASC;
                           """;

            ResultSet results = statement.executeQuery(query);

            while(results.next())
            {
                String countryName = results.getString("country");
                countryNames.add(countryName);
            }
            statement.close();
        }
        catch(SQLException e)
            {
                System.err.println(e.getMessage());
            }

        finally 
            {
            try 
                {
                    if (connection != null) 
                        connection.close();
                } 
            catch (SQLException e) 
                {
                    System.err.println(e.getMessage());
                }
            }
        return countryNames;
    }

// 2. GET COUNTRY TEMPERATURE
    public ArrayList<Level2A_world_country_temperature> getWorldCountryTemperature(String country, String startYear, String endYear, String statisticView )
    {
        ArrayList<Level2A_world_country_temperature> countryTemperature = new ArrayList<Level2A_world_country_temperature>();

        Connection connection = null;

        try
        {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            
            String query = "";
            if ("proportionalValue".equals(statisticView))
            {
                query = """
                        SELECT Country, Year, ROUND(AverageTemperature, 2) AS averageTemp,
                            (
                                SELECT ROUND((AVG(AverageTemperature) - 
                                (
                                SELECT AVG(AverageTemperature) 
                                FROM GlobalYearlyLandTempByCountry 
                                WHERE Country = "{0}" AND 
                                    Year = {1}
                                )) / 
                                (
                                SELECT AVG(AverageTemperature)
                                FROM GlobalYearlyLandTempByCountry
                                WHERE Country = "{0}" AND 
                                        YEAR = {1}
                                ) * 100, 2)
                                FROM GlobalYearlyLandTempByCountry
                                WHERE Country = "{0}" AND 
                                        YEAR = {2}
                            ) AS temp_proportional_change
                                FROM GlobalYearlyLandTempByCountry
                                WHERE Country = "{0}" AND (YEAR = {1} OR YEAR = {2});
                        """;
            } 
            else 
            {
                query = """
                        SELECT Country, Year, 
                        ROUND(AverageTemperature, 2) As averageTemp,
                        (
                            SELECT ROUND(AVG(AverageTemperature), 2)
                            FROM GlobalYearlyLandTempByCountry
                            WHERE COUNTRY LIKE "{0}" AND 
                                YEAR = {2}
                        ) - 
                        (
                            SELECT ROUND(AVG(AverageTemperature), 2)
                            FROM GlobalYearlyLandTempByCountry
                            WHERE Country LIKE "{0}" AND 
                                YEAR = {1}
                        )
                            AS temp_raw_change
                        FROM GlobalYearlyLandTempByCountry
                        WHERE COUNTRY LIKE "{0}" AND 
                        (YEAR = {1} OR YEAR = {2});
                            """;
            }
                query = MessageFormat.format(query, country, startYear, endYear);

                System.out.println(query);

                ResultSet results = statement.executeQuery(query);

                while (results.next())
                {
                    Level2A_world_country_temperature countryTemp = new Level2A_world_country_temperature();

                    countryTemp.country = results.getString("Country");
                    countryTemp.year = results.getInt("Year");
                    countryTemp.average_temperature = results.getDouble("averageTemp");

                    if ("proportionalValue".equals(statisticView))  
                    {
                        double proportionalChange = results.getDouble("temp_proportional_change");
                        countryTemp.temperature_proportional_change = Math.round(proportionalChange * 100.0) / 100.0;
                    }
                    else if("rawValue".equals(statisticView)) 
                    {
                            countryTemp.temperature_raw_change = results.getDouble("temp_raw_change");
                    }
                    countryTemperature.add(countryTemp);
                }
                  statement.close();
      } catch (SQLException e) {
          System.err.println(e.getMessage());
      } finally {
          try {
              if (connection != null) {
                  connection.close();
              }
          } catch (SQLException e) {
              System.err.println(e.getMessage());
          }
      }

      return countryTemperature;
    }

// 3. GET COUNTRY POPULATION
    public ArrayList<Level2A_world_country_population> getWorldCountryPopulation(String country, String startYear, String endYear, String statisticView )
    {

        ArrayList<Level2A_world_country_population> countryPopulation = new ArrayList<Level2A_world_country_population>();

        Connection connection = null;

        try 
        {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = """
                    SELECT Year, CountryName, Population,
                    (
                        SELECT Population
                        FROM Populations
                        WHERE CountryName = "{0}" AND Year = {2}
                    ) - (
                        SELECT Population
                        FROM Populations
                        WHERE CountryName = "{0}" AND Year = {1}
                        ) AS POPRAWCHANGE,
                    (
                        SELECT Population
                        FROM Populations
                        WHERE CountryName = "{0}" AND Year = {2}
                    ) * 1.0 / (
                        SELECT Population
                        FROM Populations
                        WHERE CountryName = "{0}" AND Year = {1}
                    ) - 1 AS POPPROPCHANGE 
                     FROM Populations
                     WHERE CountryName = "{0}" AND (Year = {1} OR Year = {2});
                    """;

                    query = MessageFormat.format(query, country, startYear, endYear);
                System.out.println(query);

                ResultSet results = statement.executeQuery(query);

                while (results.next())
                {
                    Level2A_world_country_population countryPop = new Level2A_world_country_population();
                    countryPop.country_name = results.getString("CountryName");
                    countryPop.year = results.getInt("Year");
                    countryPop.population = results.getInt("Population");
                    countryPop.population_raw_change = results.getInt("POPRAWCHANGE");
                    countryPop.population_proportional_change = results.getDouble("POPPROPCHANGE");
                    
                    countryPopulation.add(countryPop);
                }
                statement.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e){
                System.err.println(e.getMessage());
            }
        }

        return countryPopulation;
    }

// 4. GET WORLD ALL DATA *** DONT TOUCH UNTIL WE FIND SOMETHING NOT RIGHT ***
    public ArrayList<Level2A_world_data> getWorldPopulationTemp(String startYear, String endYear, String sortFunction)
    {    
        ArrayList<Level2A_world_data> worldData = new ArrayList<Level2A_world_data>();

        Connection connection = null;

        try
        {
            connection = DriverManager.getConnection(DATABASE);

            String query = """
                    SELECT g.Year,
                 ROUND((SELECT AVG(AverageTemperature) 
                        FROM GlobalYearlyTemp
                        WHERE Year = g.Year), 2) AS worldTemperature,
                            (SELECT SUM(POPULATION)
                            FROM Populations
                            WHERE Year = g.Year) AS worldPopulation
                    FROM GlobalYearlyTemp g
                    WHERE g.Year BETWEEN ? AND ?
                    """;

                    if (sortFunction != null)
                        {
                            query +=" ORDER BY " + sortFunction;
                        }
                    else 
                        {
                            query +=" ORDER BY g.Year";
                        }
                    
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setQueryTimeout(30);
            
            preparedStatement.setInt(1, Integer.parseInt(startYear));
            preparedStatement.setInt(2, Integer.parseInt(endYear));

            System.out.println(query);

            ResultSet results = preparedStatement.executeQuery();

            while (results.next())
            {
                Level2A_world_data worldDataTemp = new Level2A_world_data();
                worldDataTemp.year = results.getInt("Year");
                worldDataTemp.world_temperature = results.getDouble("worldTemperature");
                worldDataTemp.world_population = results.getLong("worldPopulation");


                worldData.add(worldDataTemp);
            }
            preparedStatement.close();
        }
             catch (SQLException e) {
                System.err.println(e.getMessage());
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }

        return worldData;
    }

// 5. OUTPUT world result table
    public String outputWorldTable(String start_year, String end_year, String sorting) 
    {
        String html = "";

        Level2A_JDBC JDBC = new Level2A_JDBC();
        ArrayList<Level2A_world_data> worldData = JDBC.getWorldPopulationTemp(start_year, end_year, sorting);
    
    
            html = html + """     
                <table class='table'>
                <tr>
                    <th>Year</th>
                    <th>Average Temperature</th>
                    <th>Population</th>
                </tr>
                """;
            
            for (Level2A_world_data worldResult : worldData) 
            {
                html +="<tr> <td>" + worldResult.year + "</td>";              
                html += MessageFormat.format("""
                                                    <td> {0} </td>
                                                    <td> {1} </td>
                                                </tr>
                        """, worldResult.world_temperature, worldResult.world_population);
            };

            html +="</table>";

            return html;
        }

// 6. CREATE country result table
    public String outputCountryTableData(String country, String start_year, String end_year, Context context, String statisticView) {
    
    String html = "";

    Level2A_JDBC jdbc = new Level2A_JDBC();

    ArrayList<Level2A_world_country_population> countryPopulation = jdbc.getWorldCountryPopulation(country, start_year, end_year, statisticView);

    ArrayList<Level2A_world_country_temperature> countryTemperature = jdbc.getWorldCountryTemperature(country, start_year, end_year, statisticView);


    if ((countryPopulation == null || countryPopulation.size() < 2) && (countryTemperature == null || countryTemperature.size() < 2)) 
        {
    html += 
            " <div class = 'no_input_message'>There is no population or temperature data available in either <b>" +
            start_year + "</b> or <b>" + end_year + "</b> for country <b>" +
            country + "</b>. Please select other regions, time frames, and statistic views. </div>";
        } 
    else 
        {
        html += MessageFormat.format("""
            <table class='country_table'>
            <tr>
                <th>Country</th>
                <th>{0} Temperature</th>
                <th>{1} Temperature</th>
                <th>Temperature Change</th>
                <th>{0} Population</th>
                <th>{1} Population</th>
                <th>Population Change</th>
            </tr>
            """, start_year, end_year);

        html += "<tr><td>" + country + "</td>";

        if ("rawValue".equals(statisticView)) 
            {
                String rawTemp_Change = String.format("%.2f °C",countryTemperature.get(0).temperature_raw_change) ;
                String rawPop_Change = String.format("%,d",countryPopulation.get(0).population_raw_change);

                html+= "<h1 class='statistic-title-message '> Here is your result </h1>";
                // 1. Output country temperature part
                for (Level2A_world_country_temperature tempResult : countryTemperature)
                    {
                        html += "<td>" + String.format("%.2f°C", tempResult.average_temperature) + "</td>";
                    }
                if (countryTemperature.get(0).temperature_raw_change > 0)
                    {
                        rawTemp_Change = "+" + rawTemp_Change ;
                    }    
                html += "<td>" + rawTemp_Change + "</td>";

                // 2. Output country population part
                for (Level2A_world_country_population popResult : countryPopulation) 
                {
                    html += "<td>" + String.format("%,d", popResult.population) + "</td>";
                }
                if (countryPopulation.get(0).population_raw_change > 0)
                    {
                        rawPop_Change = "+" + rawPop_Change;
                    }
                html += "<td>" + rawPop_Change + "</td>";    
            }
        else  
            {
                String propTemp_Change = String.format("%.2f%%", countryTemperature.get(0).temperature_proportional_change);
                String propPop_Change = String.format("%.2f%%", countryPopulation.get(0).population_proportional_change);

                html+= "<h1 class = \"statistic-title-message\"> Here is your result </h1>";
                // 1. Output country temperature part
                for (Level2A_world_country_temperature tempResult : countryTemperature)
                    {
                        html += "<td>" + String.format("%.2f°C", tempResult.average_temperature) + "</td>";
                    }
                if (countryTemperature.get(0).temperature_proportional_change > 0)
                    {
                        propTemp_Change = "+" + propTemp_Change;
                    }
                html += "<td>" + propTemp_Change + "</td>";

                // 2. Output country population part
                for (Level2A_world_country_population popResult : countryPopulation) 
                    {
                        html += "<td>" + String.format("%,d", popResult.population) + "</td>";
                    }
                if (countryPopulation.get(0).population_proportional_change > 0)
                    {
                        propPop_Change = "+" + propPop_Change;
                    }
                html += "<td>" + propPop_Change + "</td>";
                
            }
        }
        html += "</tr></table>";
        

    return html;
}

}