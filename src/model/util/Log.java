package model.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Alex on 10.02.2017.
 */
public class Log  {
    private static FileWriter out;

    static {
        try {
            File file = new File(Constants.logFileName);
            if (file.exists()) file.delete();
            out = new FileWriter(Constants.logFileName, false);
            System.out.println("Log created");
            out.write("Log created");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void close(){
        try {
            //out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("Fail to close log");
            System.out.println(e.getMessage());
        }
    }

    public static void addMessage(String message){
        try {
            System.out.println("Write to log: "+ message);

            out.append("\r\n");
            out.append(message);
            out.flush();
        } catch (IOException e) {
            System.out.println("Fail to append log");
            System.out.println(e.getMessage());
        }
    }
}
