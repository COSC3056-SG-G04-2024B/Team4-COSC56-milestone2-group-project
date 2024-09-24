package app.Level3B_code;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import app.Level3A_code.Level3A_JDBC;

public class Level3B_final implements Handler 
{

    public static final String URL = "/Level3B";

    public Level3B_JDBC JDBC3B = new Level3B_JDBC();

    @Override
    public void handle(Context context) throws Exception 
    {
        if (context.method().equals("POST")) 
        {
            String region = context.formParam("region");
            String startYear = context.formParam("start-year");

            if (region != null && (startYear == null || startYear.isEmpty())) 
            {
                // Get and display region selecting based on Region
                List<String> options = JDBC3B.getRegionOptions(region);
                renderForm(context, "", options, region);
            } 
            else 
            {
                handleFormSubmission(context);
            }
        } 
        else 
        {
            renderForm(context, "", null, null);
        }
    }

    public void renderForm(Context context, String resultHtml, List<String> options, String selectedRegion) 
    {
        String html = "<html>";

        html += """
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Webpage Level 3B</title> 
                    <link rel="stylesheet" href="./Level3B_style.css">
                </head>
                <body>
                                <header>
                        <a href="/"><img src="image.png" alt="Logo" class="logo"></a>
                        <nav class="navbar"> 
                        <nav class= "main_menu">
                            <ul></ul>
                                <li><a href="/">HOME</a></li>    
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
                        <div class="lang">
                            <a href="https://www.youtube.com/watch?v=dQw4w9WgXcQ" target= "_blank"><img src="vietnameseIcon.png" height="75"></a>
                            <a href="https://www.youtube.com/watch?v=dQw4w9WgXcQ" target="_blank" ><img src="ausLanguageIcon.png"  height="75"></a>
                        </div>
                    </header>
                    <div class='image'>
                        <h1><strong>Comparing similarity tool</strong></h1>
                    </div>
                    <div class='content'>
                        <form class="main_filter" action='/Level3B' method='post'>
                            <label for='region'; style = 'font-size: 16px;'><strong>Please choose your desired geographic region:</strong></label>
                            <select id='region' name='region' required>
                                <option value='' disabled selected>Select a region</option>
                """;
            
        html +=
            "                   <option value='country'" + ("country".equals(selectedRegion) ? " selected" : "") + ">Country</option>" +
            "                   <option value='state'" + ("state".equals(selectedRegion) ? " selected" : "") + ">State</option>" +
            "                   <option value='city'" + ("city".equals(selectedRegion) ? " selected" : "") + ">City</option>" +
            "               </select>" +
            "               <button type='submit'> Confirm </button>" +
            "           </form>";


        if (selectedRegion != null && !selectedRegion.isEmpty() && options != null && !options.isEmpty()) 
        {
                html += """
                        <form class="main_filter1" action='/Level3B' method='post'>
                            <label for='start-year'>Start Year:</label>
                            <input type='number' id='start-year' name='start-year' min='1750' max='2013' required>
                            <label for='period-length'>Period Length (in years):</label>
                            <input type='number' id='period-length' name='period-length' min='1' required>
                        """;

                html += "   <input type='hidden' name='region' value='" + selectedRegion + "'>";

                html += """
                            <label for='region-options'>Select Option:</label>
                            <select id='region-options' name='region-options' required>
                        """;

                for (String option : options) 
                {
                html += "       <option value='" + option + "'>" + option + "</option>";
                }
                html += "   </select>" +
                        "   <label for='similarity-type'>Similarity Type:</label>" +
                        "   <select id='similarity-type' name='similarity-type' required>";

                if ("country".equals(selectedRegion)) 
                {
                html += "   <option value='temperature'>Average Temperature</option>" +
                        "    <option value='population'>Population </option>" +
                        "    <option value='both'>Temperature and Population </option>";
                } 
                else 
                {
                    html += "   <option value='temperature'>Average Temperature</option>";
                }

                html += "   </select>" +
                        "   <label for='num-periods'>Number of Similar Periods:</label>" +
                        "   <input type='number' id='num-periods' name='num-periods' min='1' required>" +
                        "   <button type='submit'>Find Similar Periods</button>" +
                        "</form>";
        }

        if (resultHtml != null && !resultHtml.isEmpty()) 
        {
            html += """
                        <section class='results-section'>
                            <h2>Top similarity periods with the same region level: </h2>
                            <table>
                                <thead>
                                    <tr>
                                        <th> Rank </th>
                    """ ;
            
            html += "<th>" + selectedRegion + "</th>";

            html += """ 
                                        <th> Start Year </th>
                    """ ;

            html += resultHtml;

            html += "           </tbody>" +
                    "       </table>" +
                    "   </section>";
        }
        else 
        {
            html += "<h1 class = 'intro-message'> Hi please input anything you want to find, we will deliver our result to you right here</h1>";
        }

        html += """
            </div>
            <div class="site_map_footer">
                <div class = "site_map_footer_blank"></div>
                <div class="memberInfo">
                    <ul>
                        <div class ="titleName"> Development team members </div>
                """;
       
        try  {
            Connection connection = DriverManager.getConnection(Level3A_JDBC.DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery("SELECT studentID, memberName FROM Member");
            while (results.next()) 
            {
                String studentID = results.getString("studentID");
                String memberName = results.getString("memberName");
                
                html += "<li>" +
                        "<span>"+ memberName + "</span>" +
                        "<p>" + studentID + "</p>" +
                        "</li>";
            }
        } catch (SQLException e) {
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

    public void handleFormSubmission(Context context) 
    {
        try 
        {
            // CONVERT from input to variable
            String region = context.formParam("region");
            String startYear = context.formParam("start-year");
            String periodLength = context.formParam("period-length");
            String similarityType = context.formParam("similarity-type");
            String regionOption = context.formParam("region-options");
            String numPeriods = context.formParam("num-periods");  
            
            // CHECK parameters
            if (region == null || region.isEmpty() ||
                startYear == null || startYear.isEmpty() ||
                periodLength == null || periodLength.isEmpty() ||
                similarityType == null || similarityType.isEmpty() ||
                numPeriods == null || numPeriods.isEmpty() ||
                regionOption == null || regionOption.isEmpty()) 
                {
                    throw new IllegalArgumentException("Missing or empty form parameters");
                }

            int startYearInt = Integer.parseInt(startYear);
            int periodLengthInt = Integer.parseInt(periodLength);
            int endYearInt = startYearInt + periodLengthInt - 1;
            int numPeriodInt = Integer.parseInt(numPeriods);
            String html = "";

            // GET HTML temperature if selected
            if ("temperature".equals(similarityType)) 
            {
                html += """
                                    <th> Average Temperature </th>
                                </tr>
                            </thead>
                            <tbody>
                        """;
                html += JDBC3B.getAverageTemperatureResultTable(region, startYearInt, endYearInt, regionOption, periodLengthInt, numPeriodInt);
                
            }
            //GET HTML population if chosen
            else if ("population".equals(similarityType)) 
            {
                html += """
                                    <th> Average Population </th>
                                </tr>
                            </thead>
                            <tbody>
                        """;
                html += JDBC3B.getAveragePopulationResultTable(startYearInt, endYearInt, regionOption, periodLengthInt, numPeriodInt);
            }
            // GET HTML both if selected
            else if ("both".equals(similarityType))
            {
                html += """
                                    <th> Average Temperature </th>
                                    <th> Average Population </th>
                                </tr>
                            </thead>
                            <tbody>
                        """;
                html += JDBC3B.getAverageTemperaturePopulationResultTable(startYearInt, endYearInt, regionOption, periodLengthInt, numPeriodInt);
            }

            // RUN HTML 
            StringBuilder resultHtml = new StringBuilder();
            resultHtml.append(html);

            renderForm(context, resultHtml.toString(), JDBC3B.getRegionOptions(region), region);

        } 
        catch (IllegalArgumentException e) 
        {
            context.status(400).result("Bad Request: " + e.getMessage());
        } 
        catch (Exception e) 
        {
            context.status(500).result("Internal Server Error: " + e.getMessage());
            e.printStackTrace();
        }
    }



}
