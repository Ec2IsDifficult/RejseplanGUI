package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.*;


public class Controller {

    @FXML
    TextArea textArea;
    @FXML
    TextField inputFieldStartStation;
    @FXML
    TextField inputFieldEndStation;
    @FXML
    ComboBox<String> comboBoxStartTime;
    @FXML
    ComboBox<String> comboBoxEndTime;

    private ObservableList<String> timeList = FXCollections.observableArrayList("00.00", "00.30", "01.00", "01.30",
            "02.00", "02.30", "03.00", "03.30", "04.00", "04.30", "05.00", "05.30", "06.00", "06.30", "07.00", "07.30", "08.00", "08.30",
            "09.00", "09.30", "10.00", "10.30", "11.00", "11.30", "12.00", "12.30", "13.00", "13.30", "14.00", "14.30",
            "15.00", "15.30", "16.00", "16.30", "17.00", "17.30", "18.00", "18.30", "19.00", "19.30", "20.00", "20.30",
            "21.00", "21.30", "22.00", "22.30", "23.00", "23.30");

    //initialized the comboboxes by inserting information contained in a observablearraylist.
    public void initialize() {
        comboBoxStartTime.getItems().addAll(timeList);
        comboBoxEndTime.getItems().addAll(timeList);
    }

    public void StartTime() {
        return;
    }

    public void EndTime() {
        return;
    }

    public void clearButton(ActionEvent actionEvent) {
        inputFieldStartStation.clear();
        inputFieldEndStation.clear();
        textArea.clear();
    }

    //Detects action event from the GUI
    public void button(ActionEvent actionevent) throws SQLException {

        //Creates a connection to database via the connection class. this is treated as an object.
        ConnectionClass connectionClass = new ConnectionClass();
        Connection conn = connectionClass.getConnection();

        //Assign input from GUI into variables later to be inserted into the PreparedStatement.
        float d = Float.parseFloat(comboBoxEndTime.getValue());

        String string1 =  inputFieldStartStation.getText();
        String string2 =  inputFieldEndStation.getText();

        //Insert variables into the prepared statement.
        PreparedStatement returnValidRoute = returnValidInformation(string1, d ,
                string2,conn);

        //Produce resultset via executing SQL query in database.
        ResultSet res = returnValidRoute.executeQuery();

        //Get data from the result via the get method. Combines all these in one result and appends it on the GUI.
        while (res != null & res.next()) {
            float resultStartTime = res.getFloat(1);
            String resultStartStation = res.getString(2);
            float resultArrivalTime = res.getFloat(3);
            String resultEndStation = res.getString(4);
            String results = "Tag toget klokken: " + resultStartTime + " \nfra stationen " + resultStartStation +
                    " \n" + "Du ankommer klokken: " + resultArrivalTime + " \npå " + resultEndStation + " \n";
            textArea.appendText(results + "\n");
        }

        //Close resultset and connection.
        res.close();
        conn.close();
    }

    //Prepared statement that extracts information based on the String called "query".
    public PreparedStatement returnValidInformation(String StartStationName, float arrivalTime, String EndStationName, Connection conn) throws SQLException {
        PreparedStatement departureFromStart = null;
        String query = "Select ds.StartTime, sds.StationName, ar.StartTime, sar.StationName " +
                "From Times as ar " +
                "INNER JOIN " +
                "Times AS ds ON ar.TrainID = ds.TrainID " +
                "INNER JOIN " +
                "Stations AS sar ON ar.StationID = sar.StationID " +
                "INNER JOIN " +
                "Stations AS sds ON ds.StationID = sds.StationID " +
                "WHERE sar.StationName = '"+ EndStationName+ "'" + "And ar.StartTime <= '" + arrivalTime + "'" + "And ds.StartTime <= ar.StartTime " +
                "And sds.StationName = '" + StartStationName + "'";

        departureFromStart = conn.prepareStatement(query);
        return departureFromStart;
    }
}
