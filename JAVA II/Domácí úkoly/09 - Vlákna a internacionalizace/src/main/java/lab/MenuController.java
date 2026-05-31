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
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    @FXML
    private ResourceBundle resources;

    private Map<String, List<MapInfo>> groupedMaps;

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
        // Seskupení map podle jména písničky pomocí streamu
        this.groupedMaps = maps.stream()
            .collect(Collectors.groupingBy(MapInfo::getMapName));
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
            showAlert(text("alert.error"), text("menu.selectDifficulty"));
            return;
        }

        try {
            app.switchToGame(selectedMap);
        } catch (IOException e) {
            log.error("Nepodařilo se načíst herní okno pro mapu {}.", selectedMap.getId(), e);
            showAlert(text("alert.criticalError"), text("menu.gameLoadError") + e.getMessage());
        }
    }

    @FXML
    void onLoadClicked(ActionEvent event) {
        try {
            displayMaps(MapRepository.load());
        } catch (MapException e) {
            log.error("Nepodařilo se načíst mapy z databáze.", e);
            showAlert(text("alert.loadError"), text("menu.mapsLoadError") + e.getMessage());
        }
    }

    @FXML
    void onSaveClicked(ActionEvent event) {
        showAlert(text("alert.info"), text("menu.autoSaveInfo"));
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
            showAlert(text("alert.error"), text("menu.settingsOpenError") + e.getMessage());
        }
    }

    private String text(String key) {
        return resources.getString(key);
    }
}
