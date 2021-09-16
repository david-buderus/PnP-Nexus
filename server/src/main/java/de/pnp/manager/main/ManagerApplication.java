package de.pnp.manager.main;

import de.pnp.manager.ui.ManagerView;
import javafx.application.Application;
import javafx.stage.Stage;
import net.ucanaccess.jdbc.UcanaccessDriver;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ManagerApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            DriverManager.registerDriver(new UcanaccessDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ManagerView view = new ManagerView(primaryStage, this);

        if (this.getParameters().getRaw().size() > 0) {
            view.load(new File(this.getParameters().getRaw().get(0)));
        }
    }

}
