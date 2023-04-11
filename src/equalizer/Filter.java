package equalizer;

import java.util.concurrent.Callable;

public class Filter implements Callable<short[]> {
    private short[] inputSignal;
    private short[] outputSignal;
    private double[] feedbackSignal;
    private double gain;
    private double[] coffsNumFilter;
    private double[] coffsDenFilter;

    public void settings(final short[] inputSignal, final double[] coffsNumFilter, final double[] coffsDenFilter) {
        this.inputSignal = inputSignal;
        this.coffsNumFilter = coffsNumFilter;
        this.coffsDenFilter = coffsDenFilter;
        this.outputSignal = new short[inputSignal.length];
        this.feedbackSignal =  new double[inputSignal.length];
    }

    private void convolution() {
        double tmp;
        for(int i = 0; i <  this.inputSignal.length; i++) {
            tmp = 0;
            for(int j = 0; j < FilterInfo.COUNT_OF_COFFS; j++) {
                if(i - j >= 0){
                    tmp += this.coffsNumFilter[j] * this.inputSignal[i - j];
                    tmp -= this.coffsDenFilter[j] * this.feedbackSignal[i - j];
                }
            }
            this.feedbackSignal[i] = tmp;
            this.outputSignal[i] += this.gain * (short)(tmp / 4); //делим на 4, чтобы не было перегруза на пересечении фильтров
        }
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    @Override
    public short[] call() {
        this.convolution();
        return this.outputSignal;
    }
}
