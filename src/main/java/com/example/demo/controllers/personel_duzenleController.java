package com.example.demo.controllers;

import com.example.demo.nesneler.Personel;
import com.example.demo.islemler.PersonelGuncelleme;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class personel_duzenleController {

    @FXML
    Label idLabel;
    @FXML
    Label isimLabel;
    @FXML
    Label soyisimLabel;
    @FXML
    Label departmanLabel;
    @FXML
    Label pozisyonLabel;
    @FXML
    Label maasLabel;
    @FXML
    Label izingunuLabel;

    Stage stage;
    Scene scene;
    Parent root;

    @FXML
    TextField departmanField;
    @FXML
    TextField pozisyonField;
    @FXML
    TextField maasField;
    @FXML
    TextField izingunuField;




    private Personel personel;

    public void setCurrentPersonel(Personel personel){
        this.personel=personel;
        idLabel.setText("ID: "+String.valueOf(personel.getId()));
        departmanLabel.setText("Departman: "+personel.getDepartman());
        pozisyonLabel.setText("Pozisyon: "+personel.getPozisyon());
        maasLabel.setText("Maaş: "+String.valueOf(personel.getMaas()));
        izingunuLabel.setText("İzin Günü: "+String.valueOf(personel.getIzin_gunu()));
        isimLabel.setText("İsim: "+personel.getIsim());
        soyisimLabel.setText("Soyisim: "+personel.getSoyisim());

    }

    public void anaMenu(ActionEvent event) throws IOException{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/demo/yonetici_menu.fxml"));
        root=loader.load();
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void kaydet(ActionEvent event) throws IOException{
        //burda eğer ilgili textfielda bişey yazıldıysa onu yeni değerle eşleştirir boş ise eski değerle eşleştirir
        String yeniDepartman = departmanField.getText().isEmpty() ? personel.getDepartman() : departmanField.getText();
        String yeniPozisyon = pozisyonField.getText().isEmpty() ? personel.getPozisyon() : pozisyonField.getText();
        double yeniMaas = maasField.getText().isEmpty() ? personel.getMaas() : Double.parseDouble(maasField.getText());
        int yeniIzinGunu = izingunuField.getText().isEmpty() ? personel.getIzin_gunu() : Integer.parseInt(izingunuField.getText());

        //burdan sonrası pop up ekranı kodları
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Değişiklikleri Kaydet");
        alert.setHeaderText(null);
        alert.setContentText("Değişiklikleri kaydetmek istediğinize emin misiniz?");

        ButtonType yesButton=new ButtonType("Evet");
        ButtonType noButton=new ButtonType("Hayır");
        alert.getButtonTypes().setAll(yesButton, noButton);

        //burdan sonrası kalıp kod
        Optional<ButtonType> result=alert.showAndWait();
        if(result.isPresent() && result.get()==yesButton){
            PersonelGuncelleme.guncellePersonel(personel.getId(), yeniDepartman, yeniPozisyon, yeniMaas, yeniIzinGunu);
            Alert confAlert=new Alert(Alert.AlertType.CONFIRMATION);
            confAlert.setTitle("Başarılı");
            confAlert.setHeaderText(null);
            confAlert.setContentText("İşlemler başarıyla gerçekleşti.");
            ButtonType okButton=new ButtonType("Tamam");
            confAlert.getButtonTypes().setAll(okButton);
            confAlert.showAndWait();
            anaMenu(event);
        }






    }
}
