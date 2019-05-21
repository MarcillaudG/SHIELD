package fr.irit.smac.shield.cahcoac;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class GUITest extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    private double xOffset = 0;
    private double yOffset = 0;
    @Override
    public void start(Stage stage) throws Exception{

        //Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("test2.fxml"));

        stage.setTitle("STRUDEL Generator");
        stage.setScene(new Scene(root));
        stage.getScene().getStylesheets().add("fr/irit/smac/shield/cahcoac/style.css");
        stage.getIcons().add(
                new Image(
                        GUITest.class.getResourceAsStream( "icon.png" )));
        stage.show();
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
    }
}
