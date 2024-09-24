package app.Level2B_code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Level2B_final implements Handler {

    public static final String URL = "/Level2B";

    @Override
    public void handle(Context context) throws Exception 
    {
        String html="<html>";
        html += """
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Webpage Level 2B </title>
                <link rel = "stylesheet" href="Level2B_style.css" > 
            </head>
            
            <body>
                <div class="header">
                    <img class="logo" src="image.png" alt="logo" style="position:relative; left:60px;">
                    <nav class="navbar"> 
                        <nav class= "main_menu">
                                <li>
                                    <a href="/">HOME</a>
                                </li>
                                
                                <li>
                                    <a href="#">
                                        Population and Temperature data
                                        <img src="arrow-156117_1920.png" alt="" class="icon">
                                    </a>
                                    <ul class="sub_menu1">
                                        <li><a href="Level2A">World/Country population and temperature</a></li>
                                        <li><a href="Level2B">City/State temperature ranking</a></li>
                                    </ul>
                                </li>

                                <li>
                                    <a href="#">
                                        Change and Comparing similarity
                                        <img src="arrow-156117_1920.png" alt="" class="icon">
                                    </a>
                                    <ul class="sub_menu">
                                        <li><a href="Level3A">Change of temperature</a></li>
                                        <li><a href="Level3B">Comparing similarity tool</a></li>
                                    
                                </li>
                            </ul>
                        </nav>
                    </nav>
                    <div></div>
                        <a href="#"><img src="vietnameseIcon.png" width="50" height="75" ></a>
                        <a href="#"><img src="ausLanguageIcon.png" width="50" height="75"></a>
                </div>
        
                <div class="container">
                    <div class = "center_content">
                        <div class = "center-background">
                            <div class="website_name">
                                <h1><strong>City or State Temperature Ranking</strong></h1>
                            </div>

                            <section class="filters">
                                <form id="parentForm" action="/Level2B" method="post">
                                    <div>
                                        <select class="region" name="country">
                                            <option disabled selected>Choose country</option>
        """;

        Level2B_JDBC JDBCConnection = new Level2B_JDBC();
        html += JDBCConnection.getCityState(); 
        
        html += """
                                        </select>
                                    </div>
                                    <div>
                                        <select id="city_state" name="CityState" onclick="document.querySelector('#CityState + .error-message').style.display='none';">
                                            <option value="" disabled selected>Choose city/state</option>
                                            <option value="City">City</option>
                                            <option value="State">State</option>
                                        </select>
                                    </div>
                """;
        String country = context.formParam("country");
        if (country == null) 
            country = "Vietnam";
        String Type = context.formParam("CityState");
        if (Type == null) 
            Type = "City";
        String StartYear = context.formParam("startYear");
        if (StartYear == null) 
            StartYear = "1990"; 
        String EndYear = context.formParam("endYear");
        if (EndYear == null) 
            EndYear = "2000";
        String statistic = context.formParam("statistic");
        if (statistic == null) 
            statistic = "AverageTemperature";
        String ValueType = context.formParam("ValueType");
        if (ValueType == null) 
            ValueType = "RawValue";

        html += JDBCConnection.cityStateCheck(country, Type);

        html += """
                                    <div>
                                        <input type="number" id="startYear" name="startYear" placeholder="Start Year" min="1750" max="2012" onclick="document.querySelector('#startYear + .error-message').style.display='none';">
                                    </div>
                
                                    <div>
                                        <input type="number" id="endYear" name="endYear" placeholder="End Year" min="1751" max="2013" onclick="document.querySelector('#endYear + .error-message').style.display='none';">
                                    </div>
                                    <div>
                                        <select class="sorting" name="statistic">
                                            <option disabled selected>Statistic option</option>
                                            <option value="AverageTemperature">Average Temperature</option>
                                            <option value="MinimumTemperature">Minimum Temperature</option>
                                            <option value="MaximumTemperature">Maximum Temperature</option>
                                        </select>
                                    </div>
                                    <div>
                                        <select class="stat_view" name="ValueType">
                                            <option disabled selected>Choose data type</option>
                                            <option value="RawValue">Raw Value/Number</option>
                                            <option value="ProportionValue">Proportion Value/Percentage</option>
                                        </select>
                                    </div>

                                    <button class="filter_icon" type="submit"> 
                                        <img src="filter-44.png" alt="filter" width="35" height="30">
                                    </button>
                                </form>
                            </section>
                        </div>
                        <section class="data-table">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Ranking</th>
                                        <th>City / State</th>
                                        <th>Start temperature</th>
                                        <th>End temperature</th>
                                        <th class="temp_change">Temperature change</th>
                                    </tr>
                                </thead>
                                <tbody>
        """;

        html += JDBCConnection.outputResultTable2B(country, Type, StartYear, EndYear, statistic, ValueType);
       
        html += """
                                </tbody>
                            </table>
                        </section>
                    </div>
                </div> 
                """;
                        
        html+=""" 
                <div class="site_map_footer">
                 <div class="memberInfo">
                <ul>
                <div class ="titleName"> Development team members </div>
            """;
        
        try  {
            Connection connection = DriverManager.getConnection(Level2B_JDBC.DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet results = statement.executeQuery("SELECT studentID, memberName FROM Member");
            while (results.next()) {
                String studentID = results.getString("studentID");
                String memberName = results.getString("memberName");
                html += "           <li>" +
                                "<span>" + memberName + "</span>" +
                                "<p>" + studentID + "</p>" +
                            "</li>";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        html+="""
                        </ul>
                    </div>
                 </div>
            </body>
        </html>
        """;
        
        context.html(html);
    }
}
