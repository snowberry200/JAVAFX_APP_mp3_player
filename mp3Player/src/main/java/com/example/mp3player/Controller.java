package com.example.mp3player;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    @FXML
    ProgressBar progressBarController;
    double current;
    double end;
    @FXML
    private ComboBox<Integer> speedChanger;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label myLabel;
    private MediaPlayer mediaPlayer;
    private ArrayList<File> songs;
    private int songNumber;
    private int[] speed;
    private Timer timer;
    private TimerTask timerTask;
    private Boolean running;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //look in this file (directory)
            File directory = new File("music");
            //find files in directory and list them
            File[] mp3files = directory.listFiles();
            //arrange files in an array so to get index
            assert mp3files != null;
            songs = new ArrayList<>(List.of(mp3files));
            //initialize song Index
            int songNumber = 0;
            // initialize media
            Media media = new Media(songs.get(songNumber).toURI().toString());
            //initialize media player
            mediaPlayer = new MediaPlayer(media);
            //displaying song title and song number
            myLabel.setText(songs.get(songNumber).getName());
            //initialize speed
            speed = new int[]{1, 2, 3, 4};
        } catch (Exception e) {
            System.out.println(e);
        }

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);

            }
        });

        for (int j : speed) {
            speedChanger.getItems().add(Integer.valueOf(Integer.toString(j)));
        }

        speedChanger.setOnAction(this::changeSpeed);

        //FOR PROGRESSION OF THE SONGS
        progressBarController.setStyle("-fx-accent:#00ff00;");
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (running = true) {
                        double current = mediaPlayer.getCurrentTime().toSeconds();
                        double end = mediaPlayer.getTotalDuration().toSeconds();
                        progressBarController.setProgress(current / end);
                        beginTimer();

                    } else if (running = current / end == 1) {
                        running = false;
                        cancelTimer();
                    } else {
                        cancelTimer();
                    }


                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        };

    }

    //changing media play speed
    public void changeSpeed(ActionEvent event) {
        if (speedChanger.getValue() == null) {
            mediaPlayer.setRate(1);
        } else {
            mediaPlayer.setRate(Integer.parseInt(String.valueOf(speedChanger.getValue())));
        }
    }

    //play media Function
    public void playMedia() {
        //set media volume
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        //add media speed to play function
        changeSpeed(null);
        try {
            mediaPlayer.play();
            //begin timer
            beginTimer();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //media reset function
    public void resetMedia() {

        try {
            progressBarController.setProgress(0);
            mediaPlayer.seek(Duration.seconds(0));
            playMedia();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    //Pause media function
    public void pauseMedia() {
        cancelTimer();
        try {
            mediaPlayer.pause();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //previous media function
    public void previousMedia() {
        try {
            if (songNumber > 0) {
                try {
                    songNumber--;
                    mediaPlayer.stop();
                    Media media = new Media(songs.get(songNumber).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    myLabel.setText(songs.get(songNumber).getName());
                    playMedia();
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else if (songNumber == 0) {
                songNumber = songs.size() - 1;
                mediaPlayer.stop();
                Media media = new Media(songs.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                myLabel.setText(songs.get(songNumber).getName());
                playMedia();
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //skip media function
    public void nextMedia() {
        try {
            if (songNumber < songs.size() - 1) {
                songNumber++;
                mediaPlayer.stop();
                Media media = new Media(songs.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                myLabel.setText(songs.get(songNumber).getName());
                playMedia();
            } else if (songNumber == songs.size() - 1) {
                try {
                    songNumber = 0;
                    mediaPlayer.stop();
                    Media media = new Media(songs.get(songNumber).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    myLabel.setText(songs.get(songNumber).getName());
                    playMedia();
                } catch (Exception e) {
                    System.out.println(e);

                }


            } else {
                playMedia();
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    //PROGRESS BAR
    //begin timer function
    public void beginTimer() {
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    //cancel timer function

    public void cancelTimer() {
        running = false;
        timer.cancel();
    }


}