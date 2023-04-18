package effects;

public class Overdrive extends Effect {
    private static final short UPPER_BOUND = 2600;
    private static final short LOWER_BOUND = -2600;
    private final static double COFF_COMPRESSION = 0.1;

    public Overdrive() {
    }

    public void setInputSampleStream(short[] inputAudioStream) {
        this.inputAudioStream = inputAudioStream;
    }

    @Override
    public synchronized short[] createEffect() {
        for (int i = 0; i < this.inputAudioStream.length; i++) {
            if (this.inputAudioStream[i] > Overdrive.UPPER_BOUND)
                this.inputAudioStream[i] = (short) ((Overdrive.UPPER_BOUND) + (this.inputAudioStream[i] * COFF_COMPRESSION));
            else if (this.inputAudioStream[i] < Overdrive.LOWER_BOUND)
                this.inputAudioStream[i] = (short) ((Overdrive.LOWER_BOUND) + (this.inputAudioStream[i] * COFF_COMPRESSION));
        }
        return this.inputAudioStream;
    }
}