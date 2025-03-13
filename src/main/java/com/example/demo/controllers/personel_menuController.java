package com.example.demo.controllers;

import com.example.demo.nesneler.Personel;
import com.example.demo.nesneler.Toplanti;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class personel_menuController {

    @FXML
    private ListView<String> toplantilarListView;

    // Personel bilgilerini göstermek için kullanılan Label'lar
    @FXML
    private Label idLabel;
    @FXML
    private Label departmanLabel;
    @FXML
    private Label pozisyonLabel;
    @FXML
    private Label maasLabel;
    @FXML
    private Label izinGunuLabel;
    @FXML
    private Label epostaLabel;
    @FXML
    private Label telefonLabel;

    // Personel nesnesi (bu ekranda gösterilecek kişi)
    private Personel personel;

    // Sahne ve pencere bilgileri
    private Stage stage;
    private Scene scene;
    private Parent root;


    /**
     * Diğer ekrandan bu ekrana Personel nesnesini taşırken çağırılır.
     * Label'lara Personel bilgilerini yazdırır.
     */
    public void setPersonel(Personel personel) {
        this.personel = personel;

        // Null kontrolü
        if (personel != null) {
            idLabel.setText("ID: " + personel.getId());
            departmanLabel.setText("Departman: " + personel.getDepartman());
            pozisyonLabel.setText("Pozisyon: " + personel.getPozisyon());
            maasLabel.setText("Maaş: " + personel.getMaas());
            izinGunuLabel.setText("İzin Günü: " + personel.getIzin_gunu());
            epostaLabel.setText("E-Posta: " + personel.getEposta());
            telefonLabel.setText("Telefon: " + personel.getTelefon());

            loadToplantilar();
        }
    }

    /**
     * Çıkış yapma işlemi: init.fxml'e dönüyoruz.
     */
    public void cikis(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/init.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /*
     * Şifre değiştirme ekranına geçiş (sifre_olustur2.fxml).
     * Burada personel nesnesini sifre_olustur2Controller'a iletiyoruz.
     */
    public void sifredegistir(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/sifre_olustur2.fxml"));
        root = loader.load();

        // Controller'ı alıyoruz
        sifre_olustur2Controller sifreController = loader.getController();
        // Personel nesnesini aktararak, orada currentPersonel null kalmasını önlüyoruz
        sifreController.setPersonel(this.personel);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Talep oluşturma ekranına (talep_olustur.fxml) geçiş.
     * Personel nesnesini talep_olusturController'a aktarıyoruz.
     */
    public void talepolustur(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/talep_olustur.fxml"));
        root = loader.load();

        // Talep oluşturma ekranının controller'ını alıyoruz
        talep_olusturController controller = loader.getController();
        // Personel nesnesini ilgili controller'a aktarıyoruz
        controller.setPersonel(personel);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void iletisim(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/iletisim_bilgilerini_guncelle.fxml"));
        Parent root = loader.load();

        // Controller'ı al
        iletisim_bilgilerini_guncelleController iletisimController = loader.getController();

        // Mevcut personeli (null değilse) buraya set etmeniz lazım
        // Örneğin "this.personel" bir önceki aşamada giriş yapmış ya da tablo seçilmiş personel
        iletisimController.setPersonel(this.personel);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private void loadToplantilar() {
        String json = "";
        try {
            json = new String(Files.readAllBytes(Paths.get("toplantilar.json")));
        } catch (IOException e) {
            e.printStackTrace();
            // Hata durumunda boş bir JSON stringi kullanabilirsiniz ya da bir hata mesajı gösterebilirsiniz.
            return;
        }


        Gson gson = new Gson();
        Type listType = new TypeToken<List<Toplanti>>() {}.getType();
        List<Toplanti> toplantilar = gson.fromJson(json, listType);

        List<String> filtrelenmisToplantilar = toplantilar.stream()
                .filter(t -> t.getDepartman().equals(personel.getDepartman())) // Departman kontrolü
                .map(t -> "Saat: " + t.getSaat() + ", Departman: " + t.getDepartman()) // Detay formatı
                .collect(Collectors.toList());

        ObservableList<String> toplantilarListesi = FXCollections.observableArrayList(filtrelenmisToplantilar);

        toplantilarListView.setItems(toplantilarListesi);
    }

}