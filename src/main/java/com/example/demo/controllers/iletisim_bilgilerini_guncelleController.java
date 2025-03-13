package com.example.demo.controllers;

import com.example.demo.AnaMenu;
import com.example.demo.nesneler.Personel;
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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class iletisim_bilgilerini_guncelleController implements AnaMenu {

    @FXML
    private TextField newEmailField;    // Yeni E-Posta için TextField
    @FXML
    private TextField newPhoneField;    // Yeni Telefon için TextField

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Güncelleyeceğimiz personel
    private Personel currentPersonel;

    /**
     * Bu ekran yüklenirken, diğer ekrandan Personel bilgisi aktarılır.
     */
    public void setPersonel(Personel personel) {
        this.currentPersonel = personel;
    }

    /**
     * E-Posta güncelleme işlemi
     */
    @FXML
    public void updateEmail(ActionEvent event) {
        // TextField'dan yeni e-postayı al
        String yeniEposta = newEmailField.getText().trim();

        // Basit bir kontrol
        if (yeniEposta.isEmpty()) {
            showAlert("Hata", "Yeni e-posta boş olamaz!", Alert.AlertType.ERROR);
            return;
        }

        // Personel nesnesi null mı kontrol edelim
        if (currentPersonel == null) {
            showAlert("Hata", "Güncellenecek personel bilgisi bulunamadı!", Alert.AlertType.ERROR);
            return;
        }

        // Personelin e-posta alanını güncelle
        currentPersonel.setEposta(yeniEposta);

        // JSON dosyasında kaydı bulup güncelle
        boolean guncelleBasarili = updatePersonelJson(currentPersonel);
        if (guncelleBasarili) {
            showAlert("Başarılı", "E-Posta başarıyla güncellendi!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Hata", "E-Posta güncelleme başarısız!", Alert.AlertType.ERROR);
        }
    }

    /**
     * Telefon güncelleme işlemi
     */
    @FXML
    public void updatePhone(ActionEvent event) {
        // TextField'dan yeni telefonu al
        String yeniTelefon = newPhoneField.getText().trim();

        if (yeniTelefon.isEmpty()) {
            showAlert("Hata", "Yeni telefon numarası boş olamaz!", Alert.AlertType.ERROR);
            return;
        }

        if (currentPersonel == null) {
            showAlert("Hata", "Güncellenecek personel bilgisi bulunamadı!", Alert.AlertType.ERROR);
            return;
        }

        // Telefonu long olarak tutuyoruz (Personel sınıfına göre düzenleyin)
        try {
            long yeniTelLong = Long.parseLong(yeniTelefon);
            currentPersonel.setTelefon((int) yeniTelLong);
        } catch (NumberFormatException e) {
            showAlert("Hata", "Telefon numarası sadece rakamlardan oluşmalıdır!", Alert.AlertType.ERROR);
            return;
        }

        boolean guncelleBasarili = updatePersonelJson(currentPersonel);
        if (guncelleBasarili) {
            showAlert("Başarılı", "Telefon numarası başarıyla güncellendi!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Hata", "Telefon güncelleme başarısız!", Alert.AlertType.ERROR);
        }
    }

    /**
     * Ana Menü butonuna basıldığında çağrılır.
     * Projenizde "init.fxml" yerine başka bir menü varsa, yolu değiştirin.
     */
    @FXML
    public void anaMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/init.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * JSON içinde ilgili personeli bulup eposta/telefon bilgisini günceller, sonra tekrar yazar.
     */
    private boolean updatePersonelJson(Personel guncelPersonel) {
        try (FileReader reader = new FileReader("personel.json")) {
            Type listType = new TypeToken<List<Personel>>() {}.getType();
            List<Personel> personelList = new Gson().fromJson(reader, listType);

            if (personelList == null) {
                showAlert("Hata", "Personel JSON dosyası boş veya okunamadı!", Alert.AlertType.ERROR);
                return false;
            }

            // ID eşleşen kaydı bul
            for (Personel p : personelList) {
                if (p.getId() == guncelPersonel.getId()) {
                    // Personel'in sadece e-posta ve telefon bilgilerini güncelliyoruz
                    p.setEposta(guncelPersonel.getEposta());
                    p.setTelefon((guncelPersonel.getTelefon()));
                    break;
                }
            }

            // Güncellenmiş listeyi geri yaz
            try (FileWriter writer = new FileWriter("personel.json")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(personelList, writer);
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kullanıcıya mesaj göstermek için yardımcı metod
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}