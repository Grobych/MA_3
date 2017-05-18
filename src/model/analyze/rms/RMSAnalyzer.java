package model.analyze.rms;

import data.Dynamic;

import java.text.DecimalFormat;
import java.util.Arrays;

import static model.util.Constants.maxRawWave;

/**
 * Created by Alex on 31.03.2017.
 */
public class RMSAnalyzer {
    public static double calculateRMS(double[] buffer){
        double result = 0.0D;

        for (int i =0; i< buffer.length; i++){
            result+=Math.pow(buffer[i],2.0);
        }
        result/=buffer.length;
        result = Math.sqrt(result);
//        result = 20 * Math.log10(result/32678);
        return result;
    }

    public static double getMaxDeltaRMS(double[] array){
        double max = 0.0D;
        for (int i = 1; i<array.length; i++){
            if (Math.abs(array[i]-array[i-1])>max) {
                max = Math.abs(array[i]-array[i-1]);
            }
        }
        return max;
    }

    public static double[] calculateDRMS(double[] array){
        double result[] = new double[array.length];
        for (int i=0; i < array.length - 1; i++){
            result[i] = Math.abs(array[i+1] - array[i]);
        }
        return result;
    }

    public static double averageRMS(double RMS[]){
        double result = 0.0D;
        for (int i = 0; i< RMS.length; i++){
            result+=RMS[i];
        }
        result/=RMS.length;
        return result;
    }

    public static Dynamic getDynamic(double [] buffer, int frameLenght){
        Dynamic result = new Dynamic();
        int n = buffer.length / frameLenght ;
        System.out.println("Buffer: "+buffer.length);
        System.out.println("N: "+n);
        double max = 0;
        double average = 0;
        double dRMS[] = new double[n-1];
        double RMS[] = new double[n];
        for (int i = 0; i<n; i++){
            double frame[] = Arrays.copyOfRange(buffer,i*frameLenght,(i+1)*frameLenght);
            RMS[i] = RMSAnalyzer.calculateRMS(frame);
            if (i!=0){
                double delta = Math.abs(RMS[i]-RMS[i-1]);
                dRMS[i-1] = delta;
                if (delta>max) max = delta;
                average+=delta;
                average/=2;
//                System.out.println("R  "+RMS[i]);
//                System.out.println("dR "+delta);
            }
        }
        result.setRMS(averageRMS(RMS));
        result.setAverageDeltaRMS(average);
        result.setMaxDeltaRMS(max);
        result.setRMSlist(RMS);
        result.setRMSdinamics(dRMS);
        return result;
    }

    public static double getAsDB(double value){
        return 20 * Math.log10(value/maxRawWave);
    }

    public static double[] getNormalizedLenghtRMS(double[] RMS, int length){
        int step = RMS.length/length;
        int i =0;

        double result[] = new double[length];
        while (i<length){
            int temp = 0;
            for (int j = 0; j < step; j++) {
                temp+=RMS[i*step+j];
            }
            temp/=step;
            result[i] = temp;
            i++;
        }

        return result;
    }

    public static String getRMSString(Dynamic RMS){
        String pattern = "##0.0000";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String format = decimalFormat.format(RMSAnalyzer.getAsDB(RMS.getRMS()));
        format =format.concat("dB");
        return new String(format);
    }

    public static String getMaxDeltaRMSString(Dynamic RMS){
        String pattern = "##0.0000";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String format = decimalFormat.format(RMSAnalyzer.getAsDB(RMS.getMaxDeltaRMS()));
        format = format.concat("dB");
        return new String(format);
    }

    public static String getAverageDeltaRMSString(Dynamic RMS){
        String pattern = "##0.0000";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String format = decimalFormat.format(RMSAnalyzer.getAsDB(RMS.getAverageDeltaRMS()));
        format = format.concat("dB");
        return new String(format);
    }

}
