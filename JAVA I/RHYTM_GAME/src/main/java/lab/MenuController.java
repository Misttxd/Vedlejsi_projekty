package lab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lab.map.MapInfo;
import lab.map.MapException;
import lab.map.MapRepository;
import lab.map.Score;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MenuController {

    private App app;

    @FXML
    private TableView<MapInfo> mapTableView;

    @FXML
    private TableColumn<MapInfo, String> difficultyColumn;
    @FXML
    private TableColumn<MapInfo, Integer> noteCountColumn;
    @FXML
    private TableColumn<MapInfo, String> infoColumn;
    @FXML
    private TableColumn<MapInfo, Score> highScoreColumn;
    @FXML
    private ListView<String> songListView;

    private Map<String, List<MapInfo>> groupedMaps;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    void initialize() {
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        noteCountColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedNoteCount"));
        infoColumn.setCellValueFactory(new PropertyValueFactory<>("info"));
        highScoreColumn.setCellValueFactory(new PropertyValueFactory<>("highScore"));

        songListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && groupedMaps != null) {
                mapTableView.getItems().clear();
                mapTableView.getItems().addAll(groupedMaps.get(newValue));
            }
        });
    }

    public void displayMaps(java.util.List<MapInfo> maps) {
        this.groupedMaps = maps.stream().collect(Collectors.groupingBy(MapInfo::getMapName));
        
        songListView.getItems().clear();
        songListView.getItems().addAll(groupedMaps.keySet());

        if (!songListView.getItems().isEmpty()) {
            songListView.getSelectionModel().select(0);
        }
    }

    @FXML
    void onPlayClicked(ActionEvent event) {
        MapInfo selectedMap = mapTableView.getSelectionModel().getSelectedItem();

        if (selectedMap == null) {
            showAlert("Chyba", "Vyberte si konkrétní obtížnost v pravé tabulce.");
            return;
        }

        try {
            app.switchToGame(selectedMap);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Kritická chyba", "Nepodařilo se načíst herní okno: " + e.getMessage());
        }
    }

    @FXML
    void onLoadClicked(ActionEvent event) {
        try {
            displayMaps(MapRepository.load());
        } catch (MapException e) {
            e.printStackTrace();
            showAlert("Chyba načítání", "Nepodařilo se načíst mapy: " + e.getMessage());
        }
    }

    @FXML
    void onSaveClicked(ActionEvent event) {
        showAlert("Info", "Data jsou v DB ukládána automaticky.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
