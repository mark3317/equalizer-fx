package player;

import equalizer.Equalizer;
import equalizer.Filter;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

public class AudioPlayer {
    private final File currentMusicFile;
    private AudioInputStream audioStream;
    private SourceDataLine sourceDataLine;
    public static final int BUFF_SIZE = 30000;
    private final byte[] bufferBytes  = new byte[BUFF_SIZE];
    private short[] bufferShort = new short[BUFF_SIZE / 2];
    private boolean pauseStatus;
    private boolean stopStatus;
    private double gain;
    private final Equalizer equalizer;

    public AudioPlayer(File musicFile) {
        this.currentMusicFile = musicFile;
        this.equalizer = new Equalizer();
        this.gain = 1.0;
    }


    public void play() {
        try {
            this.audioStream = AudioSystem.getAudioInputStream(currentMusicFile);
            AudioFormat audioFormat = audioStream.getFormat();
            this.sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
            this.sourceDataLine.open(audioFormat);
            this.sourceDataLine.start();
            this.pauseStatus = false;
            this.stopStatus = false;

            while ((this.audioStream.read(this.bufferBytes) != -1)) {
                this.ByteArrayToShortArray();

                if (this.pauseStatus)
                    this.pause();

                if (this.stopStatus)
                    break;

                equalizer.setInputSignal(this.bufferShort);
                this.equalizer.equalization();
                this.bufferShort = equalizer.getOutputSignal();

                this.ShortArrayToByteArray();
                this.sourceDataLine.write(this.bufferBytes, 0, this.bufferBytes.length);
            }
            this.sourceDataLine.drain();
            this.sourceDataLine.close();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void pause() {
        if (this.pauseStatus) {
            while (true) {
                try {
                    if (!this.pauseStatus || this.stopStatus) break;
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public void setPauseStatus(boolean pauseStatus) {
        this.pauseStatus = pauseStatus;
    }

    public void setStopStatus(boolean stopStatus) {
        this.stopStatus = stopStatus;
    }

    public boolean getStopStatus() {
        return this.stopStatus;
    }

    public void close() {
        if (this.audioStream != null)
            try {
                this.audioStream.close();
            } catch (IOException e) {
            }
        if (this.sourceDataLine != null)
            this.sourceDataLine.close();
    }

    private void ByteArrayToShortArray() {
        for (int i = 0, j = 0; i < this.bufferBytes.length; i += 2, j++) {
            this.bufferShort[j] = (short) ((ByteBuffer.wrap(this.bufferBytes, i, 2).order(
                    java.nio.ByteOrder.LITTLE_ENDIAN).getShort() / 2) * this.gain);
        }
    }

    private void ShortArrayToByteArray() {
        for (int i = 0, j = 0; i < this.bufferShort.length && j < this.bufferBytes.length; i++, j += 2) {
            this.bufferBytes[j] = (byte) (this.bufferShort[i]);
            this.bufferBytes[j + 1] = (byte) (this.bufferShort[i] >>> 8);
        }
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    public Equalizer getEqualizer() {
        return this.equalizer;
    }

}
