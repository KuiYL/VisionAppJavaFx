package com.visionstructure.visionstructure;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;
import java.io.IOException;

public class HelloApplication extends Application {

    static
    {
        System.load("C:\\Users\\Monbe\\IdeaProjects\\VisionStructure\\src\\main\\java\\com\\visionstructure\\visionstructure\\libs\\opencv_java490.dll");
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Visio");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}