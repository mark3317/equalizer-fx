package player;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer  {
    private Clip clipSound;
    private final AudioInputStream audioStream;

    public AudioPlayer(File musicFile) throws UnsupportedAudioFileException, IOException {
        this.audioStream = AudioSystem.getAudioInputStream(musicFile);
        AudioFormat audioFormat = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
        try {
            if (AudioSystem.isLineSupported(info)) {
                this.clipSound = (Clip) AudioSystem.getLine(info);
                this.clipSound.open(audioStream);
            }
        } catch (LineUnavailableException | IOException e) {
        }
    }

    public void play() {
        this.clipSound.start();
    }

    public void pause() {
        long currentFrame = this.clipSound.getMicrosecondPosition();
            this.clipSound.stop();
            this.clipSound.setMicrosecondPosition(currentFrame);
    }

    public void stop() {
        this.clipSound.stop();
        this.clipSound.setMicrosecondPosition(0);
    }

    public void close() {
        if (this.audioStream != null)
            try {
                this.audioStream.close();
            } catch (IOException e) {
            }
        if (this.clipSound != null)
            this.clipSound.close();
    }
}
