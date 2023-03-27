package GUI;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.*;
import javafx.stage.Stage;
import player.AudioPlayer;
import java.io.File;

public class FXMLDocumentController {

    private AudioPlayer audioPlayer;
    private Thread playThread;

    @FXML
    private void open() {
        //Выбор файлов формата wav
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Audio Files", "*.wav"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if(selectedFile == null) return;

        this.audioPlayer = new AudioPlayer(selectedFile);
        playThread = new Thread(()->{
        	this.audioPlayer.play();
        });
        playThread.start();

        System.out.println("PLAY");
    }

    @FXML
    private void play() {
        if (this.audioPlayer != null){
            if (this.audioPlayer.getStopStatus()) {
                playThread = new Thread(() -> {
                    this.audioPlayer.play();
                });
                playThread.start();
            }
            else
                this.audioPlayer.setPauseStatus(false);
        }
    }

    @FXML
    private void pause() {
        if (this.audioPlayer != null)
            this.audioPlayer.setPauseStatus(true);
    }

    @FXML
    private void stop() {
        if (this.audioPlayer != null)
            this.audioPlayer.setStopStatus(true);
    }

    @FXML
    private void clickClose() {
    	if(this.audioPlayer != null) {
            if(this.playThread != null)
        	    this.playThread.interrupt();
            this.audioPlayer.close();
    	}

    	System.exit(0);
    }
}
