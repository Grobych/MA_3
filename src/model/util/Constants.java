package model.util;

import java.net.InetAddress;

/**
 * Created by Alex on 12.01.2017.
 */
public class Constants {

    public final static int AFClength = 1024;
    public final static String logFileName = new String("log.txt");
    public final static int mp3FrameLen = 2304;
    public final static int maxRawWave = 32678;
    public final static int RMSArrayLen = 200;
    public final static int MFCCWindow = 1024;
    public final static String tempPathWindows = new String("C:\\ProgramData\\bpmkey.txt");
    public final static String tempPathLinux = new String("\\home\\bpmkey.txt");
    public final static String bpmKeyAnalyzerPath = new String("lib\\BPMKeyAnalyzer\\TrackAnalyzer.jar");
    public final static String tempFolderServer = new String("C:\\Users\\Alex\\IdeaProjects\\MusicAnalyzer_Server\\test\\");
    public final static int serverPort = 19999;
    public final static InetAddress serverAddress = InetAddress.getLoopbackAddress();
}
