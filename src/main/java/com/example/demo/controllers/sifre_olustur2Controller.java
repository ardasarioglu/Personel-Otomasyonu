package com.example.demo.controllers;

import com.example.demo.islemler.SifreOlustur;
import com.example.demo.nesneler.Personel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

public class sifre_olustur2Controller {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField epostaField;
    @FXML
    private TextField parolaField;
    @FXML
    private TextField paroladogrulaField;

    // Bu ekrana gelen Personel nesnesini saklıyoruz
    private Personel currentPersonel;

    /**
     * personel_menuController'dan gelen Personel nesnesini burada tutarız.
     */
    public void setPersonel(Personel personel) {
        this.currentPersonel = personel;
    }

    /**
     * Giriş ekranına dönme (init.fxml)
     */
    public void anaMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/init.fxml"));
        root = loader.load();
        stage = (Stage) epostaField.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Şifre onaylama işlemi (personel.json'da e-posta eşleşirse şifreyi günceller)
     */
    public void onayla() throws IOException {
        String eposta = epostaField.getText();
        String sifre = parolaField.getText();
        String sifredogrula = paroladogrulaField.getText();

        Gson gson = new Gson();
        boolean check = false;
        Personel foundPersonel = null;

        // Personel listesi JSON'dan okunuyor
        try (FileReader reader = new FileReader("personel.json")) {
            Type userListType = new TypeToken<List<Personel>>() {}.getType();
            List<Personel> personelListesi = gson.fromJson(reader, userListType);

            // E-postayı eşleştiren personeli bul
            for (Personel p : personelListesi) {
                if (eposta.equals(p.getEposta())) {
                    check = true;
                    foundPersonel = p;
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
                    // Şifre değiştir
                    SifreOlustur.sifre_olustur(foundPersonel, sifre);

                    // Ana menüye dön
                    anaMenu();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hata");
                alert.setHeaderText(null);
                alert.setContentText("Şifreler aynı değil!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText(null);
            alert.setContentText("Bu e-postaya sahip bir kullanıcı yok!");
            alert.showAndWait();
        }
    }

    /**
     * Geri dön: personel_menu.fxml'e gideriz.
     * currentPersonel null değilse setPersonel ile menüye aktarır.
     */
    public void geriDon(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/personel_menu.fxml"));
        root = loader.load();

        // personel_menuController'ı yakala
        personel_menuController menuController = loader.getController();

        // Optional ile null kontrolü
        Optional<Personel> personelOptional = Optional.ofNullable(currentPersonel);
        personelOptional.ifPresentOrElse(
                p -> menuController.setPersonel(p), // Null değilse menüye aktar
                () -> System.out.println("Personel bilgisi bulunamadı!") // Null ise log bas
        );

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}