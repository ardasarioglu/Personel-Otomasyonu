package com.example.demo.controllers;

import com.example.demo.nesneler.Toplanti;
import com.example.demo.islemler.ToplantiEkle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class toplanti_duzenleController {
    @FXML
    private TextField toplantisaatiField;

    @FXML
    private TextField departmanField;

    private Parent root;
    private Scene scene;
    private Stage stage;

    // "Onayla" butonuna tıklanınca çağrılacak metod
    @FXML
    public void onayla(ActionEvent event) {
        String saat = toplantisaatiField.getText();
        String departman = departmanField.getText();

        // Toplantı nesnesi oluştur
        Toplanti yeniToplanti = new Toplanti(saat, departman);

        // JSON dosyasına toplantı ekle
        try {
            ToplantiEkle.toplantiEkle(yeniToplanti);  // Toplantıyı JSON dosyasına ekler
        } catch (IOException e) {
            e.printStackTrace();
            // Hata durumunda kullanıcıya bildirim göster
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Hata");
            errorAlert.setHeaderText("Toplantı ekleme sırasında bir hata oluştu.");
            errorAlert.setContentText("Toplantı JSON dosyasına kaydedilemedi.");
            errorAlert.showAndWait();
            return;
        }

        // Başarılı mesajını göster
        Alert infAlert = new Alert(Alert.AlertType.INFORMATION);
        infAlert.setTitle("Toplantı Ekle");
        infAlert.setHeaderText(null);
        infAlert.setContentText("Toplantı başarıyla eklendi.");
        infAlert.showAndWait();

        // Ana menüye dön
        try {
            anaMenu(event);  // Ana menüye geçiş yap
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ana menüye geçiş için metod
    @FXML
    private void anaMenu(ActionEvent event) throws IOException {
        try {
            // Ana menü FXML dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/yonetici_menu.fxml"));
            root = loader.load();

            // Sahneyi (pencereyi) al
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Yeni sahneyi (ana menü) ayarla
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Hata oluşursa kullanıcıya bildirim göster
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Hata");
            errorAlert.setHeaderText("Ana menüye geçiş sırasında bir hata oluştu.");
            errorAlert.setContentText("Ana menü FXML dosyası bulunamadı.");
            errorAlert.showAndWait();
        }
    }
}
