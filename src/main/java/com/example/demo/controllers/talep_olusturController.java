package com.example.demo.controllers;

import com.example.demo.nesneler.Personel;
import com.example.demo.nesneler.Talep;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class talep_olusturController {

    @FXML
    Stage stage;
    @FXML
    Scene scene;
    @FXML
    Parent root;
    @FXML
    private TextField zamTextField;

    @FXML
    private TextField izinTextField;

    private static final String FILE_PATH = "talepler.json";
    private Personel personel;

    // Personel bilgisini alıp talep oluşturma
    public void setPersonel(Personel personel) {
        this.personel = personel;
    }

    // Zam isteği butonuna tıklandığında
    @FXML
    void onZamOnaylaButtonClick(ActionEvent event) {
        String zamDegeri = zamTextField.getText();

        if (zamDegeri.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Hata", "Zam isteği boş bırakılamaz.");
        } else {
            Talep yeniTalep = new Talep(
                    "zam",
                    zamDegeri,
                    String.valueOf(personel.getId()),
                    personel.getIsim(),
                    personel.getSoyisim()
            );
            saveTalepToFile(yeniTalep);
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Zam isteğiniz alındı: " + zamDegeri + "%");
            zamTextField.clear();
        }
    }

    // İzin isteği butonuna tıklandığında
    @FXML
    void onIzinOnaylaButtonClick(ActionEvent event) {
        String izinDegeri = izinTextField.getText();

        if (izinDegeri.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Hata", "İzin isteği boş bırakılamaz.");
        } else {
            Talep yeniTalep = new Talep(
                    "izin",
                    izinDegeri,
                    String.valueOf(personel.getId()),
                    personel.getIsim(),
                    personel.getSoyisim()
            );
            saveTalepToFile(yeniTalep);
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "İzin isteğiniz alındı: " + izinDegeri + " gün");
            izinTextField.clear();
        }
    }

    // Talebi JSON dosyasına kaydetme
    private void saveTalepToFile(Talep yeniTalep) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Talep> talepler = new ArrayList<>();

        // JSON dosyasını okuma
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type talepListType = new TypeToken<List<Talep>>() {}.getType();
            talepler = gson.fromJson(reader, talepListType);

            if (talepler == null) {
                talepler = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            // Dosya yoksa, yeni bir liste oluştur
            talepler = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Yeni talebi listeye ekleme
        talepler.add(yeniTalep);

        // Listeyi JSON dosyasına yazma
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(talepler, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Kullanıcıya uyarı göstermek için
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Geri dönme işlemi
    public void geriDon(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/personel_menu.fxml"));
        root = loader.load();
        personel_menuController personelMenuController = loader.getController();

        // Optional ile personel kontrolü
        Optional<Personel> personelOptional = Optional.ofNullable(personel);
        personelOptional.ifPresentOrElse(
                p -> personelMenuController.setPersonel(p), // Eğer personel null değilse işlem yap
                () -> System.out.println("Personel bilgisi bulunamadı!") // Eğer null ise alternatif işlem
        );

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
