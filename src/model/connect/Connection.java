package model.connect;

import com.google.gson.Gson;
import model.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Alex on 21.04.2017.
 */
public class Connection {

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    private boolean isConnected = false;
    Gson gson;

    private static Connection instance;

    public static synchronized Connection getInstance(){
        if (instance==null){
            instance = new Connection();
        }
        return instance;
    }


    private Connection(){
        try {
            socket = new Socket(Constants.serverAddress,Constants.serverPort);
            System.out.println("Connection success!");
            isConnected = true;
            socket.setSoTimeout(30000);
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            gson = new Gson();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            if (isConnected){
                sendCommand("QUIT");
                socket.close();
                in.close();
                out.flush();
                out.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String line){
        System.out.println(line);
        out.println(line);
        out.flush();
    }

    public String receive(){
        String result = null;
        try {
            result = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    public boolean sendCommand(String command){
        send(command);
        String answer = receive();
        if (answer==null) return false;
        if (answer.compareTo("GOT_"+command)==0) return true;
        else return false;
    }
    public String receiveCommand(){
        String result = null;
        try {
            result = in.readLine();
            send("GOT_"+result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    public void sendArray(double array[]){

        String line = gson.toJson(array);
        out.println(line);
        out.flush();
    }
    public double[] receiveArray(){
        try {
            String line = in.readLine();
            double result[] = gson.fromJson(line,double[].class);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public void sendDouble(double value){
        String line = gson.toJson(value);
        out.println(line);
        out.flush();
    }

    public double receiveDouble(){
        try {
            String line = in.readLine();
            return gson.fromJson(line,double.class);
        } catch (IOException e) {
            e.printStackTrace();
            return Double.parseDouble(null);
        }
    }
}
