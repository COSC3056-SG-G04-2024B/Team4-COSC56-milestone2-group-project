package app.Level3A_code;
 
import io.javalin.http.Context;
import io.javalin.http.Handler;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import app.Level2A_code.Level2A_JDBC;
 
public class Level3A_final implements Handler {
 
    public static final String URL = "/Level3A";
 
  @Override
  public void handle(Context context) throws Exception 
  {

    Level3A_JDBC JDBCConnection = new Level3A_JDBC();
    String html = "<html>";
 
    html += """
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Webpage Level 3A</title>
            <link rel="stylesheet" href="./Level3A_style.css">
        </head>
        <body>
        <header>
            <a href="/"><img src="image.png" alt="Logo" class="logo"></a>
          
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
              <h1>Temperature Changing</h1>
          </div>
          <div class = 'container'>
            <section class="tool1-content-container">
                <h2 class = 'tool1-title'">Single Region && Single Period</h2>
                <div class="tool1-filter">
                        <div>
                            <form action="/Level3A" method="post">
                            <div class="form-container">
                                  <div class="form-item">
                                    <label for="startYear">Starting Year:</label>
                                    <input type="number" id="startYear" name="startYear" placeholder="Enter the start year" min="1750" max="2015"><br>
                                  </div>
                                  <div class="form-item id="item1">
                                    <label for="startTool1">Length:</label>
                                    <input type="number" id="startTool1" name="startTool1" placeholder="Enter the spans for the year" min="1"><br>
                                  </div>
                                  <div class="form-item">
                                    <label for="locationTool1">Location:</label>
                                    <input type="text" id="locationTool1" name="locationTool1" placeholder="Enter your location"><br>
                                  </div>
                                  <div class="form-item">
                                    <label for="regionTool1">Region:</label>
                                    <select id="regionTool1" name="regionTool1">
                                        <option value="" disabled selected>Select a Type of Region</option>
                                        <option value="Global">Global</option>
                                        <option value="Country">Country</option>
                                        <option value="State">State</option>
                                        <option value="City">City</option>
                                    </select><br>
                                  </div>
                                  </div>
                                    <input class="submit" type="submit" value="Submit">
                            </form>
                        </div>
                </div>
                <div></div>
                <div class="containerOutput1">
                    <div class="new-box-output1">
                        <div style="text-align: center;">
                            <h2 class="output1-info">Your result displays here:</h2>
                        </div>
                        <p class="output1-box">
            """;
       
    String startYearTool1 = context.formParam("startYear");
    String startTool1 = context.formParam("startTool1");
    String locationTool1 = context.formParam("locationTool1");
    String regionTool1 = context.formParam("regionTool1");

    if (startYearTool1 == null) 
    {
      startYearTool1 = "1990";
    }
    
    if (startTool1 == null) 
    {
      startTool1 = "10";
    }
  
    if (locationTool1 == null) 
    {
      locationTool1 = "Australia";
    }
 
    if (regionTool1 == null) 
    {
      regionTool1 = "Country";
    }
    
    // DISPLAY output table tool 1
    html += JDBCConnection.getOutputTool1(startYearTool1, startTool1, locationTool1, regionTool1);
 
    html += """
              </p>
              <div class = 'terms_of_use'>
                  <h3 class = 'how '><strong>How to use our tool:</strong></h3>
                  <div>
                      <p>1. There are 4 boxes for you to choose Starting Year - Length - Location - Region. After filling in every boxes you just need to click Submit to get the data from our database<br>
                      Example: (1990, 10, Australia, Country)</p>
                  </div>
              </div>
              <div>
                  <p><br>2. Basic Information - If region input is empty, then the default value will set to Country</p>
                  <div>
                      <strong> <br>The range of year for City/State/Country is 1750-2013 and Range of Global year is 1750-2015 </strong>
                  </div>
              </div>
              </div>
              </div>
        </section>
            """;
 
 
        html += """
              <section class="tool2-content-container">
                  <h2>Single Region && Multiple Period</h2>
                  <div class="tool2-filter">
                      <div class="new-box">
                          <div>
                              <form action="/Level3A" method="post">
                              <div class="form-container">
                                  <div class="form-item">
                                <details class = 'title-checkbox'>
                                  <summary> Year checkbox </summary>
                                    <div>
                                      <br>
                                      <div class = 'tool2-checkbox-container'>
                  """;
        // CHECK BOX
        html+= JDBCConnection.tool2YearCheckBox_2_1();
       
        html += """
                                        </div>
                                        <br>
                                      </div>
                                    </details>
                                  <script>
    function toggleFolder(folderId) {
        var folderContent = document.getElementById(folderId);
        if (folderContent.style.display === "none" || folderContent.style.display === "") {
            folderContent.style.display = "block";
        } else {
            folderContent.style.display = "none";
        }
    }
</script>
</div>
                                
                                <div class="form-item">
                                <label for="length">Length:</label>
                                <input type="number" name="lengthTool2" placeholder="Enter year length" min="1"><br>
                                </div>
                                <div class="form-item">
                                <label for="Location">Location:</label>
                                <input type="text" name="locationTool2" placeholder="Enter location"><br>
                                </div>
                                <div class="form-item">
                                <label for="region">Region:</label>
                                <select name="regionTool2">
                                    <option value="" disabled selected>Select a Type of Region</option>
                                    <option value="Global">Global</option>
                                    <option value="Country">Country</option>
                                    <option value="State">State</option>
                                    <option value="City">City</option>
                                </select><br>
                                </div>
                                </div>
                                <input class="submit1" type="submit" value="Submit">
                                </form>
                                </div>
                                </div>
                                </div>
                                <div class="containerOutput2" style="margin-top: 10px;">
                                <div class="new-box-output1" style="margin-top: 10px;" >
                                    <div>
                                        <h3 class="output1-info">Your result displays here:</h3>
                                    </div>
                                    <table class="spreadsheet" id="fileTable">
                                        <thead>
                                            <tr>
                                                <th>RANK</th>
                                                <th>Year</th>
                                                <th>Average Value</th>
                                            </tr>
                                        </thead>
                                </div>
                                        <tbody>
                """;
 
 
    // Process form parameters
    ArrayList<String> startYearTool2 = new ArrayList<>();
 
    for (int year = 1750; year <= 2015; year++) {
      String yearParam = context.formParam("year" + year);
      if (yearParam != null) {
        startYearTool2.add(yearParam);
      }
    }
    if (startYearTool2.size() == 0) {
      startYearTool2.add("1990");
      startYearTool2.add("1995");
      startYearTool2.add("2000");
    }
 
    String lengthTool2 = context.formParam("lengthTool2");
 
    if (lengthTool2 == null) {
      lengthTool2 = "10";
 
    }
    String locationTool2 = context.formParam("locationTool2");
 
    if (locationTool2 == null) {
      locationTool2 = "Australia";
    }
    String regionTool2 = context.formParam("regionTool2");
 
    if (regionTool2 == null) {
      regionTool2 = "Country";
 
    }
 
    html += JDBCConnection.getOutputTool2(startYearTool2, lengthTool2, locationTool2, regionTool2);
 
    html += """
            </tbody>
            </table>
            <br><br>
                <div>
                    <strong>How to use our tool:</strong>
                    <div>
                        <p>You need to click on the "Year checkbox" and then you will see the list of year for you to choose. Next you just need to fill in every boxes and click submit </p><br>
                    </div>
                </div>
                  <div>
                      <strong>The range of year for City/State/Country is 1750-2013 and Range of Global year is 1750-2015 </strong>
                  </div>
                </div>
                </div>
             
            </section>
            """;
 
 
    html += """
            <section class="tool3-content-container">
                <h2>Multiple Region && Multiple Period</h2>
                <div class="tool3-filter">
                    <div class="new-box">
                        <div>
                            <form action="/Level3A" method="post">
                            <div class="form-container">
                                <div class="form-item">
                                <label for="Case">Multiple Region:</label>
                                <input type="text" name="CaseOptions" placeholder="Enter region + year.">
                                </div>
                                <div class="form-item">
                                <label for="length">Length:</label>
                                <input type="number" name="lengthTool3" placeholder="Enter year length" min="1">
                                </div>
                                <div class="form-item">
                                <label for="#">Region Type:</label>
                                <select name="regionTool3">
                                    <option value="" disabled selected>Select a Type of Region</option>
                                    <option value="Country">Country</option>
                                    <option value="State">State</option>
                                    <option value="City">City</option>
                                </select><br>
                                </div>
                                <div class="form-item">
                                <label for="FilterOptions">Filter Options:</label>
                                <select name="ConditionTool3">
                                    <option value="" disabled selected>Select a Condition</option>
                                    <option value="OR">Or | Output data if 1 condition is true</option>
                                    <option value="AND">And | Output data if both conditions are true</option>
                                    <option value="NONE">No filter | No filter</option>
                                    <option value="Pop">Population Only</option>
                                    <option value="Temp">Temperature Only</option>
                                </select>
                                </div>
                                <div class="form-item">
                                <label for="FilterOptions" >Set up Temperature Filter:</label>
                                <input type="number" name="minTempRange" placeholder="Minimum temperature" style="margin-bottom: 10px;">
                                <input type="number" name="maxTempRange" placeholder="Maximum temperature"><br>
                                </div>
                                <div class="form-item">
                                <label for="ConditionWithPopulation">Set up Population Filter:</label>
                                <input type="number" name="minPopulation" placeholder="Minimum Population " style="margin-bottom: 10px;">
                                <input type="number" name="maxPopulation" placeholder="Maximum Population ">
                                </div>
                                </div>
                                <input class="submit2" type="submit" value="Submit">
                            </form>
                    </div>
                </div> 
                </div> 
                <div class = 'blankspace-output3'></div>
                <div class="containerOutput3">
                    <div class="new-box-output1">
                        <div style="text-align: center;">
                            <h2 class="output1-info">Your result displays here:</h2>
                        </div>
                        <table class="spreadsheet" id="fileTable">
                            <thead>
                                <tr>
                                    <th>RANK</th>
                                    <th>LOCATION</th>
                                    <th>Year</th>
                                    <th>Average Value</th>
                                    <th>Population</th>
                                </tr>
                            </thead>
                            <tbody>
              """;
         
 
    String caseInput = context.formParam("CaseOptions");
    if (caseInput == null) {
      caseInput = "Vietnam 2000, Australia 1990, ";
    }
 
    ArrayList<String> caseLocation = new ArrayList<>();
 
    String[] elements = caseInput.split("[,\\s]+");
 
    for (int i = 0; i < elements.length; i += 2) {
      caseLocation.add(elements[i]);
    }
 
    ArrayList<String> caseYear = new ArrayList<>();
    String[] parts = caseInput.split("[,\\s]+");
 
    for (String part : parts) {
      try 
      {
        int year = Integer.parseInt(part);
        caseYear.add(Integer.toString(year)); 
      } catch (NumberFormatException e) {
      }
    }
 
    String regionTool3 = context.formParam("regionTool3");
    String lengthTool3 = context.formParam("lengthTool3");
    String MinTemp = context.formParam("minTempRange");
    String MaxTemp = context.formParam("maxTempRange");
    String Condition = context.formParam("ConditionTool3");
    String MinPop = context.formParam("minPopulation");
    String MaxPop = context.formParam("maxPopulation");
 
    html += JDBCConnection.getOutputTool3(caseLocation, caseYear, regionTool3, lengthTool3, caseInput, MinTemp, MaxTemp, Condition, MinPop, MaxPop);

    html += """
                          </tbody>
                      </table>
                      <br>          
                      <br>
                      <div>
                          <strong>How to use our tool:</strong>
                          <div>
                            <p>1. You need to type the country name + year. Example Australia 1990. If you want to add more you need to put the " , " to seperate each input </p><br>
                            <p>2. Then you are required to fill in the "Length" box and setup the region type and condition  </p><br>
                            <p>3. The last thing you need to do is setup min-max average, min-max population and click the submit b</p><br>
                          </div>
                      </div>
                      <div>
                          <strong>The range of year for City/State/Country is 1750-2013 and Range of Global year is 1750-2015 </strong>
                      </div>
                  </div>
                </div>
                </section>
                </div>
                """;

    // Footer of the HTML webpage
    html += """
            <div class="site_map_footer">
                <div class = "site_map_footer_blank"></div>
                <div class="memberInfo">
                    <ul>
                        <div class ="titleName"> Development team members </div>
                """;
       
        try  {
            Connection connection = DriverManager.getConnection(Level2A_JDBC.DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
 
            ResultSet results = statement.executeQuery("SELECT studentID, memberName FROM Member");
            while (results.next()) {
                String studentID = results.getString("studentID");
                String memberName = results.getString("memberName");
                    html+="<li>";
                    html+=("<span>")+(memberName)+("</span>");
                    html+=("<p>")+(studentID)+("</p>");
                    html+=("</li>");
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
 
 
}