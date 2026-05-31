package lab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lab.map.MapInfo;
import lab.map.MapException;
import lab.map.MapRepository;
import lab.map.Score;

import java.io.IOException;

public class MenuController {

    private App app;

    @FXML
    private TableView<MapInfo> mapTableView;
    @FXML
    private TableColumn<MapInfo, String> mapNameColumn;
    @FXML
    private TableColumn<MapInfo, String> difficultyColumn;
    @FXML
    private TableColumn<MapInfo, Score> highScoreColumn;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    void initialize() {
        // Propojení sloupců tabulky s vlastnostmi třídy MapInfo
        mapNameColumn.setCellValueFactory(new PropertyValueFactory<>("mapName"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        highScoreColumn.setCellValueFactory(new PropertyValueFactory<>("highScore"));
    }

    // Tuto metodu volá App, aby zobrazila načtené mapy
    public void displayMaps(java.util.List<MapInfo> maps) {
        mapTableView.getItems().clear();
        mapTableView.getItems().addAll(maps);
    }

    @FXML
    void onPlayClicked(ActionEvent event) {
        MapInfo selectedMap = mapTableView.getSelectionModel().getSelectedItem();

        if (selectedMap == null) {
            showAlert("Chyba", "Nejprve musíte vybrat mapu ze seznamu.");
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
        // Manuální znovunačtení z disku
        try {
            displayMaps(MapRepository.load());
        } catch (MapException e) {
            e.printStackTrace();
            showAlert("Chyba načítání", "Nepodařilo se načíst mapy: " + e.getMessage());
        }
    }

    @FXML
    void onSaveClicked(ActionEvent event) {
        try {
            MapRepository.save(mapTableView.getItems());
            showAlert("Uloženo", "Mapy a skóre byly úspěšně uloženy.");
        } catch (MapException e) {
            e.printStackTrace();
            showAlert("Chyba ukládání", "Nepodařilo se uložit mapy: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
