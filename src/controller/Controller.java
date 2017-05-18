package controller;

import data.Result;
import data.Song;
import data.SongList;
import data.Status;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import model.analyze.rms.RMSAnalyzer;
import model.connect.Connection;
import model.threads.PreferityAnalyzeThread;
import model.threads.SongAnalyzeThread;
import model.threads.SongLoaderThread;
import model.util.FileWorker;
import model.util.Log;
import model.util.Utils;
import org.apache.commons.math3.complex.Complex;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable, Closeable {
    FileWorker fileWorker = new FileWorker();
    FileChooser fileChooser = new FileChooser();
    DirectoryChooser chooser = new DirectoryChooser();
    @FXML
    SplitPane mainSplitPane;
    @FXML
    Tab physicalParamTab, resultTab;
    @FXML
    TabPane tabPane;
    @FXML
    Button loadFolderButton, loadFileButton;
    @FXML
    TableColumn tableNameColumn, tableStateColumn;
    @FXML
    TableColumn<String,String> resultSongNameColumn;
    @FXML
    TableView<Song> songTable;
    @FXML
    TableView<String> resultSongTable;
    @FXML
    LineChart <String, Double> ACFChart, RMSChart;
    @FXML
    Label RMSLabel,MaxDeltaRMSLabel, AverageDeltaRMSLabel, toneLabel, tempLabel;
    @FXML
    Slider RhythmSlider, EmotionalSlider, FESlider, FRSlider;
    @FXML
    TextField emotionalFeed, rhythmFeed;


    XYChart.Series RMSseries = new XYChart.Series();
    XYChart.Series RMSRseries = new XYChart.Series();
    XYChart.Series AFCSeries = new XYChart.Series();

    @FXML
    public void onResize(){
        System.out.println("CHECK");
        double divider = 0.3;
        mainSplitPane.setDividerPositions(divider);
    }
    @FXML
    public void loadFolderButtonClick(){
        chooser.setTitle("Choose Folder");
        File defaultDirectory = new File("C:\\");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory==null) return;
        List<File> files = new ArrayList<>();
        FileWorker.getFilesFromFolder(selectedDirectory,files);
        SongLoaderThread slt = new SongLoaderThread(files);
        Thread loadThread = new Thread(slt);
        loadThread.start();
    }

    @FXML
    public void loadFileButtonClick() {
        fileChooser.setTitle("Choose File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3"));
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files==null) {
            Log.addMessage("No files");
            return;
        }
        SongLoaderThread slt = new SongLoaderThread(files);
        Thread loadThread = new Thread(slt);
        loadThread.start();
    }

    public void showAlert(String alertMessage){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    @FXML
    public void showResults(double prefR, double prefE, List<String> list){
        RhythmSlider.setMin(-1);
        RhythmSlider.setMax(1);
        RhythmSlider.setValue(prefR);
        EmotionalSlider.setMin(-1);
        EmotionalSlider.setMax(1);
        EmotionalSlider.setValue(prefE);
        resultSongTable.refresh();
        //resultSongTable.setEditable(false);
        System.out.println(list.size());
        if (list.size()!=0){
            resultSongNameColumn.setCellValueFactory(cellData ->
                    new ReadOnlyStringWrapper(cellData.getValue()));
            for (String str : list) {
                resultSongTable.getItems().add(str);
            }
        } else {
            showAlert("No similar songs! :(");
        }
        SingleSelectionModel<Tab> model = tabPane.getSelectionModel();
        model.select(resultTab);
    }

    @FXML
    public void resultTableClick(){
        String name = resultSongTable.getSelectionModel().getSelectedItem();
        Utils.findSongInInternet(name);
    }

    @FXML
    public void resultButtonClick(){
        for (Song song : SongList.getList()) {
            if (song.getStatus()!= Status.SUCCESS) {
                showAlert("Not all songs has been analyzed!");
                return;
            }
        }
        PreferityAnalyzeThread preferityAnalyzeThread = new PreferityAnalyzeThread();
        preferityAnalyzeThread.run();

        showResults(Result.getPrefRhythm(),Result.getPrefEmotional(),Result.getSongs());
    }



    @FXML
    public void initialisationTable(){
        songTable.refresh();
        tableNameColumn.setCellValueFactory(new PropertyValueFactory<Song,Integer>("FullName"));
        tableStateColumn.setCellValueFactory(new PropertyValueFactory<Song,Integer>("Status"));
        songTable.setItems(SongList.getList());
    }
    @FXML
    public void showSongInfo(){
        Song song = songTable.getSelectionModel().getSelectedItem();
        if (song==null) return;
        if (song.getRMS()!=null){
            RMSLabel.setText(RMSAnalyzer.getRMSString(song.getRMS()));
            MaxDeltaRMSLabel.setText(RMSAnalyzer.getMaxDeltaRMSString(song.getRMS()));
            AverageDeltaRMSLabel.setText(RMSAnalyzer.getAverageDeltaRMSString(song.getRMS()));
            ObservableList points = getChartPoints(song.getRMS().getRMSlist());
            if (RMSseries!=null)RMSseries.getData().removeAll();
            RMSseries.setData(points);
        }
        if (song.getAFC()!=null){
            ObservableList points = getChartPoints(song.getAFC());
            if (AFCSeries!=null) AFCSeries.getData().removeAll();
            AFCSeries.setData(points);
        }
//        if (song.getTone()!=null){
//            toneLabel.setText(song.getTone());
//        }
//        if (song.getBpm()!=0){
//            tempLabel.setText(String.valueOf(song.getBpm()));
//        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Log.addMessage("Running program...");
        initialisationTable();
        RMSChart.getData().add(RMSseries);
        RMSChart.getData().add(RMSRseries);
        ACFChart.getData().add(AFCSeries);
    }

    @Override
    public void close() throws IOException {
        Log.close();
        Connection.getInstance().closeConnection();
    }

    @FXML
    public void runButtonClick() {
        SongAnalyzeThread analyzer = new SongAnalyzeThread();
        Thread thread = new Thread(analyzer);
        thread.start();
//        SongAnalyzeThread analyzer2 = new SongAnalyzeThread();
//        Thread thread2 = new Thread(analyzer2);
//        thread2.start();
    }

    private ObservableList<XYChart.Data> getChartPoints(double [] cx) {

        ObservableList<XYChart.Data> points = FXCollections.observableArrayList();

        for (int i = 0; i < cx.length; i++) {
            points.add(createPoint(i, cx[i], false));
        }

        return points;
    }
    private ObservableList<XYChart.Data> getChartPoints(ArrayList<Complex> cx) {

        ObservableList<XYChart.Data> points = FXCollections.observableArrayList();

        for (int i = 0; i < cx.size(); i++) {
            Complex c = cx.get(i);
            points.add(createPoint(i, c.abs(), false));
        }

        return points;
    }
    private XYChart.Data createPoint(double x, double y, boolean isVisible) {

        XYChart.Data<Object, Object> point = new XYChart.Data<Object, Object>(x,y);

        Rectangle rect = new Rectangle(4, 4);
        rect.setVisible(isVisible);
        point.setNode(rect);

        return point;
    }

    @FXML
    public void agreeImageClick(MouseEvent mouseEvent) {
        Song song = songTable.getSelectionModel().getSelectedItem();
        Connection connection = Connection.getInstance();
        connection.sendCommand("LOADTEST");
        connection.send(song.getName());
        connection.send(song.getArtist());
        System.out.println("OK");
        showAlert("You feedback has been send to server!");
    }
    @FXML
    public void disagreeImageClick(MouseEvent mouseEvent) {
        Song song = songTable.getSelectionModel().getSelectedItem();
        Connection connection = Connection.getInstance();
        connection.sendCommand("LOADFEEDBACK");
        connection.send(song.getName());
        connection.send(song.getArtist());
        double e = Double.parseDouble(emotionalFeed.getText());
        double r = Double.parseDouble(rhythmFeed.getText());
        connection.sendDouble(e);
        connection.sendDouble(r);
        System.out.println("not OK");
        showAlert("You feedback has been send to server!");
    }

    @FXML
    public void showInfo(MouseEvent mouseEvent) {
        switch (tabPane.getSelectionModel().getSelectedIndex()){
            case 0 : showSongInfo(); break;
            case 1 : showFeedbackInfo(); break;
            default: break;
        }
    }

    @FXML
    private void showFeedbackInfo() {
        Song song = songTable.getSelectionModel().getSelectedItem();
        if (song==null||song.getStatus()!=Status.SUCCESS) return;

        FESlider.setMax(1);
        FESlider.setMin(-1);
        FRSlider.setMax(1);
        FRSlider.setMin(-1);

        FESlider.setValue(song.getEmotional());
        FRSlider.setValue(song.getRhythm());
    }

}
