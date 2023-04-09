package equalizer;

public class Filter {
    protected short[] inputSignal;
    protected short[] outputSignal;

    public Filter(){
    }
    public short[] filtering(final short[] inputSignal) {
        this.inputSignal = inputSignal;
        this.outputSignal = new short[inputSignal.length];
        this.convolution();
        return this.outputSignal;
    }

    private void convolution() {
        double tmp;
        for(int i = 0; i <  this.inputSignal.length; i++) {
            tmp = 0;
            for(int j = 0; j < FilterInfo.COUNT_OF_COFFS; j++) {
                if(i - j >= 0)
                    tmp += FilterInfo.COFFS_NUM[j] * this.inputSignal[i - j];
            }
            this.outputSignal[i] += (short)(tmp);
        }
    }
}
