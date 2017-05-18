package model.util.mp3Util;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Alex on 27.04.2017.
 */
public class Mp3Parser {

    private AudioInputStream stream;
    private int readed = 0;
    private byte buffer[];
    private boolean mono;
    public void read(int n){
        try {
            buffer = new byte[n*4];
            readed+=stream.read(buffer,0,n*4);
            System.out.println(readed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer=null;
    }

    public void setFile(File file) throws IOException, UnsupportedAudioFileException {
        stream = getAudioStream(file);
    }

    public static AudioInputStream getAudioStream(File file) throws IOException, UnsupportedAudioFileException {
        AudioInputStream in= AudioSystem.getAudioInputStream(file);
        AudioInputStream din = null;
        AudioFormat baseFormat = in.getFormat();
        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false);
        din = AudioSystem.getAudioInputStream(decodedFormat, in);
        return din;
    }
}
