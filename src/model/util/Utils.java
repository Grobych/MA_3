package model.util;

import org.apache.commons.math3.complex.Complex;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alex on 28.03.2017.
 */
public class Utils {
    public static Complex[] shortToComplex(short[] array){
        Complex complex[] = new Complex[array.length];
        for (int i=0; i<array.length; i++) {
            Complex number = new Complex(array[i],0.0);
            complex[i] = number;
        }
        return complex;
    }
    public static double[] shortToDouble(short[] array){
        double result[] = new double[array.length];
        for (int i=0; i<array.length; i++) {
            result[i] = (double) array[i];
        }
        return result;
    }
    public static double[] shortToDouble(short[] array, int n){
        double result[] = new double[n];
        for (int i=0; i<n; i++) {
            result[i] = (double) array[i];
        }
        return result;
    }
    public static double[] listToDouble(List list, int n){
        double result[] = new double[n];
        for (int i=0; i<n; i++) {
            result[i] = (double) list.get(i);
        }
        return result;
    }
    public static double[] listToDouble(List list){
        double result[] = new double[list.size()];
        for (int i=0; i<list.size(); i++) {
            result[i] = (double) list.get(i);
        }
        return result;
    }

    public static ArrayList<Complex> digitalToComplex(double [] list){
        ArrayList<Complex> complexList = new ArrayList<>(list.length);
        for(int i=0;i<list.length;i++)
        {
            Complex temp = new Complex(list[i],0);
            complexList.add(temp);
        }
        return complexList;
    }
    public static ArrayList<Complex> digitalToComplex(short [] list, int n){
        ArrayList<Complex> complexList = new ArrayList<>();
        for(int i=0;i<n;i++)
        {
            System.out.print("Short: " + list[i] + " ");
            double amp = list[i]/Math.pow(2,15);
            System.out.print("Div: " + amp + " ");
            Complex temp = new Complex(amp,0);
            System.out.println("Complex: " + temp);
            complexList.add(temp);
        }
        return complexList;
    }

    public static void divideBuffer(double []input, double output1[], double output2[], boolean mode){
        // true - stereo
        // false - mono
        if (mode){
            for (int i = 0; i < input.length; i++) {
                if (i % 2 == 0) output1[i / 2] = input[i];
                else output2[(i - 1) / 2] = input[i];
            }
        }else {
            output1 = Arrays.copyOf(input,input.length/2);
            output2 = Arrays.copyOfRange(input,input.length/2+1,input.length);
        }
    }
    public static void divideBuffer(double []input, double output1[], double output2[], int n, boolean mode){
        // true - stereo
        // false - mono
        if (mode){
            for (int i = 0; i < n; i++) {
                if (i % 2 == 0) output1[i / 2] = input[i];
                else output2[(i - 1) / 2] = input[i];
            }
        }else {
            output1 = Arrays.copyOf(input,n/2);
            output2 = Arrays.copyOfRange(input,n/2+1,n);
        }
    }
    public static double[] mixChannelBuffer(double left[], double right[]){
        if (left.length==right.length){
            double result[] = new double[left.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = (left[i]+right[i])/2;
            }
            return result;
        } else return null;
    }
    public static double[] mixChannelBuffer(double input[]){
        double result[] = new double[input.length/2];
        for (int i = 0; i < result.length; i++) {
            result[i]=(input[i*2]+input[i*2+1])/2;
        }
        return result;
    }

    public static void printArray(double array[]){
        for (double v : array) {
            System.out.print((v*1000)/1000+" ");
        }
        System.out.println();
    }

    public static double hypot(double a, double b) {
        double r;
        if (Math.abs(a) > Math.abs(b)) {
            r = b/a;
            r = Math.abs(a)*Math.sqrt(1+r*r);
        } else if (b != 0) {
            r = a/b;
            r = Math.abs(b)*Math.sqrt(1+r*r);
        } else {
            r = 0.0;
        }
        return r;

    }

    public static void findSongInInternet(String name){
        String URL = new String("https://music.yandex.by/search?text="+name);
        URL = URL.replace(' ','+');
        openInBrowser(URL);
    }

    public static void openInBrowser(String URL){
        try {
            Desktop.getDesktop().browse(new URI(URL));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
