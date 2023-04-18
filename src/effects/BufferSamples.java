package effects;

import static player.AudioPlayer.BUFF_SIZE;

public class BufferSamples {
    public static final int SAMPLE_SIZE = BUFF_SIZE / 2;
    private final short[][] sampleDelays;
    private final int sizeArray;
    private int indexCurrentElement;

    public BufferSamples(int sizeArray) {
        this.sampleDelays = new short[sizeArray][SAMPLE_SIZE];
        this.sizeArray = sizeArray;
        this.indexCurrentElement = 0;
    }

    public int getIndexCurrentElement() {
        return indexCurrentElement;
    }

    public void add(short[] sample) {
        this.indexCurrentElement = (this.indexCurrentElement + 1) % this.sizeArray;
        System.arraycopy(sample, 0, this.sampleDelays[this.indexCurrentElement], 0, SAMPLE_SIZE);

        for (int i = 0; i < this.sizeArray; i++) {
            for (int j = 0; j < SAMPLE_SIZE; j++) {
                this.sampleDelays[i][j] *= 0.9;
            }
        }
    }

    public short getAmplitudeSampleDelay(int indexSample, int indexAmplitude) {
        return sampleDelays[indexSample][indexAmplitude];
    }
}
