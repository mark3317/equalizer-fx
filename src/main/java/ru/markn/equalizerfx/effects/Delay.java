package ru.markn.equalizerfx.effects;

import java.util.concurrent.Callable;

public class Delay extends Effect implements Callable<short[]> {
    private static final double RATIO_DRY_TO_WET = 0.7;
    public static final int DEFAULT_SIZE_BUFFER = 3;
    public BufferSamples BufferSamples;

    public Delay(int sizeBufferSamples) {
        this.BufferSamples = new BufferSamples(sizeBufferSamples);
    }

    @Override
    public synchronized short[] createEffect() {
        int indexCurrentSampleDelay = this.BufferSamples.getIndexCurrentElement();
        for (int j = 0; j < ru.markn.equalizerfx.effects.BufferSamples.SAMPLE_SIZE; j++) {
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

