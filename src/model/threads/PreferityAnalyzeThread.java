package model.threads;

import data.Result;
import data.Song;
import data.SongList;
import model.connect.Connection;
import model.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 11.05.2017.
 */
public class PreferityAnalyzeThread {
    public void run() {
        Connection connection = Connection.getInstance();
        connection.sendCommand("TEST");
        connection.sendCommand("LOADSONGS");
        for (Song song : SongList.getList()) {
            if (!connection.sendCommand("NEXT")) {
                Log.addMessage("Send Command Error" + song.getFullName());
                return;
            }
            connection.send(song.getName());
            connection.send(song.getArtist());
            connection.sendArray(song.getRMS().getRMSlist());
            connection.sendArray(song.getAFC());
            connection.sendDouble(song.getRMS().getRMS());
            connection.sendDouble(song.getRMS().getAverageDeltaRMS());
            connection.sendDouble(song.getRMS().getMaxDeltaRMS());
        }
        connection.sendCommand("END");

        String answer = connection.receive();
        System.out.println("received: " + answer);
        if (answer.compareTo("SUCCESS") == 0) {
            double prefRhythm = Double.parseDouble(connection.receive());
            double prefEmotional = Double.parseDouble(connection.receive());
            while (connection.receive().compareTo("NEXT") == 0) {
                String name = connection.receive();
                String author = connection.receive();
                double emotional = connection.receiveDouble();
                double rhythm = connection.receiveDouble();
                for (Song song : SongList.getList()) {
                    if ((song.getName().compareTo(name) == 0) && (song.getArtist().compareTo(author) == 0)) {
                        song.setEmotional(emotional);
                        song.setRhythm(rhythm);
                    }
                }
            }
            System.out.println(prefRhythm + " " + prefEmotional);
            for (Song song : SongList.getList()) {
                System.out.println(song.getFullName());
                System.out.println(song.getEmotional());
                System.out.println(song.getRhythm());
            }
            answer = connection.receive();
            List<String> recommendationList = new ArrayList<>();


            if (answer.compareTo("SIMILAR") == 0) {
                while (true) {
                    answer = connection.receive();
                    System.out.println(answer);
                    if (answer.compareTo("END") != 0) {
                        recommendationList.add(answer);
                    } else break;
                }


            }
            Result.setSongs(recommendationList);
            Result.setPrefEmotional(prefEmotional);
            Result.setPrefRhythm(prefRhythm);
        }

    }
}
