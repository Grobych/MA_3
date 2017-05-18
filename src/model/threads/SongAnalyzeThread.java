package model.threads;

import data.Song;
import data.SongList;
import data.Status;
import model.analyze.Analyzer;

/**
 * Created by Alex on 01.04.2017.
 */
public class SongAnalyzeThread implements Runnable {

    @Override
    public void run() {

        if (SongList.getList().size()==0) return;
        Analyzer analyzer = new Analyzer();
        for (int i=0; i<SongList.getList().size(); i++) {
            Song song = SongList.getList().get(i);
            if (song.getStatus()== Status.WAITING){
                song = analyzer.analyzeSongStream(song);
                //song = analyzer.getBpmKey(song);
                SongList.getList().set(i,song);
                System.out.println("CHECK");
//                FileWorker.savePersonDataToFile(song);
            }

        }
    }
}
