package app.Level2B_code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;

import app.Level1_code.JDBCConnection;

public class Level2B_JDBC {

    public static final String DATABASE = "jdbc:sqlite:database/dataset_Climate.db";

    public  Level2B_JDBC() {
        System.out.println("Created Level 2B JDBC Connection Object");
    }

  public String outputResultTable2B(String country, String type, String startYear, String endYear, String statistic, String ValueType) 
      {
      String html = "";

      try (Connection connection = DriverManager.getConnection(JDBCConnection.DATABASE);
          Statement statement = connection.createStatement()) 
          {
        String tableRegionBy = "GlobalYearlyLandTempBy";
        tableRegionBy += type;

        String query = """
                      SELECT {0} AS Location, 
                          MAX(CASE WHEN Year = {1} THEN {2} END) AS StartTemp, 
                          MAX(CASE WHEN Year = {3} THEN {2} END) AS EndTemp, 
                          ROUND((
                              COALESCE(MAX(CASE WHEN Year = {3} THEN {2} END), 0) - 
                              COALESCE(MAX(CASE WHEN Year = {1} THEN {2} END), 0)
                          ) / COALESCE(MAX(CASE WHEN Year = {1} THEN {2} END), 1) * 100, 2) AS TemperatureChange 
                      FROM {4} 
                      WHERE Country = "{5}" 
                      AND (Year = {1} OR Year = {3}) 
                      GROUP BY {0} 
                      HAVING StartTemp IS NOT NULL 
                      AND EndTemp IS NOT NULL 
                      AND (EndTemp - StartTemp) <> 0 
                      ORDER BY ABS(TemperatureChange) DESC;
                      """;
        query = MessageFormat.format(query, type, startYear, statistic, endYear, tableRegionBy, country);
        System.out.println("Query 1 executed: \n" + query);
        ResultSet resultSet = statement.executeQuery(query);

        String queryRawValue = "SELECT " +
            type + " AS Location, " +
            "StartTemp, " +
            "EndTemp, " +
            "ROUND(TemperatureChange, 3) AS TemperatureChange " +
            "FROM ( " +
            "    SELECT " +
            "        Start." + type + ", " +
            "        Start." + statistic + " AS StartTemp, " +
            "        End." + statistic + " AS EndTemp, " +
            "        COALESCE(End." + statistic + ", 0) - COALESCE(Start." + statistic + ", 0) AS TemperatureChange " +
            "    FROM " +
            "        (SELECT DISTINCT " + type + ", " + statistic + " FROM " + tableRegionBy + " WHERE Country = '" + country
            + "' AND Year = " + startYear + " AND " + statistic + " IS NOT NULL AND " + statistic + " <> '') AS Start " +
            "    INNER JOIN " +
            "        (SELECT DISTINCT " + type + ", " + statistic + " FROM " + tableRegionBy + " WHERE Country = '" + country
            + "' AND Year = " + endYear + " AND " + statistic + " IS NOT NULL AND " + statistic + " <> '') AS End " +
            "    ON Start." + type + " = End." + type + " " +
            ") AS SubQuery " +
            "WHERE " +
            "    TemperatureChange IS NOT NULL AND (TemperatureChange != 0) AND (StartTemp IS NOT NULL OR EndTemp IS NOT NULL) "
            +
            "ORDER BY " +
            "    ABS(TemperatureChange) DESC;";

        System.out.println("Query 2 executed: \n" + query);
        ResultSet resultSetRawValue = statement.executeQuery(queryRawValue);

        int i = 1;
        if ("ProportionValue".equals(ValueType)) 
        {
          while (resultSet.next()) 
          {
            String location = resultSet.getString("Location");
            Double startTemp = resultSet.getDouble("StartTemp");
            Double endTemp = resultSet.getDouble("EndTemp");
            Double changeRate = resultSet.getDouble("TemperatureChange");

            html += "<tr>" +
                "    <td>" + i + "</td>" +
                "    <td>" + location + "</td>" +
                "    <td>" + startTemp + " °C</td>" +
                "    <td>" + endTemp + " °C</td>  " +
                "    <td>" + changeRate + " %</td> " +
                "</tr>";
            i++;
          }
        } 
        else 
        {
          while (resultSetRawValue.next()) 
          {
            String location = resultSetRawValue.getString("Location");
            Double startTemp = resultSetRawValue.getDouble("StartTemp");
            Double endTemp = resultSetRawValue.getDouble("EndTemp");
            Double changeRate = resultSetRawValue.getDouble("TemperatureChange");

            html += "<tr> " +
                "    <td>" + i + "</td> " +
                "    <td>" + location + "</td> " +
                "    <td>" + startTemp + " °C</td> " +
                "    <td>" + endTemp + " °C</td>  " +
                "    <td>" + changeRate + " °C</td> " +
                "</tr>";
            i++;
          }
        }

      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }

      return html;
    }


  public String getCityState() 
  {
    String html = "";

    ArrayList<String> cityStateFilter = new ArrayList<>();

    try (Connection connection = DriverManager.getConnection(JDBCConnection.DATABASE);
        Statement statement = connection.createStatement()) 
    {

      String query = "SELECT DISTINCT Country FROM GlobalYearLyLandTempByState ORDER BY Country ASC";

      String queryCountryList = "SELECT Country FROM GlobalYearLyLandTempByState " +
                                "UNION " +
                                "SELECT Country FROM GlobalYearLyLandTempByCity ORDER BY Country ASC";

      ResultSet cityStateList = statement.executeQuery(query);

      while (cityStateList.next()) 
      {
        String countryFilter = cityStateList.getString("Country");
        cityStateFilter.add(countryFilter);
      }

      ResultSet dropDownList = statement.executeQuery(queryCountryList);
      while (dropDownList.next()) 
      {
        String countryList = dropDownList.getString("Country");
        boolean hasState = cityStateFilter.contains(countryList);

        if (hasState) 
        {
          html += "<option value='" + countryList + "'>" + countryList + "</option>";
        } 
        else 
        {
          html += "<option value='" + countryList + "'>" + countryList + " (No State) </option>";
        }
      }

    } 
    catch (SQLException e) 
    {
      System.err.println(e.getMessage());
    }

    return html;
  }

  public String cityStateCheck(String country, String type) 
  {
    String html = "";

    ArrayList<String> countryFilterList = new ArrayList<>();

    try (Connection connection = DriverManager.getConnection(JDBCConnection.DATABASE);
        Statement statement = connection.createStatement()) 
        {

      String query = "SELECT DISTINCT Country FROM GlobalYearLyLandTempByState ORDER BY Country ASC";
      ResultSet stateList = statement.executeQuery(query);

      while (stateList.next()) 
      {
        String countryFilter = stateList.getString("Country");
        countryFilterList.add(countryFilter);
      }


    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }

    return html;
  }

}
