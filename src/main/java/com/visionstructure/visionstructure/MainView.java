package com.visionstructure.visionstructure;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class MainView {

    @FXML
    public ImageView imageOne;
    public ImageView imageTwo;
    public SplitPane imagePane;
    public Button buttonSettings;
    public Button buttonSelect;

    private VideoCapture grabber;

    @FXML
    private void initialize() {

        grabber = new VideoCapture(0);

        if (!grabber.isOpened()) {
            System.err.println("Не удалось открыть камеру!");
        } else {
            Thread thread = new Thread(this::updateImageView);
            thread.setDaemon(true);
            thread.start();
        }
        buttonSettings.setOnAction(event -> {
            loadNewFXML("inRange.fxml");
        });


        buttonSelect.setOnAction(event -> {
            loadNewFXML("cvtColor.fxml");
        });
    }
    public int selectedColor = Imgproc.COLOR_BGR2RGB;

    private void updateImageView() {
        while (!Thread.interrupted()) {
            Mat frame = new Mat();
            if (grabber.read(frame)) {
                Mat source = new Mat();
                Mat finalSource = new Mat();
                finalSource = frame.clone();
                source = frame.clone();
                if (selectedColor != Imgproc.COLOR_BGR2RGB) {

                    Imgproc.cvtColor(frame, finalSource, selectedColor);
                }


                imageOne.setImage(matToImage(source));
                Mat finalSource1 = finalSource;
                Platform.runLater(() -> {
                    imageTwo.setImage(matToImage(finalSource1));
                });

                source.release();
            }
        }
    }

    private void loadNewFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            if (fxmlPath.equals("cvtColor.fxml")) {
                CvtColor controller = loader.getController();
                controller.setMainView(this);
                controller.setImageView(this.imageTwo);
            }
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProcessedImageView(Mat processedMat) {
        if (processedMat != null) {
            Image processedImage = matToImage(processedMat);
            Platform.runLater(() -> {
                imageTwo.setImage(processedImage);
            });
        }
    }
    public static Mat imageViewToMat(ImageView imageView) {
        Image fxImage = imageView.getImage();

        if (fxImage == null) {
            System.err.println("ImageView does not contain an image.");
            return null;
        }

        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            if (!ImageIO.write(bufferedImage, "png", byteArrayOutputStream)) {
                System.err.println("Failed to write image to buffer.");
                return null;
            }

            return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (CvException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Image matToImage(Mat mat) {
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".png", mat, byteMat);

        byte[] byteArray = byteMat.toArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);

        try {
            BufferedImage bufferedImage = ImageIO.read(bis);
            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
