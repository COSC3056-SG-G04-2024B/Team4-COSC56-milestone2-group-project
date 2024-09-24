package app.Level2A_code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.Level1_code.JDBCConnection;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Level2A_final implements Handler
{

    public static final String URL = "/Level2A";

    @Override
    public void handle (Context context) throws Exception
    {
        String html = "<html>";

        html += """
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Webpage Level 2A</title>
                <link rel = "stylesheet" href="Level2A_style.css" > 
            </head>
            
            <body>
                <div class="header">
                    <a href="/"><img class="logo" src="image.png" alt="logo" style="position:relative; left:60px;"></a>
                    <nav class="navbar"> 
                <nav class= "main_menu">
                        <ul></ul>
                            <li><a href="/">Home</a></li>
                            <li><a href="#">
                                Population and Temperature data
                                <img src="arrow-156117_1920.png" alt="" class="icon">
                                </a>
                                <ul class="sub_menu1">
                                    <li><a href="/Level2A">World/Country population and temperature</a></li>
                                    <li><a href="/Level2B">City/State temperature ranking</a></li>
                                </ul>
                            </li>
                            <li><a href="#">
                                Change and Comparing similarity
                                <img src="arrow-156117_1920.png" alt="" class="icon">
                                </a>
                                <ul class="sub_menu">
                                    <li><a href="Level3A">Change of temperature</a></li>
                                    <li><a href="Level3B">Comparing similarity tool</a></li>
                                </ul>
                            </li>
                        </ul>
                    </nav>
                </nav>
                    <div></div>
                        <a href="#"><img src="vietnameseIcon.png" height="75" ></a>
                        <a href="#"><img src="ausLanguageIcon.png" height="75"></a>
                </div>

            <div class="container">
                <div class = "center_content">
                    <div class = "center-background">
                        <div class="website_name">
                            <h1><strong> Country Population and Temperature Data</strong></h1>
                        </div>
                """;
                       
        // 2.WORLD COUNTRY OPTIONS IN FILTER
               Level2A_JDBC JDBC = new Level2A_JDBC();
               ArrayList<String> countryList = JDBC.getCountryList();   

        html += """
                        <section class="filters">
                            <form action="/Level2A" method="post">
                                <div class = "world_country">
                                    <select id = "country" name = "country" onchange="handleCountryChange()"> 
                                        <option value="" disabled selected> Select a Country </option>" 
                                        <option value="world"> World </option>
                """;         
                                    for (String country : countryList) 
                                    {
        html +=                        "<option value='" + country + "'>" + country + "</option>"; 
                                    }
        html += """
                                    </select> 
                                </div>
                """;
        html += """
                                    

                <div class="startYear">
                    <input type="text" id="startYear" name="startYear"  placeholder = "Start Year" min="1750" max="2012" onclick="document.querySelector('#startYear + .error-message').style.display='none';">
                </div>

                <div class="endYear">
                    <input type="text" id="endYear" name="endYear" placeholder = "End Year"  min="1751" max="2013" onclick="document.querySelector('#endYear + .error-message').style.display='none';">
                </div>

                <div class="statisticView" id="valueSelectContainer" style="display: none;">
                    <select id="statisticView" name="statisticView">
                        <option value="">Statistics view</option>
                        <option value="proportionalValue">Proportional value</option>
                        <option value="rawValue">Raw value</option>
                    </select>
                </div>
                
                <div class="sortFunction" id="sortSelectContainer" style="display: none;">
                    <select id="sortSelect" name="sortFunction">
                        <option value='sortBox'>Sort</option>
                        <option value='worldTemperature ASC'>Temperature Ascending</option>
                        <option value='worldTemperature DESC'>Temperature Descending</option>
                        <option value='worldPopulation ASC'>Population Ascending</option>
                        <option value='worldPopulation DESC'>Population Descending</option>
                    </select>
                </div>

                <script>
                    function handleCountryChange() {
                        const countrySelect = document.getElementById('country');
                        const sortSelectContainer = document.getElementById('sortSelectContainer');
                        const valueSelectContainer = document.getElementById('valueSelectContainer');

                        if (countrySelect.value == 'world') {
                            sortSelectContainer.style.display = 'block';
                            valueSelectContainer.style.display = 'none';
                        } else if (countrySelect.value) {
                            sortSelectContainer.style.display = 'none';
                            valueSelectContainer.style.display = 'block';
                        } else {
                            sortSelectContainer.style.display = 'none';
                            valueSelectContainer.style.display = 'none';
                        }
                    }
                </script>

                            <button class="filter_icon">
                                <img src="magnify.png" alt="filter" width="35" height="30">
                            </button>
                        </form>
                    </section>
                </div>
                    <section class="data-table">
                """;

    // 3. GET INPUT
        String country = context.formParam("country");
        String startYear = context.formParam("startYear");
        String endYear = context.formParam("endYear");
        String statisticView = context.formParam("statisticView");
        String sortFunction = context.formParam("sortFunction");

    if (country == null)
    {
        html += """
                <h1> <strong> Hello our user please choose every option in our filter bar and click the "magnify" icon. Then we will show your wished data as soon as right here </strong><h1>
                """;
    }
    else if (country.equals("world"))
            {
                if ("sortBox".equals(sortFunction))
                {
                    html += """
                        <table class= 'data-table'>
                            <tr>
                                <th>
                                    <h1> <strong> No Information Found </strong><h1>
                                </th>
                            </tr>
                            <tbody>
                                <td>
                                    It seems you are using our filter in World mode not right, please select again!
                                </td>
                            </tbody>
                        </table>
                            """;
                }
                else 
                {
                    html += JDBC.outputWorldTable(startYear, endYear, sortFunction);
                }
            }
            else 
            {
                if (sortFunction.equals(sortFunction))
                {
                    html += JDBC.outputCountryTableData(country, startYear, endYear, context, statisticView);
                }
                else 
                {
                    html += """
                    <table class= 'data-table'>
                        <tr>
                            <th>
                                <h1> <strong> No Information Found </strong><h1>
                            </th>
                        </tr>
                        <tbody>
                            <td>
                                It seems you are using our filter not right in Country mode, please select again!
                            </td>
                        </tbody>
                    </table>
                """;
                }
            }

        html += """
                    </section>
                </div>
            </div>
                """;
                    
        html += """ 
            <div class="site_map_footer">
                <div class = "site_map_footer_blank"></div>
                <div class="memberInfo">
                    <ul>
                        <div class ="titleName"> Development team members </div>
                """;
        
        try  
        {
            Connection connection = DriverManager.getConnection(JDBCConnection.DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet results = statement.executeQuery("SELECT studentID, memberName FROM Member");
            while (results.next()) 
            {
                String studentID = results.getString("studentID");
                String memberName = results.getString("memberName");

            html += "           <li>" +
                           "<span>" + memberName + "</span>" +
                           "<p>" + studentID + "</p>" +
                       "</li>";
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        html += """
                    </ul>
                </div>
            </div>
        </body>
        </html>
                """;

    context.html(html);
                
}
        
}
