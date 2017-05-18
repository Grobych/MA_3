package model.threads;

import model.util.mp3Util.SongLoader;

import java.io.File;
import java.util.List;

/**
 * Created by Alex on 19.01.2017.
 */
public class SongLoaderThread implements Runnable {

    List<File> files;

    public SongLoaderThread(List<File> files){
        this.files = files;
    }

    @Override
    public void run() {
        SongLoader.loadSongList(files);
    }
}
