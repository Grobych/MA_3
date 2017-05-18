package model.analyze;

import data.Dynamic;
import data.Song;
import data.Status;
import javazoom.jl.decoder.*;
import model.analyze.mfcc.MFCC;
import model.analyze.rms.RMSAnalyzer;
import model.util.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static model.util.Constants.MFCCWindow;
import static model.util.Constants.RMSArrayLen;

/**
 * Created by Alex on 17.02.2017.
 */
public class Analyzer {



    public Song analyzeSongStream(Song song){
        if (song.getStatus()!= Status.SUCCESS){
            song.setStatus(Status.IN_PROCESS);
            Bitstream bitStream;
//            double frameL[] = new double[mp3FrameLen/2];
//            double frameR[] = new double[mp3FrameLen/2];
//            double spector[] = new double[AFClength];
            double monoFrame[];
            double tempFrame[];
            List RMSList = new ArrayList();
      //      FrequencyAnalyzer.initFFT(AFClength);


            List<double[]> MFCCList = new ArrayList<>();
            try {
                boolean stereo;
                int i =0;
                bitStream = new Bitstream(new FileInputStream(song.getPath()));
                Decoder decoder = new Decoder();
                SampleBuffer samples = null;
                MFCC mfcc = new MFCC(44100,MFCCWindow,16,false);
                while (true) {
                    Header header = bitStream.readFrame();
                    if (header == null){break;}
                    samples = (SampleBuffer) decoder.decodeFrame(header, bitStream); //returns the next 2304 samples
                    stereo = samples.getChannelCount()==2;
                    tempFrame = Utils.shortToDouble(samples.getBuffer());

                    //get MFCC
                    if (stereo){
                        monoFrame = Utils.mixChannelBuffer(tempFrame);
                        MFCCList.add(mfcc.processWindow(Arrays.copyOf(monoFrame,1024),0));
                    } else {
                        MFCCList.add(mfcc.processWindow(Arrays.copyOf(tempFrame,1024),0));
                    }
                    //get RMS
                    RMSList.add(RMSAnalyzer.calculateRMS(tempFrame));
//                    double[] frameSpector = FrequencyAnalyzer.getSpectrogram(frameL, AFClength);
//                    for (int j = 0; j < frameSpector.length; j++) {
//                        spector[j]+=frameSpector[j];
//                        //spector[j]/=2;
//                    }
                    bitStream.closeFrame();
                    samples.clear_buffer();
                    i++;
                }
//                for (int j = 0; j < spector.length; j++) {
//                    spector[j]=spector[j]/i;
//                }
                song.setRMS(getDynamic(RMSList));
                song.setAFC(getRoundMFCC(MFCCList));
                //song.setAFC(Arrays.copyOfRange(spector,0,AFClength/2));
            } catch (FileNotFoundException e) {
                song.setStatus(Status.FAIL);
                e.printStackTrace();
                return song;
            } catch (BitstreamException e) {
                e.printStackTrace();
                song.setStatus(Status.FAIL);
                return song;
            } catch (DecoderException e) {
                e.printStackTrace();
                song.setStatus(Status.FAIL);
                return song;
            }


        }
        song.setStatus(Status.SUCCESS);
        return song;
    }

    private double[] getRoundMFCC(List<double[]> list){
        double result[] = new double[list.get(0).length];
        for (double[] set : list) {
            for (int i = 0; i < result.length; i++) {
                result[i]+=set[i];
            }
        }
        for (int i = 0; i < result.length; i++) {
            result[i]/=list.size();
        }
        return result;
    }

    private Dynamic getDynamic(List list){
        Dynamic dynamic = new Dynamic();
        double[] RMSArray = RMSAnalyzer.getNormalizedLenghtRMS(Utils.listToDouble(list),RMSArrayLen);
        dynamic.setRMSlist(RMSArray);
        dynamic.setRMSdinamics(RMSAnalyzer.calculateDRMS(RMSArray));
        dynamic.setRMS(RMSAnalyzer.averageRMS(RMSArray));
        dynamic.setMaxDeltaRMS(RMSAnalyzer.getMaxDeltaRMS(RMSArray));
        dynamic.setAverageDeltaRMS(RMSAnalyzer.averageRMS(dynamic.getRMSdinamics()));
        return dynamic;
    }


//    public Song getBpmKey(Song song){
//        Runtime r = Runtime.getRuntime();
//
//        Process p = null;
//        String cmd= "java -jar "+bpmKeyAnalyzerPath+" \""+song.getPath()+"\" -w -o "+tempPathWindows;
//        System.out.println(cmd);
//        try
//        {
//            p = r.exec(cmd);
//            p.waitFor();
//            System.out.println("FINISH "+p.exitValue());
//            song = FileWorker.parseBpmKeyFile(tempPathWindows,song);
//        }
//        catch (Exception e){
//            Log.addMessage(e.getMessage());
//            System.out.println(e.getMessage());
//            song.setTone(null);
//            song.setBpm(0);
//            return song;
//        }
//        return song;
//    }
}