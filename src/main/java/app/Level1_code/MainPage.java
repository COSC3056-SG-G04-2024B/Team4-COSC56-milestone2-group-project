package app.Level1_code;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainPage implements Handler 
{
    public static final String URL = "/";

    @Override
    public void handle(Context context) throws Exception 
    {
        String html = "<html>";

        html += """
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title> Mainpage</title> 
            <link rel="stylesheet" href="./Level1_format_style.css">
            <link rel="stylesheet" href="./Level1_style.css">
        </head>
        <body>
            <header>
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
                <div class="lang">
                    <a href="https://www.youtube.com/watch?v=dQw4w9WgXcQ" target= "_blank"><img src="vietnameseIcon.png" height="75"></a>
                    <a href="https://www.youtube.com/watch?v=dQw4w9WgXcQ" target="_blank" ><img src="ausLanguageIcon.png"  height="75"></a>
                </div>
            </header>
            <div class='image'>
                <h1>Together, We Can Heal the Earth: Act Now for a Sustainable Future</h1>
            </div>
            <div class = 'more'>
            <h2> Scroll down for more </h2>
            <div class = 'info-container'>
                """;

        // SHOW 4 information blocks  
        html += getInfoBlock();

        html += "</div></div>";

        // DISPLAY year Quote
        html += getYearRangeQuote();     

        html += """
                <div class="our_information">
                    <div class="about_us">
                        <h2>About us</h2>
                        <p><strong>Welcome</strong> to my team, We have created this website to help you realize that climate change is happening faster in recent years. Our first goal is to provide the public with an easy way to find information about climate change through changes in annual temperatures globally or by country.</p>
                    </div>
                    <div class="information">
                        <p>We also provide more in-depth information about climate change through powerful analytical tools to help some people who need data for analysis or to make a presentation in a school project. All of us want people to care more about climate change and its future consequences.</p>
                    </div>
                </div>
               """;
        
        html += """      
                <div class="footer_section">
                <div class='personal-container-wrapper'>
                """;

        // GET and SHOW personas
        html += getPersonas();

        html += "</div>";
           
        html += """ 
                <div class="site_map_footer">
                    <div 
                        class = "site_map_footer_blank">
                    </div>
                    <div class="memberInfo">
                        <ul>
                            <div class ="titleName"> Development team members </div>
                """;

        // GET member information
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

            html += "           <li>";
            html += "               <span>" + memberName + "</span>";
            html += "               <p>" + studentID + "</p>";
            html += "           </li>";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        html += """
                        </ul>
                    </div>
                 </div>  
            </body>
                """;
        context.html(html);
    }

    //GET HTML year range quote
    private String getYearRangeQuote() throws SQLException
{
    String html = "<section class='landing'><div class='overlay'>";
    Connection connection = null;

    try
    {
        connection = DriverManager.getConnection(JDBCConnection.DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        int minTempYear = 0;
        int maxTempYear = 0;
        double min_temp = 0;
        double max_temp = 0;
        int minPopYear = 0;
        int maxPopYear = 0;
        long min_pop = 0;
        long max_pop = 0;

        String queryTemp = """
                            SELECT MIN(year) AS min_year, MAX(year) AS max_year 
                            FROM GlobalYearlyTemp;
                            """;
        ResultSet tempResult = statement.executeQuery(queryTemp);

        if (tempResult.next())
        {
            minTempYear = tempResult.getInt("min_year");
            maxTempYear = tempResult.getInt("max_year");
        }

        String yearMinMaxTemp = """
                                SELECT MIN(AverageTemperature) AS minTY, MAX(AverageTemperature) AS maxTY 
                                FROM GlobalYearlyTemp 
                                WHERE Year IN (
                                    (SELECT MIN(year) FROM GlobalYearlyTemp ),
                                    (SELECT MAX(year) FROM GlobalYearlyTemp )
                                )
                                """;
        ResultSet tempMinMaxResult = statement.executeQuery(yearMinMaxTemp);
        if (tempMinMaxResult.next())
        {
            min_temp = tempMinMaxResult.getDouble("minTY");
            max_temp = tempMinMaxResult.getDouble("maxTY");
        }

        String queryPop = """
                            SELECT MIN(year) AS min_year, MAX(year) AS max_year
                            FROM Populations;
                          """;
        ResultSet popResult = statement.executeQuery(queryPop);
        if (popResult.next())
        {
            minPopYear = popResult.getInt("min_year");
            maxPopYear = popResult.getInt("max_year");
        }

        String yearMinMaxPop = """
                                SELECT MIN(population) AS minPY, MAX(population) AS maxPY
                                FROM Populations
                                WHERE Year IN (
                                    (SELECT MIN(year) FROM Populations WHERE countryname = 'World'),
                                    (SELECT MAX(year) FROM Populations WHERE countryname = 'World')
                                )
                                AND countryname LIKE 'World';
                                """;
        ResultSet popMinMaxResult = statement.executeQuery(yearMinMaxPop);
        if (popMinMaxResult.next())
        {
            min_pop = popMinMaxResult.getLong("minPY");
            max_pop = popMinMaxResult.getLong("maxPY");
        }
        html += "<div class='commoninfor'><h3>Our website demonstrates " + 
                (maxTempYear - minTempYear + 1) + " years of temperature data from " + minTempYear + " to " 
                + maxTempYear +
                " and " + (maxPopYear - minPopYear + 1) + " years of population data from " +
                minPopYear + " to " + maxPopYear + 
                "</h3></div></div></section>";

        html += "<div class = 'container0'>" +
                "<h2><strong> 1750 world temperature</strong></h2>" +
                "<div class = 'infoboxes'> " + min_temp + "°C</div>" +
                "<h2><strong>  2015 world temperature </strong></h2>" +
                "<div class = 'infoboxes'> " + max_temp + "°C</div>" +
                "<h2><strong>  1960 world temperature </strong></h2>" +
                "<div class = 'infoboxes'> " + min_pop + " people</div>" +
                "<h2><strong>  2013 world temperature </strong></h2>" +
                "<div class = 'infoboxes'> " + max_pop + " people</div>" +
                "</div>";
    }
    finally
    {
        if (connection != null)
            connection.close();
    }

    return html;
}

    //GET 4 information blocks
    private String getInfoBlock()
    {
        String html = "";
        Connection connection = null;
        try  
        {
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            
            String[] queries = 
            {
                "SELECT COUNT(*) AS 'total' FROM GlobalYearlyLandTempByCity",
                "SELECT COUNT(DISTINCT Year) AS 'total' FROM GlobalYearlyTemp",
                "SELECT COUNT(*) AS 'total' FROM GlobalYearlyLandTempByCountry",
                "SELECT COUNT(*) AS 'total' FROM Populations"
            };
            String[] labels = 
            {
                "results by cities",
                "within years",
                "results by countries",
                "results by Populations"
            };

            for (int i = 0; i < queries.length; i++) 
            {
                ResultSet resultSet = statement.executeQuery(queries[i]);
                if (resultSet.next()) 
                {
                    int total = resultSet.getInt("total");
                    String infoBlock = "info-block" + (i+1);

                    html += "<div class ='" + infoBlock + "'>" +
                                "<h1>"+ total + "</h1>" +
                                "<p>" + labels[i] + "</p>" +
                            "</div>";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return html;
    }

    //GET HTML persona 
    private String getPersonas() throws SQLException
    {
        String html = "";
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);
            Statement statement = connection.createStatement();

            statement.setQueryTimeout(30);

            String [] personas = {"Sarah Brown", "Oscar Scott", "Anthony Green"};

            String query = """
                                SELECT background, needs, goals, skillsandexperiences, painpoints
                                FROM Personas
                                WHERE name = ?;
                                """;

            PreparedStatement queryStatement = connection.prepareStatement(query);

            for (String persona : personas)
            {          
                queryStatement.setString(1, persona);
                System.out.println("GET persona executed:" + query);
                ResultSet result = queryStatement.executeQuery();
                
                while (result.next())
                {
                    String background = result.getString("background");
                    String needs = result.getString("needs");
                    String goals = result.getString("goals");
                    String SaE = result.getString("skillsandexperiences");
                    String pp = result.getString("painpoints");

                    html += "<div class = 'personal-container'>" +
                            "<div class = 'personal-header'><h2>" + persona + "</h2></div>" +
                            "<div class = 'personal-content'>" +
                            "<h3> Background: </h3><p>" + background + "</p>" +
                            "<h3> Needs: </h3><p>" + needs + "</p>" +
                            "<h3> Goals: </h3><p>" + goals + "</p>" +
                            "<h3> Skill and Experiences: </h3><p>" + SaE + "</p>" +
                            "<h3> Pain point: </h3><p>" + pp + "</p>" +
                            "</div></div>";
                    }
            }
            queryStatement.close();
        }
        catch (SQLException e)
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

        html += "</div>";
        return html;
    }
}