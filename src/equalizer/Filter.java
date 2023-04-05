package equalizer;

public class Filter {
    private short[] inputSignal;
    private short[] outputSignal;
    private double[] feedbackSignal;

    public Filter(){
    }
    public short[] filtering(final short[] inputSignal) {
        this.inputSignal = inputSignal;
        this.outputSignal = new short[inputSignal.length];
        this.feedbackSignal =  new double[inputSignal.length];
        this.convolution();
        return this.outputSignal;
    }

    private void convolution() {
        double tmp;
        for(int i = 0; i <  this.inputSignal.length; i++) {
            tmp = 0;
            for(int j = 0; j < FilterInfo.COUNT_OF_COFFS; j++) {
                if (i - j >= 0) {
                    tmp += FilterInfo.COFFS_NUM[j] * this.inputSignal[i - j];
                    tmp -= FilterInfo.COFFS_DEN[j] * this.feedbackSignal[i - j];
                }
            }
            this.feedbackSignal[i] = tmp;
            this.outputSignal[i] = (short)(tmp);
        }
    }

}
