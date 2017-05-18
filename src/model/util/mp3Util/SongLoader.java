package model.util.mp3Util;

import data.Song;
import data.SongList;
import model.util.Log;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Alex on 13.01.2017.
 */
public class SongLoader {


    private static Song parseMP3(MP3File mp3){
        Song song = new Song();
        boolean ID1 = mp3.hasID3v1Tag();
        boolean ID2 = mp3.hasID3v2Tag();
        //System.out.println("Ver 1: "+ID1+" \nVer2: "+ID2);

        if (ID2){
            song.setArtist(mp3.getID3v2Tag().getLeadArtist());
            song.setName(mp3.getID3v2Tag().getSongTitle());
           // song.setAlbum(mp3.getID3v2Tag().getAlbumTitle());
        } else if (ID1){
            song.setArtist(mp3.getID3v1Tag().getArtist());
            song.setName(mp3.getID3v1Tag().getSongTitle());
           // song.setAlbum(mp3.getID3v1Tag().getAlbumTitle());
        }else
        {
            Log.addMessage("No tag in "+mp3.getMp3file().getPath());
            song.setFullName(mp3.getMp3file().getName());
            System.out.println("No tag!");
        }

        System.out.println("load name: "+song.getName());
        return song;
    }


    public static Song loadSong(String path){
        Song song;// = new Song();

        try {
            Log.addMessage("Try to parse "+path);
            MP3File mp3 = new MP3File(path); /// !!!! some song get StringIndexOutOfBand Ex (-4)  !!!!
            song = parseMP3(mp3);
        } catch (IOException e) {
            System.out.println("File open error!");
            return null;
            //e.printStackTrace();
        } catch (TagException e) {
            System.out.println("No tag in file!");
            return null;
            //e.printStackTrace();
        }
        catch (StringIndexOutOfBoundsException e){
            Log.addMessage("StringIndexOutOfBoundsException  in "+path);
            System.out.println("ERROR   \n"+path+" Len: "+path.length());
            System.out.println(e.getMessage());
            //e.printStackTrace();
            return null;
        }
        song.setPath(path);
        return song;
    }

    public static void loadSongList(List<File> fileList){
        for (File file : fileList){
            Song song = loadSong(file.getAbsolutePath());
            if (song!=null) SongList.add(song);
        }
    }
}
