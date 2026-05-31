package lab;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lab.map.MapInfo;
import lab.map.Score;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class MenuController {

    @Setter
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

    @FXML
    void initialize() {
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        noteCountColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedNoteCount"));
        infoColumn.setCellValueFactory(new PropertyValueFactory<>("info"));
        highScoreColumn.setCellValueFactory(new PropertyValueFactory<>("highScore"));

        songListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null && groupedMaps != null) {
                    mapTableView.getItems().clear();
                    List<MapInfo> mapsForSong = groupedMaps.get(newValue);
                    if (mapsForSong != null) {
                        mapTableView.getItems().addAll(mapsForSong);
                    }
                }
            }
        });
    }

    public void displayMaps(java.util.List<MapInfo> maps) {
        this.groupedMaps = new LinkedHashMap<>();
        for (MapInfo map : maps) {
            String songName = map.getMapName();
            List<MapInfo> mapsForSong = groupedMaps.get(songName);
            if (mapsForSong == null) {
                mapsForSong = new ArrayList<>();
                groupedMaps.put(songName, mapsForSong);
            }
            mapsForSong.add(map);
        }
        log.debug("Zobrazuji {} map seskupených do {} skladeb.", maps.size(), groupedMaps.size());

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
            log.error("Nepodařilo se načíst herní okno pro mapu {}.", selectedMap.getId(), e);
            showAlert("Kritická chyba", "Nepodařilo se načíst herní okno: " + e.getMessage());
        }
    }

    @FXML
    void onLoadClicked(ActionEvent event) {
        displayMaps(app.loadMapsFromServer());
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

    @FXML
    void onSettingsClicked(ActionEvent event) {
        try {
            app.switchToSettings();
        } catch (Exception e) {
            log.error("Nepodařilo se otevřít nastavení z menu.", e);
            showAlert("Chyba", "Nepodařilo se otevřít nastavení: " + e.getMessage());
        }
    }
}
