package model.util;

import data.Song;

import java.io.*;
import java.util.List;

/**
 * Created by Alex on 12.01.2017.
 */
public class FileWorker {
    public static void getFilesFromFolder(File f, List<File> list){
        File[] files;
        if (f.isDirectory() && (files = f.listFiles()) != null) {
            for (File file : files) {
                getFilesFromFolder(file, list);
            }
        }
        else {
            String path = f.getPath();
            if (path.substring(path.length()-4, path.length()).equals(".mp3")) {
                list.add(f);
            }
        }
    }

//    public static Song parseBpmKeyFile(String path, Song song){
//        try {
//            InputStream fis = new FileInputStream(path);
//            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
//            BufferedReader br = new BufferedReader(isr);
//            String line = br.readLine();
//            String temp[] = line.split(";");
//
//            song.setTone(temp[1].substring(temp[1].length()-1));
//            song.setBpm((int)Float.parseFloat(temp[2]));
//
//        } catch (FileNotFoundException e) {
//            Log.addMessage(e.getMessage());
//            System.out.println(e.getMessage());
//            System.out.println("FILE EX");
//            return song;
//        } catch (IOException e) {
//            Log.addMessage(e.getMessage());
//            System.out.println(e.getMessage());
//            System.out.println("IO EX");
//            return song;
//        } catch (NumberFormatException e){
//
//        }
//        return song;
//    }


    public static void savePersonDataToFile(Song song) {
        BufferedWriter  out = null;
        String filename;
        try {
            filename = new String(Constants.tempFolderServer+song.getFullName()+".txt");
            Log.addMessage("Write to file "+filename);
            out = new BufferedWriter(new FileWriter(filename));
            out.write(String.valueOf(song.getRMS().getRMS()));
            out.newLine();
            out.write(String.valueOf(song.getRMS().getAverageDeltaRMS()));
            out.newLine();
            out.write(String.valueOf(song.getRMS().getMaxDeltaRMS()));
            out.newLine();
//            out.write(String.valueOf(song.getBpm()));
//            out.newLine();
//            out.write(song.getTone());
//            out.newLine();
            double RMS[] = song.getRMS().getRMSlist();
            for (int i = 0; i < RMS.length; i++) {
                out.write(RMS[i]+" ");
            }
            out.newLine();
//            double dRMS[] = song.getRMS().getRMSdinamics();
//            for (int i = 0; i < dRMS.length; i++) {
//                out.write(dRMS[i]+" ");
//            }
//            out.newLine();
            double MFCC[] = song.getAFC();
            for (int i = 0; i < MFCC.length; i++) {
                out.write(MFCC[i]+" ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFilesFromFolder(File folder){
        if (folder.isDirectory()){
            File[] files = folder.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    public static void serialize(List<Song> obj, OutputStream out) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(obj);
        oos.close();
    }
    private static List<Song> deserialize(InputStream in) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(in);
        List<Song> colors = (List<Song>) ois.readObject();
        ois.close();
        return colors;
    }
}
