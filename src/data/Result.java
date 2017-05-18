package data;

import java.util.List;

/**
 * Created by Alex on 11.05.2017.
 */
public class Result {
    private static List<String> songs;
    private static double prefRhythm;
    private static double prefEmotional;

    public static double getPrefEmotional() {
        return prefEmotional;
    }

    public static double getPrefRhythm() {
        return prefRhythm;
    }

    public static List<String> getSongs() {
        return songs;
    }

    public static void setPrefEmotional(double prefEmotional) {
        Result.prefEmotional = prefEmotional;
    }

    public static void setPrefRhythm(double prefRhythm) {
        Result.prefRhythm = prefRhythm;
    }

    public static void setSongs(List<String> songs) {
        Result.songs = songs;
    }
}
