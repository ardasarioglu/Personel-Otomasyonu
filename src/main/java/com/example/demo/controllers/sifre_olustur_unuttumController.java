package com.example.demo.controllers;

import com.example.demo.nesneler.Personel;
import com.example.demo.islemler.SifreOlustur;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;  // <-- ActionEvent import
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;         // <-- Node import
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class sifre_olustur_unuttumController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TextField epostaField;
    @FXML
    TextField parolaField;
    @FXML
    TextField paroladogrulaField;

    // Personel bilgisini tutmak için ekledik
    private Personel currentPersonel;

    // Ana menüye dönme
    public void anaMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/init.fxml"));
        root = loader.load();
        stage = (Stage) epostaField.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Şifre onaylama işlemi
    public void onayla() throws IOException {
        String eposta = epostaField.getText();
        String sifre = parolaField.getText();
        String sifredogrula = paroladogrulaField.getText();

        Gson gson = new Gson();
        boolean check = false;

        // Lokal değişken yerine, bulduğumuz personeli 'currentPersonel' e kaydedeceğiz
        try (FileReader reader = new FileReader("personel.json")) {
            Type userListType = new TypeToken<List<Personel>>() {}.getType();
            List<Personel> personelListesi = gson.fromJson(reader, userListType);

            for (Personel personel : personelListesi) {
                if (eposta.equals(personel.getEposta())) {
                    check = true;
                    currentPersonel = personel; // <-- Bulduğumuz personeli sakla
                    break;
                }
            }
        }
        if (check) {
            if (sifre.equals(sifredogrula)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Şifre Değiştir");
                alert.setHeaderText(null);
                alert.setContentText("Şifreyi değiştirmek istediğinize emin misiniz?");
                ButtonType yesButton = new ButtonType("Evet");
                ButtonType noButton = new ButtonType("Hayır");
                alert.getButtonTypes().setAll(yesButton, noButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == yesButton) {
                    // SifreOlustur işlemimizi yapalım
                    SifreOlustur.sifre_olustur(currentPersonel, sifre);

                    // Değişiklik sonrası ana menüye dön
                    anaMenu();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hata");
                alert.setHeaderText(null);
                alert.setContentText("Şifreler aynı değil!");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText(null);
            alert.setContentText("Bu e-postaya sahip bir kullanıcı yok!");
            alert.showAndWait();
        }
    }

    // Personel menüsüne geri dönme (Optional ile personel kontrolü)
    public void geriDon(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/personel_menu.fxml"));
        root = loader.load();

        // Burada personel_menuController'ı yakalıyoruz
        personel_menuController personelMenuController = loader.getController();

        // Optional ile currentPersonel kontrolü
        Optional<Personel> personelOptional = Optional.ofNullable(currentPersonel);
        personelOptional.ifPresentOrElse(
                p -> personelMenuController.setPersonel(p),  // Null değilse setPersonel
                () -> System.out.println("Personel bilgisi bulunamadı!") // Null ise log bas
        );

        // Sahneyi ayarla ve göster
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}