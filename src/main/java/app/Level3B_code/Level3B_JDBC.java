package app.Level3B_code;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
public class Level3B_JDBC {

    private static final String DATABASE = "jdbc:sqlite:database/dataset_Climate.db";

    // GET region options 
    public List<String> getRegionOptions(String region) throws SQLException 
    {
        List<String> options = new ArrayList<>();
        String query = getRegionQuery(region);

        if (query == null) 
        {
            return options;
        }

        System.out.println("Query getRegionOption executed: \n" + query);

        try (Connection connection = DriverManager.getConnection(DATABASE);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) 
            {
                options.add(resultSet.getString(1));
            }
        }

        return options;
    }

    //GET region option 
    private String getRegionQuery(String region) 
    {
        if (region.equals("country"))
        {
            return "SELECT DISTINCT Country FROM GlobalYearlyLandTempByCountry ORDER BY Country ASC";
        }
        else if (region.equals("state"))
            return "SELECT DISTINCT State FROM GlobalYearlyLandTempByState ORDER BY State ASC";
        else if (region.equals("city"))
        {
            return "SELECT DISTINCT City FROM GlobalYearlyLandTempByCity ORDER BY City ASC";
        }
        else return null;
    }

    // GET the temperature query based on the region type *** DONE ***
    private String getTemperatureQuery(String region, String regionOption) 
    {
        String query = "";

        if ("country".equals(region))
        {
            query = """
                    SELECT tempCountry.Country, tempCountry.Start_Year, tempCountry.average_Temperature, 
                        ABS(tempCountry.average_Temperature - (
                            SELECT AVG(AverageTemperature)
                            FROM GlobalYearlyLandTempByCountry 
                    """;

            query += "WHERE Country = '" + regionOption + "'"; 
            query += """
                            AND Year BETWEEN ? AND ?
                        )) AS TemperatureDifference
                    FROM (
                        SELECT Country, MIN(Year) AS "Start_Year", ROUND(AVG(AverageTemperature), 4) AS "Average_Temperature"
                        FROM GlobalYearlyLandTempByCountry
                        """;
                                
            query += "WHERE Country != '" + regionOption + "'";
            query += """
                        GROUP BY Country, (Year / ?) 
                    ) tempCountry
                    ORDER BY TemperatureDifference
                    LIMIT ?; 
                    """;
        }
        else if ("city".equals(region))
        {
            query = """
                    SELECT tempCity.City, tempCity.Start_Year, tempCity.average_Temperature, 
                        ABS(tempCity.average_Temperature - (
                            SELECT AVG(AverageTemperature)
                            FROM GlobalYearlyLandTempByCity 
                    """;

            query += "WHERE City = '" + regionOption + "'"; 
            query += """
                            AND Year BETWEEN ? AND ?
                        )) AS TemperatureDifference
                    FROM (
                        SELECT City, MIN(Year) AS "Start_Year", ROUND(AVG(AverageTemperature), 4) AS "Average_Temperature"
                        FROM GlobalYearlyLandTempByCity
                        """;
                                
            query += "WHERE City != '" + regionOption + "'";
            query += """
                        GROUP BY City, (Year / ?) 
                    ) tempCity
                    ORDER BY TemperatureDifference
                    LIMIT ?; 
                    """;
        }
        else if ("state".equals(region))
        {
            query = """
                    SELECT tempState.State, tempState.Start_Year, tempState.average_Temperature, 
                        ABS(tempState.average_Temperature - (
                            SELECT AVG(AverageTemperature)
                            FROM GlobalYearlyLandTempByState 
                    """;

            query += "WHERE State = '" + regionOption + "'"; 
            query += """
                            AND Year BETWEEN ? AND ?
                        )) AS TemperatureDifference
                    FROM (
                        SELECT State, MIN(Year) AS "Start_Year", ROUND(AVG(AverageTemperature), 4) AS "Average_Temperature"
                        FROM GlobalYearlyLandTempByState
                        """;
                                
            query += "WHERE State != '" + regionOption + "'";
            query += """
                        GROUP BY State, (Year / ?) 
                    ) tempState
                    ORDER BY TemperatureDifference
                    LIMIT ?; 
                    """;
        }
        else query = null;

        return query;
    }
    
    // SETUP and GET HTML in case "Only Average Temperature" *** DONE ***
    public String getAverageTemperatureResultTable(String region, int startYear, int endYear, String regionOption, int length, int numPeriodInt) throws SQLException 
    {
        String query = getTemperatureQuery(region, regionOption);
        String html = "";
        try (Connection connection = DriverManager.getConnection(DATABASE);
             PreparedStatement statement = connection.prepareStatement(query)) 
        {
            // Parse value to "?" in SQL query
            statement.setInt(1, startYear);
            statement.setInt(2, endYear);
            statement.setInt(3, length);
            statement.setInt(4, numPeriodInt);

            // Print the query statement
            System.out.println("Query 2 executed: \n" + query);

            // RUN query to get table data in SQL and While loop to ADD data to result table
            try (ResultSet resultSet = statement.executeQuery()) 
            {
                int i = 1;
                while (resultSet.next()) {
                    
                    String regionOut = resultSet.getString(region);
                    Integer yearOut = resultSet.getInt("Start_Year");
                    Double averageTempOut = resultSet.getDouble("Average_Temperature");

                    html += "<tr>" +
                        "   <td>" + i + "</td>" +
                        "   <td>" + regionOut + "</td>" +
                        "   <td>" + yearOut + "</td>" +
                        "   <td>" + averageTempOut + "</td>" +
                        "</tr>";
                    i++;
                }
            }
        }

        return html;
    }

    // GET the population query in condition region == Country *** DONE ***
    private String getCountryPopulationQuery(String regionOption) 
    {
        String query ="";
        query = """
                SELECT sp.CountryName, sp.StartYear, sp.AvgPopulation, 
                    ABS(sp.AvgPopulation - (
                        SELECT AVG(Population) 
                        FROM Populations
                """;
        query += "WHERE CountryName = '" + regionOption + "'";
        query += """
                        AND Year BETWEEN ? AND ? 
                    )) AS PopulationDifference
                FROM (
                    SELECT CountryName, MIN(Year) AS StartYear, AVG(Population) AS AvgPopulation
                    FROM Populations
                """;
        query += "WHERE CountryName != '" + regionOption + "'";
        query += """
                    GROUP BY CountryName, (Year / ?) 
                ) AS sp
                ORDER BY PopulationDifference
                LIMIT ? 
                """;
        return query;
    }
    
    // SETUP and GET HTML in case "Only Population" *** DONE ***
    public String getAveragePopulationResultTable(int startYear, int endYear, String regionOption, int length, int numPeriodInt) throws SQLException 
    {
        String query = getCountryPopulationQuery(regionOption);
        String html = "";
        try (Connection connection = DriverManager.getConnection(DATABASE);
             PreparedStatement statement = connection.prepareStatement(query)) 
        {
            // Parse value to "?" in SQL query
            statement.setInt(1, startYear);
            statement.setInt(2, endYear);
            statement.setInt(3, length);
            statement.setInt(4, numPeriodInt);

            // Print the query statement
            System.out.println("Query getCountryPopulation executed: \n" + query);

            // RUN query to get table data in SQL and While loop to ADD data to result table
            try (ResultSet resultSet = statement.executeQuery()) 
            {
                int i = 1;
                while (resultSet.next()) {
                    
                    DecimalFormat df = new DecimalFormat("#.#####");
                    String countryNameOut = resultSet.getString("CountryName");
                    Integer yearOut = resultSet.getInt("StartYear");
                    Double averagePopulationOut = resultSet.getDouble("AvgPopulation");
                    String averagePopulationOutString = df.format(averagePopulationOut);

                    html += "<tr>" +
                        "   <td>" + i + "</td>" +
                        "   <td>" + countryNameOut + "</td>" +
                        "   <td>" + yearOut + "</td>" +
                        "   <td>" + averagePopulationOutString + "</td>" +
                        "</tr>";
                    i++;
                }
            }
        }

        return html;
    }

    // GET average temperature + population query in condition region == Country *** DONE ***
    private String getAverageTemperaturePopulationQuery(String regionOption)
    {
        String query = "";
        query += """
                SELECT gt.Country, gt.StartYear, gt.AvgTemperature, pop.AvgPopulation,
                    (ABS(gt.AvgTemperature - (
                        SELECT AVG(AverageTemperature)
                        FROM GlobalYearlyLandTempByCountry
                """;
        query += "WHERE Country = '" + regionOption + "'";
        query += """
                        AND Year BETWEEN ? AND ?
                    )) + ABS(pop.AvgPopulation - (
                        SELECT AVG(Population)
                        FROM Populations
                """;
        query += "WHERE CountryName = '" + regionOption + "'";
        query += """
                        AND Year BETWEEN ? AND ?
                    ))) / 2 AS SimilarityScore
                FROM (
                    SELECT Country, MIN(Year) AS StartYear, AVG(AverageTemperature) AS AvgTemperature
                    FROM GlobalYearlyLandTempByCountry
                """;
        query += "WHERE Country != '" + regionOption + "'";
        query += """
                    GROUP BY Country, (Year / ?) 
                ) gt
                JOIN (
                    SELECT CountryName AS Country, MIN(Year) AS StartYear, AVG(Population) AS AvgPopulation
                    FROM Populations
                """;
        query += "WHERE CountryName != '" + regionOption + "'";
        query += """
                    GROUP BY CountryName, (Year / ?) 
                ) pop ON gt.Country = pop.Country AND gt.StartYear = pop.StartYear
                ORDER BY SimilarityScore
                LIMIT ?
        """;


        return query;
    }   

    // SETUP and GET HTML in case "Both AverageTemperature and Population" *** DONE ***
    public String getAverageTemperaturePopulationResultTable(int startYear, int endYear, String regionOption, int length, int numPeriodInt) throws SQLException
    {
        String query = getAverageTemperaturePopulationQuery(regionOption);
        String html = "";
        try (Connection connection = DriverManager.getConnection(DATABASE);
             PreparedStatement statement = connection.prepareStatement(query))
        {
            // Parse value to "?" in SQL query
            statement.setInt(1, startYear);
            statement.setInt(2, endYear);
            statement.setInt(3, startYear);
            statement.setInt(4, endYear);
            statement.setInt(5, length);
            statement.setInt(6, length);
            statement.setInt(7, numPeriodInt);

            // PRINT the query statement
            System.out.println("Query getAverageTemperaturePopulationQuery executed: \n" + query);

            // RUN
            try (ResultSet resultSet = statement.executeQuery())
            {
                int i = 1;
                while (resultSet.next())
                {
                    DecimalFormat df = new DecimalFormat("#.#####");
                    String countryNameOut = resultSet.getString("Country");
                    Integer yearOut = resultSet.getInt("StartYear");
                    Double averageTemperatureOut = resultSet.getDouble("AvgTemperature");
                    Double averagePopulationOut = resultSet.getDouble("AvgPopulation");
                    String averagePopulationOutString = df.format(averagePopulationOut);
                    String averageTemperatureOutString = df.format(averageTemperatureOut);

                    html += "<tr>\r\n" +
                        "   <td>" + i + "</td>\r\n" +
                        "   <td>" + countryNameOut + "</td>\r\n" +
                        "   <td>" + yearOut + "</td>\r\n" +
                        "   <td>" + averageTemperatureOutString + "</td>\r\n" +
                        "   <td>" + averagePopulationOutString + "</td>\r\n" +
                        "</tr>";
                    i++;
                }
            }

        }
        return html;
    }


}
