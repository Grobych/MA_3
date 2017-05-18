package model.analyze.specter;

import org.apache.commons.math3.complex.Complex;

import java.util.Arrays;

import static model.util.Constants.AFClength;
import static model.util.Constants.maxRawWave;

/**
 * Created by Alex on 03.04.2017.
 */
public class FrequencyAnalyzer {
    static FourierManager FFT = null;

    public static void initFFT(int n){
        if (FFT == null) FFT = new FourierManager(n);
    }

    public static double[] getSpectrogram(double buffer[], int N){
        //buffer = normalize(buffer);

        double temp[] = null;
        double fase[] = new double[N];
        double result[] = new double[N];
        temp = Arrays.copyOf(buffer,N);
        //temp = throwWindow(temp);
        FFT.fft(temp,fase);
        for (int i = 0; i < temp.length; i++) {
            Complex complex = new Complex(temp[i],fase[i]);
            temp[i] = Math.abs(complex.getArgument());
        }
        return Arrays.copyOf(temp,AFClength/2);
    }

    public static double[] normalize(double buffer[]){
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = buffer[i]/maxRawWave;
        }
        return buffer;
    }

    public static double[] throwWindow(double buffer[]){
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = FFT.getWindow()[i]*buffer[i];
        }
        return buffer;
    }
}
