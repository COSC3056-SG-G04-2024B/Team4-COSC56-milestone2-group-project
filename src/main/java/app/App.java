package app;

import app.Level1_code.MainPage;
import app.Level2A_code.Level2A_final;
import app.Level2B_code.Level2B_final;
import app.Level3A_code.Level3A_final;
import app.Level3B_code.Level3B_final;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;


/**
 * Main Application Class.
 * <p>
 * Running this class as regular java application will start the 
 * Javalin HTTP Server and our web application.
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class App {

    public static final int         JAVALIN_PORT    = 7004;
    public static final String      CSS_DIR         = "css/";
    public static final String      IMAGES_DIR      = "images/";

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new RouteOverviewPlugin("/help/routes"));
            config.addStaticFiles(CSS_DIR);
            config.addStaticFiles(IMAGES_DIR);
        }).start(JAVALIN_PORT);
        
        configureRoutes(app);
    }

    public static void configureRoutes(Javalin app) {
        
        // MAINPAGE    * LEVEL 1 *
        app.get(MainPage.URL, new MainPage());
        // Calculation page  * LEVEL 2 *
        app.get(Level2A_final.URL, new Level2A_final());
        app.get(Level2B_final.URL, new Level2B_final());
        
        // FINAL CALCULATION PAGE  * LEVEL 3 *
        app.get(Level3A_final.URL, new Level3A_final());
        app.get(Level3B_final.URL, new Level3B_final());

        //             *   POST   *
        app.post(Level2A_final.URL, new Level2A_final());
        app.post(Level2B_final.URL, new Level2B_final());
        app.post(Level3A_final.URL, new Level3A_final());
        app.post(Level3B_final.URL, new Level3B_final());
    }

}
