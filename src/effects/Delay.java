package effects;

import java.util.concurrent.Callable;

import static effects.BufferSamples.SAMPLE_SIZE;

public class Delay extends Effect implements Callable<short[]> {
    private static final double RATIO_DRY_TO_WET = 0.7;
    public static final int DEFAULT_SIZE_BUFFER = 3;
    public BufferSamples BufferSamples;

    public Delay() {
        super();
        this.BufferSamples = new BufferSamples(DEFAULT_SIZE_BUFFER);
    }

    public Delay(int sizeBufferSamples) {
        super();
        this.BufferSamples = new BufferSamples(sizeBufferSamples);
    }

    @Override
    public synchronized short[] createEffect() {
        int indexCurrentSampleDelay = this.BufferSamples.getIndexCurrentElement();
        for (int j = 0; j < SAMPLE_SIZE; j++) {
            this.inputAudioStream[j] = (short) (RATIO_DRY_TO_WET * (this.inputAudioStream[j])
                    + ((1 - RATIO_DRY_TO_WET) * this.BufferSamples.getAmplitudeSampleDelay(indexCurrentSampleDelay, j)));
        }
        this.BufferSamples.add(this.inputAudioStream);
        return this.inputAudioStream;
    }

    @Override
    public short[] call() {
        return createEffect();
    }
}

