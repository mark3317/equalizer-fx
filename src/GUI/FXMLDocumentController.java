package GUI;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.*;
import javafx.stage.Stage;
import player.AudioPlayer;
import java.io.File;
import java.io.IOException;

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
            System.out.println("PLAY");
        	this.audioPlayer.play();
        });
        playThread.start();
    }

    @FXML
    private void play() {
        System.out.println("PLAY");
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
        System.out.println("PAUSE");
        if (this.audioPlayer != null)
            this.audioPlayer.setPauseStatus(true);
    }

    @FXML
    private void stop() {
        System.out.println("STOP");
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

    @FXML
    private void filterBox() {
        System.out.println("FIR HPF");
        if(!this.audioPlayer.filterIsActive())
            this.audioPlayer.setFilter(true);
        else this.audioPlayer.setFilter(false);
    }
}
