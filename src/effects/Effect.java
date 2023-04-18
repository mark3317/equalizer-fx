package effects;

import java.util.concurrent.ExecutionException;

public abstract class Effect {
    protected short[] inputAudioStream;

    public abstract short[] createEffect() throws InterruptedException, ExecutionException;
}
