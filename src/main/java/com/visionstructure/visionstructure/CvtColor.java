package com.visionstructure.visionstructure;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.HashMap;

import static com.visionstructure.visionstructure.MainView.matToImage;

public class CvtColor {
    @FXML
    public ChoiceBox<String> choiceBox;

    private static HashMap<String, Integer> map = new HashMap<>();

    private ImageView imageInput = null;
    private Image ResultImage;
    private MainView mainView;

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public void setImageView(ImageView imageView) {
        this.imageInput = imageView;
    }

    @FXML
    private void initialize() {
        initializeMap();
        choiceBox.setItems(FXCollections.observableArrayList(map.keySet()));

        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedValue) -> {
            if (selectedValue != null) {
                int selectedColor = map.get(selectedValue);
                mainView.selectedColor = selectedColor;
//                Mat mat = MainView.imageViewToMat(imageInput);
//                Mat source = new Mat();
//                Imgproc.cvtColor(mat, source, selectedColor);
//                mat.release();
//                System.out.println(source.size());
//                mainView.setProcessedImageView(source);
            }

        });
    }

    private void initializeMap() {
        if (map.isEmpty()) {
            map.put("COLOR_BGR2HSV", Imgproc.COLOR_BGR2HSV);
            map.put("COLOR_BGR2GRAY", Imgproc.COLOR_BGR2GRAY);
            map.put("COLOR_BGR2RGB", Imgproc.COLOR_BGR2RGB);
            map.put("COLOR_BGR2BGRA", Imgproc.COLOR_BGR2BGRA);
        }
    }
}
