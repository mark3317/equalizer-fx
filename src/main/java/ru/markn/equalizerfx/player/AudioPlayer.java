package ru.markn.equalizerfx.player;

import ru.markn.equalizerfx.effects.Chorus;
import ru.markn.equalizerfx.effects.Overdrive;
import ru.markn.equalizerfx.equalizer.Equalizer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import javax.sound.sampled.*;

public class AudioPlayer {
    private final File currentMusicFile;
    private AudioInputStream audioStream;
    private SourceDataLine sourceDataLine;
    public static final int BUFF_SIZE = 15000;
    private final byte[] bufferBytes = new byte[BUFF_SIZE];
    private short[] bufferShort = new short[BUFF_SIZE / 2];
    private boolean pauseStatus;
    private boolean stopStatus;
    private double gain;
    private final Equalizer equalizer;
    private final Chorus chorus;
    private boolean isChorus;
    private final Overdrive overdrive;
    private boolean isOverdrive;

    public AudioPlayer(File musicFile) {
        this.currentMusicFile = musicFile;
        this.equalizer = new Equalizer();
        this.gain = 1.0;
        this.isOverdrive = false;
        this.overdrive = new Overdrive();
        this.isChorus = false;
        this.chorus = new Chorus();
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

                if (this.pauseStatus) this.pause();

                if (this.stopStatus) break;

                if (this.isOverdrive) {
                    this.overdrive(this.bufferShort);
                }

                if (this.isChorus) {
                    this.chorus(this.bufferShort);
                }

                equalizer.setInputSignal(this.bufferShort);
                this.equalizer.equalization();
                this.bufferShort = equalizer.getOutputSignal();

                this.ShortArrayToByteArray();
                this.sourceDataLine.write(this.bufferBytes, 0, this.bufferBytes.length);
            }
            this.sourceDataLine.drain();
            this.sourceDataLine.close();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException | ExecutionException |
                 InterruptedException e) {
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
        if (this.audioStream != null) try {
            this.audioStream.close();
        } catch (IOException ignored) {}
        if (this.sourceDataLine != null) this.sourceDataLine.close();
    }

    private void ByteArrayToShortArray() {
        for (int i = 0, j = 0; i < this.bufferBytes.length; i += 2, j++) {
            this.bufferShort[j] = (short) ((ByteBuffer.wrap(this.bufferBytes, i, 2).order(java.nio.ByteOrder.LITTLE_ENDIAN).getShort() / 2.0) * this.gain);
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

    private void overdrive(short[] inputSamples) {
        this.overdrive.setInputSampleStream(inputSamples);
        this.overdrive.createEffect();
    }

    public boolean overdriveIsActive() {
        return this.isOverdrive;
    }

    public void setOverdrive(boolean b) {
        this.isOverdrive = b;
    }

    private void chorus(short[] inputSamples) throws ExecutionException, InterruptedException {
        chorus.setInputSampleStream(inputSamples);
        chorus.createEffect();
    }

    public boolean ChorusIsActive() {
        return this.isChorus;
    }

    public void setChorus(boolean b) {
        this.isChorus = b;
    }

}
