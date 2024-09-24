package app.Level3A_code;

import java.util.ArrayList;
import java.util.List;

import app.Level1_code.JDBCConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Level3A_JDBC {

    public static final String DATABASE = "jdbc:sqlite:database/dataset_Climate.db";
    public Level3A_JDBC() {
        System.out.println("Created Level 3A JDBC Connection Object");
    }

    public String getOutputTool1(String startYearTool1, String startTool1, String locationTool1, String regionTool1) 
    {
      String html = "";
      int startYearcheck;
      int lengthcheck;
    
      try 
      {
        startYearcheck = Integer.parseInt(startYearTool1);
      } 
      catch (NumberFormatException e) 
      {
        return "<span class='error-message'>Invalid year format.</span>";
      }
    
      try 
      {
        lengthcheck = Integer.parseInt(startTool1);
      } 
      catch (NumberFormatException e) 
      {
        return "<span class='error-message'><b>Invalid length format.</b></span>";
      }
    
      String AverageTemperature = "AverageTemperature";

      if (regionTool1.equals("Country")) {
        AverageTemperature = "AverageTemperature";
      }
      int yearLength = 0;
    
      if (startTool1 != null && startYearTool1 != null) 
      {
        int periodCheck = Integer.parseInt(startTool1);
        int yearInt = Integer.parseInt(startYearTool1);
        yearLength = periodCheck + yearInt;
        if (yearLength > 2015) 
        {
          html = "Your period(Timespans) is not in our database";
          return html;
        }
    
      }
    
      try (Connection connection = DriverManager.getConnection(DATABASE);

        Statement statement = connection.createStatement()) {
        String sqlType = "GlobalYearlyLandTempBy" + regionTool1;
    
        String queryCheckLocation = "";
        boolean locationFound = false;
    
        if (regionTool1.equals("Country")) 
        {
          queryCheckLocation = "SELECT Country FROM GlobalYearlyLandTempByCountry";
        } 
        else if (regionTool1.equals("City")) 
        {
          queryCheckLocation = "SELECT City FROM GlobalYearlyLandTempByCity";
        } 
        else if (regionTool1.equals("State")) 
        {
          queryCheckLocation = "SELECT State FROM GlobalYearlyLandTempByState";
        } 
        else 
        {
          html = "Invalid region type.";
        }
    
        if (!queryCheckLocation.isEmpty()) 
        {
          // 1. PRINT QUERY CHECK LOCATION
          System.out.println("Query 1 executed \n" + queryCheckLocation);

          ResultSet locationCheck = statement.executeQuery(queryCheckLocation);
          while (locationCheck.next()) {
            String location = locationCheck.getString(regionTool1);
            if (locationTool1.toLowerCase().contains(location.toLowerCase())) 
            {
              locationFound = true;
              break; // Location found, no need to check further
            }
          }
          if (!locationFound) 
          {
            html = "That location may not be in that region type or we don't have that location in our database.";
          }
        }
    
        String query = "SELECT" + 
            "    ROUND(AVG(" + AverageTemperature + "), 3) AS avgtemp" + 
            " FROM " + sqlType  + 
            " WHERE " + 
            "    Year BETWEEN " + startYearTool1 + " AND (" + startYearTool1 + "+ " + startTool1 + ")" + 
            "    AND UPPER(" + regionTool1 + ") = UPPER('" + locationTool1 + "');";
        
        if (regionTool1.equals("Global")) {
          query =  "SELECT ROUND(AVG(AverageTemperature), 3) AS avgtemp" + 
              "FROM GlobalYearlyTemp" + 
              "WHERE" + 
              "    Year BETWEEN " + startYearTool1 + "AND " + startYearTool1 + " + " + startTool1 + ";";
        }
        
        // 2. PRINT QUERY TOOl 1
        System.out.println("Query tool 1 executed\n" + query);
        ResultSet resultSet = statement.executeQuery(query);
    
        while (resultSet.next()) {
          String tool1Output = resultSet.getString("avgtemp");
          html += "<span class='span1'> Average temperature of " + locationTool1 + " is: "  +   tool1Output + "°C </span>";
        }
    
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    
      return html;
    }
    

    public String getOutputTool2(ArrayList<String> startYearTool2, String lengthTool2, String locationTool2, String regionTool2) {
      String html = "";
      if (startYearTool2.size() == 0) {
        return html;
      }
    
      try {
        int lengthcheck = Integer.parseInt(lengthTool2);
      } catch (NumberFormatException e) {
        return "<span class='error-message'>Invalid length format.</span>";
      }
    
      if (startYearTool2.size() > 0) {
        if (lengthTool2 != null) {
          int lengthCheck = Integer.parseInt(lengthTool2);
          for (int i = 0; i < startYearTool2.size(); i++) {
            int check = 0;
            int year = Integer.parseInt(startYearTool2.get(i));
            check = year + lengthCheck;
            if (check > 2015) {
              html = "Your length input/year range is larger than what we have in our database";
            }
          }
        }
      }
    
      try (Connection connection = DriverManager.getConnection(DATABASE);
          Statement statement = connection.createStatement()) {
        String sqlType = "GlobalYearlyLandTempBy";
        sqlType += regionTool2;
        if (regionTool2.equals("Country")) 
        {
          String AverageTemperature = "AverageTemp";
        }
    
        String queryCheckLocation = "";
        boolean locationFound = false;
    
        if (regionTool2.equals("Country")) {
          queryCheckLocation = "SELECT Country FROM GlobalYearlyLandTempByCountry";
        } else if (regionTool2.equals("City")) {
          queryCheckLocation = "SELECT City FROM GlobalYearlyLandTempByCity";
        } else if (regionTool2.equals("State")) {
          queryCheckLocation = "SELECT State FROM GlobalYearlyLandTempByState";
        } else {
          html = "Invalid region type.";
        }
    
        if (!queryCheckLocation.isEmpty()) 
        {
          //PRINT QUERY 2
          System.out.println("Query 2 executed \n" + queryCheckLocation);
          ResultSet locationCheck = statement.executeQuery(queryCheckLocation);
          while (locationCheck.next()) 
          {
            String location = locationCheck.getString(regionTool2);
            if (locationTool2.toLowerCase().contains(location.toLowerCase())) 
            {
              locationFound = true;
              break; // Location found, no need to check further
            }
          }
          if (!locationFound) {
            html = "That location may not be in that region type or we don't have that location in our database.";
          }
        }
    
        String selectYear = "";
    
        for (int i = 0; i < startYearTool2.size(); i++) 
        {
          if (i < startYearTool2.size() - 1) 
          {
            selectYear = selectYear + "SELECT " + startYearTool2.get(i) + " AS start_year UNION ALL ";
          } else 
          {
            selectYear = selectYear + "SELECT " + startYearTool2.get(i) + " AS start_year";
          }
        }
    
        String query = "WITH Periods AS ( " + selectYear + ")," + " " + 
                      "PeriodAverage AS ( " + 
                      "    SELECT p.start_year, " +   
                      "           ROUND(AVG(t.AverageTemperature ),3) AS avg_temp " +   
                      "      FROM Periods p " +   
                      "           JOIN " +   
                      sqlType + " t ON t.Year BETWEEN p.start_year AND p.start_year + " + lengthTool2 + " " +   
                      "     WHERE UPPER(t." + regionTool2 + ") = UPPER('" + locationTool2 + "') " +   
                      "     GROUP BY p.start_year " +   
                      ") " +   
                      "SELECT * " +   
                      "  FROM PeriodAverage ORDER BY avg_temp ASC;";
    
        if (regionTool2.equals("Global")) 
        {
          query = "WITH Periods AS ( " +   
              selectYear +   
              "), " +   
              "PeriodAverage AS ( " +   
              "    SELECT p.start_year, " +   
              "           AVG(t.AverageTemperature) AS avg_temp " +   
              "      FROM Periods p " +   
              "           JOIN " +   
              "           GlobalYearlyTemp t ON t.Year BETWEEN p.start_year AND p.start_year + " + lengthTool2 + " " +   
              "     GROUP BY p.start_year " +   
              ") " +   
              "SELECT * " +   
              "  FROM PeriodAverage;";
        }

        //PRINT QUERY TOOL 2
        System.out.println("Query tool 2 executed\n" + query);

        ResultSet resultSet = statement.executeQuery(query);
        int i = 1;
        while (resultSet.next()) {
          int Year = resultSet.getInt("start_year");
          Double avgtemp = resultSet.getDouble("avg_temp");
          html += "<tr> " +
              "    <td>" + i + "</td> " +
              "    <td>" + Year + " </td> " +
              "    <td>" + avgtemp + " °C</td>  " +
              "</tr>";
          i++;
        }
    
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    
      return html;
    }
    
    public String getOutputTool3(ArrayList<String> caseLocation, ArrayList<String> caseYear, String regionTool3,
        String lengthTool3, String caseInput, String MinTemp, String MaxTemp, String Condition, String MinPop,
        String MaxPop) {
      String html = "";
      if (caseInput == null) {
        html = "You can't leave case Input type empty";
        return html; 
      }
    
    
      if (Condition == null) {
        Condition = "NONE";
      }
    
      if (caseLocation.size() < 0 || caseYear.size() < 0) {
        return "You need to input LOCATION and YEAR";
      }
    
      int lengthcheck;
      try {
        lengthcheck = Integer.parseInt(lengthTool3);
      } catch (NumberFormatException e) {
        return "<h3 class='error-message'> It seems that you are using our filter wrong please look at the instruction under.</h3>";
      }
    
      if (caseInput.equals("NONE") || regionTool3.equals("NONE") || lengthTool3.equals("NONE")) {
        return html; 
      }
    
      if (caseYear.size() == 0) {
        return html;
      }
      if (!caseYear.isEmpty() && lengthTool3 != null) {
        int lengthCheck = Integer.parseInt(lengthTool3);
        for (String yearString : caseYear) {
          int year = Integer.parseInt(yearString);
          if (year + lengthCheck > 2015) {
            html = "Your length input/year range is larger than what we have in our database";
            return html;
          }
        }
      }
    
      try (Connection connection = DriverManager.getConnection(DATABASE);
          Statement statement = connection.createStatement()) {
    
        String sqlType = "GlobalYearlyLandTempBy" + regionTool3;
        String AverageTemperature = "AverageTemperature";
        if (regionTool3.equals("Country")) {
          AverageTemperature = "AverageTemperature";
        }
    
        String queryCheckLocation = "";
        boolean locationFound = false;
    
        if (regionTool3.equals("Country")) {
          queryCheckLocation = "SELECT Country FROM GlobalYearlyLandTempByCountry";
        } else if (regionTool3.equals("City")) {
          queryCheckLocation = "SELECT City FROM GlobalYearlyLandTempByCity";
        } else if (regionTool3.equals("State")) {
          queryCheckLocation = "SELECT State FROM GlobalYearlyLandTempByState";
        } else {
          html = "Invalid region type.";
        }
    
        if (!queryCheckLocation.isEmpty()) 
        {
          // 5. PRINT QUERY
          System.out.println("Query 3 executed \n" + queryCheckLocation);
          ResultSet locationCheck = statement.executeQuery(queryCheckLocation);
          while (locationCheck.next()) {
            String location = locationCheck.getString(regionTool3);
            for (int index = 0; index < caseLocation.size(); index++) {
              if (caseLocation.get(index).toLowerCase().contains(location.toLowerCase())) {
                locationFound = true;
                break; // Location found, no need to check further
              }
            }
          }
          if (!locationFound) {
            html = "That location may not be in that region type or we don't have that location in our database.";
          }
        }
    
        String caseSelect = "";
        for (int index = 0; index < caseLocation.size(); index++) {
          caseSelect += "SELECT UPPER('" + caseLocation.get(index) + "') AS region, " + caseYear.get(index)
              + " AS start_year";
          if (index < caseLocation.size() - 1) {
            caseSelect += " UNION ALL ";
          }
        }
    
        String FilterCondition = "";
        if (Condition.equals("NONE")) {
          FilterCondition = "";
        }
    
        // Set up condtion(Where) if user only choose Temp range
        if (Condition.equals("Temp")) {
          if (MinTemp.equals("NONE") && MaxTemp.equals("NONE")) {
            FilterCondition = "";
          }
          if (MinTemp.equals("NONE")) {
            FilterCondition = "Where avg_temp < " + MaxTemp;
          }
          if (MaxTemp.equals("NONE")) {
            FilterCondition = "Where avg_temp > " + MinTemp;
          } else {
            FilterCondition = "Where avg_temp Between " + MinTemp + " And " + MaxTemp;
          }
        }
    
        // Set up condtion(Where) if user only choose Pop range
        if (regionTool3.equals("Country") && Condition.equals("Pop")) {
          if (MinPop.equals("NONE") && MaxPop.equals("NONE")) {
            FilterCondition = "";
          }
          if (MinPop.equals("NONE")) {
            FilterCondition = "Where avg_population < " + MaxPop;
          }
          if (MaxTemp.equals("NONE")) {
            FilterCondition = "Where avg_population > " + MinPop;
          } else {
            FilterCondition = "Where avg_population Between " + MinPop + " And " + MaxPop;
          }
        }
    
        // Set up condtion(Where) if user choose Pop range and Temp Range
        if (regionTool3.equals("Country") && Condition.equals("AND")) {
          if (MinPop.equals("NONE") && MaxPop.equals("NONE") && MinTemp.equals("NONE") && MaxTemp.equals("NONE")) {
            FilterCondition = "";
          }
          if (MinTemp.equals("NONE")) {
            FilterCondition = "Where avg_temp < " + MaxPop + " AND avg_population between " + MinPop + " And " + MaxPop;
          }
          if (MaxTemp.equals("NONE")) {
            FilterCondition = "Where avg_temp > " + MinPop + " AND avg_population between " + MinPop + " And " + MaxPop;
          }
          if (MinPop.equals("NONE")) {
            FilterCondition = "Where avg_temp Between " + MinTemp + " And " + MaxTemp + " AND avg_population < "
                + MaxPop;
          }
          if (MaxPop.equals("NONE")) {
            FilterCondition = "Where avg_temp Between " + MinTemp + " And " + MaxTemp + " AND avg_population > " + MinPop;
          } else {
            FilterCondition = "Where avg_temp Between " + MinTemp + " And " + MaxTemp + " AND avg_population Between "
                + MinPop + " And " + MaxPop;
          }
        }
        // Set up condtion(Where) if user choose condition: Pop range OR Temp Range
        if (regionTool3.equals("Country") && Condition.equals("OR")) {
          if (MinPop.equals("NONE") && MaxPop.equals("NONE") && MinTemp.equals("NONE") && MaxTemp.equals("NONE")) {
            FilterCondition = "";
          }
          if (MinTemp.equals("NONE")) {
            FilterCondition = "Where avg_temp < " + MaxPop + " OR (avg_population between " + MinPop + " And " + MaxPop
                + ")";
          }
          if (MaxTemp.equals("NONE")) {
            FilterCondition = "Where avg_temp > " + MinPop + " OR (avg_population between " + MinPop + " And " + MaxPop
                + ")";
          }
          if (MinPop.equals("NONE")) {
            FilterCondition = "Where (avg_temp Between " + MinTemp + " And " + MaxTemp + ") OR avg_population < "
                + MaxPop;
          }
          if (MaxPop.equals("NONE")) {
            FilterCondition = "Where (avg_temp Between " + MinTemp + " And " + MaxTemp + ") OR avg_population > "
                + MinPop;
          } else {
            FilterCondition = "Where (avg_temp Between " + MinTemp + " And " + MaxTemp + ") OR (avg_population Between "
                + MinPop + " And " + MaxPop + ")";
          }
        }
    
        String query = "";
        if (regionTool3.equals("Country")) 
        {
          query = "WITH Periods AS ( " + caseSelect +
              "), " +   
              "PeriodAverage AS ( " +   
              "    SELECT " +   
              "        p.region, " +   
              "        p.start_year, " +   
              "        ROUND(AVG(t." + AverageTemperature + "),3) AS avg_temp, " +   
              "        AVG(pop.Population) AS avg_population, " +   
              "        RANK() OVER (ORDER BY AVG(t." + AverageTemperature + ")) AS period_rank " +   
              "    FROM " +   
              "        Periods p " +   
              "    JOIN " +   
              "        " + sqlType + " t ON t.Year BETWEEN p.start_year AND p.start_year + " + lengthTool3 + " " +   
              "                                        AND UPPER(t.Country) = UPPER(p.region) " +   
              "    JOIN " +   
              "        Populations pop ON UPPER(pop.CountryName) = UPPER(p.region) AND pop.Year = p.start_year " +   
              "    GROUP BY " +   
              "        p.region, p.start_year " +   
              ") " +   
              "SELECT " +   
              "    region, " +   
              "    start_year, " +   
              "    avg_temp, " +   
              "    avg_population, " +   
              "    period_rank " +   
              "FROM " +   
              "    PeriodAverage  " + FilterCondition + ";";
        } else {
          query = "WITH Periods AS ( " + caseSelect +
              "), " +
              "PeriodAverage AS ( " +
              "    SELECT " +
              "        p.region, " +
              "        p.start_year, " +
              "        ROUND(AVG(t." + AverageTemperature + "),3) AS avg_temp, " +
              "        RANK() OVER (ORDER BY AVG(t." + AverageTemperature + ")) AS period_rank " +
              "    FROM " +
              "        Periods p " +
              "    JOIN " +
              "        " + sqlType + " t ON t.Year BETWEEN p.start_year AND (p.start_year + " + lengthTool3 + ") " +
              "        AND UPPER(t.Country) = p.region " +
              "    GROUP BY " +
              "        p.region, p.start_year " +
              ") " +
              "SELECT " +
              "    region, " +
              "    start_year, " +
              "    avg_temp, " +
              "    period_rank " +
              "FROM " +
              "    PeriodAverage  " + FilterCondition + ";";
        }
        // 6. PRINT QUERY TOOL 3
        System.out.println("Query 3 executed \n" + queryCheckLocation);
        ResultSet resultSet = statement.executeQuery(query);
        int i = 1;
        while (resultSet.next()) {
          String country = resultSet.getString("region");
          int year = resultSet.getInt("start_year");
          Double avgTemp = resultSet.getDouble("avg_temp");
          int population = 0;
          if (regionTool3.equals("Country")) {
            population = resultSet.getInt("avg_population");
          }
          html = html + "<tr> " +
              "    <td>" + i + "</td> " +
              "    <td>" + country + "</td> " +
              "    <td>" + year + " </td> " +
              "    <td>" + avgTemp + " °C</td>  " +
              "    <td>" + population + " </td>  " +
              "</tr>";
          i++;
        }
    
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
      return html;
    }

    
    public String tool2YearCheckBox_2_1() {
    String html = "";
    int interval = 50; // Each folder will cover 50 years
    List<Integer> years = new ArrayList<>(); // Store years from the database
 
    try (Connection connection = DriverManager.getConnection(JDBCConnection.DATABASE);
         Statement statement = connection.createStatement()) {
 
        // Fetch all distinct years from the GlobalYearlyTemp table
        String yearListQuery = "SELECT DISTINCT year FROM GlobalYearlyTemp ORDER BY year;";
        ResultSet yearListData = statement.executeQuery(yearListQuery);
 
        // Add the fetched years to the list
        while (yearListData.next()) {
            years.add(yearListData.getInt("year"));
        }
 
        // If no years are found, return an empty string
        if (years.isEmpty()) {
            return "<p>No years found in the database.</p>";
        }
 
        // Determine the range of years dynamically
        int startYear = years.get(0);  // Start with the first year from the database
        int endYear = years.get(years.size() - 1);  // End with the last year from the database
        int currentFolderStartYear = startYear;
 
        // Start the outer folder (All Years)
        html += "<div class='folder'>   " +
                "  <div class='folder-header' onclick='toggleFolder('all-years')'>All Years</div>   " +
                "  <div id='all-years' class='folder-content'>   ";
 
        // Loop through each 50-year range and create folders
        while (currentFolderStartYear <= endYear) {
            int currentFolderEndYear = Math.min(currentFolderStartYear + interval - 1, endYear);
 
            // Create a folder for the current 50-year range
            html += "<div class='folder'>   " +
                    "  <div class='folder-header' onclick='toggleFolder('years-" + currentFolderStartYear + "-" + currentFolderEndYear + "')'>" +
                    currentFolderStartYear + " - " + currentFolderEndYear + "</div>   " +
                    "  <div id='years-" + currentFolderStartYear + "-" + currentFolderEndYear + "' class='folder-content'>   ";
 
            // Add checkboxes for the years in the current folder's range
            for (int year : years) {
                if (year >= currentFolderStartYear && year <= currentFolderEndYear) {
                    html += " <div class='checkbox-item'>   " +
                            "    <input type='checkbox' id='year" + year + "' name='year" + year + "' value='" + year + "'>   " +
                            "    <label for='year" + year + "'>" + year + "</label>   " +
                            "</div>";
                }
            }
 
            // Close the current folder
            html += "  </div>   </div>   ";
            currentFolderStartYear += interval;
        }
 
        html += "  </div>   </div>   ";
 
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }
 
    return html;
}
  

}