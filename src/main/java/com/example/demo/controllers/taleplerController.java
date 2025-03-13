package com.example.demo.controllers;

import com.example.demo.nesneler.Talep;
import com.example.demo.nesneler.Personel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class taleplerController {

    @FXML
    private TableView<Talep> tableView;

    @FXML
    private TableColumn<Talep, String> idColumn;

    @FXML
    private TableColumn<Talep, String> isimColumn;

    @FXML
    private TableColumn<Talep, String> soyisimColumn;

    @FXML
    private TableColumn<Talep, String> talepColumn;

    @FXML
    private TextField idTextField;

    private ObservableList<Talep> talepler;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static final String TALEPLER_JSON_PATH = "talepler.json";
    private static final String PERSONEL_JSON_PATH = "personel.json";

    @FXML
    public void initialize() {
        // Kolonları ilgili Talep alanlarıyla bağlama
        idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        isimColumn.setCellValueFactory(new PropertyValueFactory<>("isim"));
        soyisimColumn.setCellValueFactory(new PropertyValueFactory<>("soyisim"));

        // Talep metni: Zamsa "%... maaş zammı", izinse "... gün izin"
        talepColumn.setCellValueFactory(cellData -> {
            Talep talep = cellData.getValue();
            String talepText = talep.getType().equalsIgnoreCase("zam")
                    ? "%" + talep.getValue() + " maaş zammı"
                    : talep.getValue() + " gün izin";
            return new javafx.beans.property.SimpleStringProperty(talepText);
        });

        // JSON'dan talepleri yükleyip tabloya atıyoruz
        talepler = FXCollections.observableArrayList(loadTaleplerFromJson());
        tableView.setItems(talepler);
    }

    private List<Talep> loadTaleplerFromJson() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(TALEPLER_JSON_PATH)) {
            Type listType = new TypeToken<List<Talep>>() {}.getType();
            List<Talep> talepler = gson.fromJson(reader, listType);

            // Debug amaçlı konsola yazdırmak isterseniz
            for (Talep talep : talepler) {
                System.out.println("Yüklenen Talep: "
                        + talep.getUserId() + ", "
                        + talep.getIsim() + ", "
                        + talep.getSoyisim() + ", "
                        + talep.getType() + ", "
                        + talep.getValue());
            }

            return talepler != null ? talepler : List.of();
        } catch (IOException e) {
            showErrorAlert("Hata", "Talepler JSON dosyası okunamadı!");
            return List.of();
        }
    }

    @FXML
    public void onaylaButtonClick(ActionEvent event) {
        Talep selectedTalep = getSelectedTalep();
        if (selectedTalep == null) {
            showErrorAlert("Hata", "Lütfen bir talep seçin veya geçerli bir ID girin.");
            return;
        }

        if (applyTalepToPersonel(selectedTalep)) {
            talepler.remove(selectedTalep);
            updateTaleplerJson();
            tableView.refresh();
            showInfoAlert("Başarılı", "Talep başarıyla onaylandı ve personel bilgisi güncellendi.");
        } else {
            showErrorAlert("Hata", "Talep onaylanamadı, ilgili personel bulunamadı!");
        }
    }

    @FXML
    public void reddetButtonClick(ActionEvent event) {
        Talep selectedTalep = getSelectedTalep();
        if (selectedTalep == null) {
            showErrorAlert("Hata", "Lütfen bir talep seçin veya geçerli bir ID girin.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Talebi Reddet");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Bu talebi reddetmek istediğinizden emin misiniz?");
        if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            talepler.remove(selectedTalep);
            updateTaleplerJson();
            tableView.refresh();
            showInfoAlert("Başarılı", "Talep başarıyla reddedildi!");
        }
    }

    private boolean applyTalepToPersonel(Talep talep) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(PERSONEL_JSON_PATH)) {
            Type listType = new TypeToken<List<Personel>>() {}.getType();
            List<Personel> personelListesi = gson.fromJson(reader, listType);

            for (Personel personel : personelListesi) {
                // Talep'in userId'si, Personel'in "id" değerine eşitse...
                if (String.valueOf(personel.getId()).equals(talep.getUserId())) {
                    // Zam ise maaşı artır
                    if (talep.getType().equalsIgnoreCase("zam")) {
                        double zam = Double.parseDouble(talep.getValue());
                        personel.setMaas(personel.getMaas() * (1 + zam / 100));
                    }
                    // İzin ise personelin mevcut izin gününü düş
                    else if (talep.getType().equalsIgnoreCase("izin")) {
                        int kullanilanIzin = Integer.parseInt(talep.getValue());
                        int mevcutIzin = personel.getIzin_gunu();

                        if (mevcutIzin >= kullanilanIzin) {
                            personel.setIzin_gunu(mevcutIzin - kullanilanIzin);
                        } else {
                            showErrorAlert("Hata", "İzin gününüz yetersiz!");
                            return false;
                        }
                    }
                    // Personel kaydını güncelle
                    updatePersonelJson(personelListesi);
                    return true;
                }
            }
        } catch (IOException e) {
            showErrorAlert("Hata", "Personel JSON dosyası okunamadı!");
        }
        return false;
    }

    private void updatePersonelJson(List<Personel> personelListesi) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(PERSONEL_JSON_PATH)) {
            gson.toJson(personelListesi, writer);
        } catch (IOException e) {
            showErrorAlert("Hata", "Personel JSON dosyası güncellenemedi!");
        }
    }

    /**
     * Bir talep seçili mi? Yoksa ID girilerek mi talep bulunuyor?
     * Birden fazla talep varsa ChoiceDialog açar (Talep.toString() sayesinde,
     * ekranda kod yerine "3 gün izin" / "%6 maaş zammı" metinleri gösterilir).
     */
    private Talep getSelectedTalep() {
        // Önce tabloda seçili talep var mı?
        Talep selectedTalep = tableView.getSelectionModel().getSelectedItem();

        // Tabloda seçim yok ama ID yazıldıysa
        if (selectedTalep == null && !idTextField.getText().isEmpty()) {
            String id = idTextField.getText();
            List<Talep> matchedTalepler = talepler.stream()
                    .filter(t -> t.getUserId().equals(id))
                    .collect(Collectors.toList());

            // Birden fazla talep bulunduysa ChoiceDialog aç
            if (matchedTalepler.size() > 1) {
                ChoiceDialog<Talep> dialog = new ChoiceDialog<>(matchedTalepler.get(0), matchedTalepler);
                dialog.setTitle("Talep Seçimi");
                dialog.setHeaderText("Birden fazla talep bulundu.");
                dialog.setContentText("Lütfen bir talep seçin:");

                // Artık setConverter'a gerek yok, Talep.toString() kullanacak
                Optional<Talep> result = dialog.showAndWait();
                return result.orElse(null);

            } else if (matchedTalepler.size() == 1) {
                // Tek eşleşme varsa doğrudan dön
                return matchedTalepler.get(0);
            }
        }
        return selectedTalep;
    }

    private void updateTaleplerJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(TALEPLER_JSON_PATH)) {
            gson.toJson(talepler, writer);
        } catch (IOException e) {
            showErrorAlert("Hata", "Talepler JSON dosyası güncellenemedi!");
        }
    }

    @FXML
    public void anaMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/yonetici_menu.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Yardımcı Alert metodları
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}