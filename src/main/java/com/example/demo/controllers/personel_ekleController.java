package com.example.demo.controllers;

import com.example.demo.nesneler.Personel;
import com.example.demo.islemler.PersonelEkle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;

public class personel_ekleController {
    Stage stage;
    Scene scene;
    Parent root;

    @FXML
    TextField isimField;
    @FXML
    TextField soyisimField;
    @FXML
    TextField departmanField;
    @FXML
    TextField pozisyonField;
    @FXML
    TextField epostaField;
    @FXML
    TextField telefonField;
    @FXML
    TextField maasField;
    @FXML
    TextField izingunuField;

    public void anaMenu() throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/demo/yonetici_menu.fxml"));
        root=loader.load();
        stage=(Stage)isimField.getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void kaydet() throws IOException{
        if(departmanField.getText().isEmpty()||pozisyonField.getText().isEmpty()||epostaField.getText().isEmpty()||telefonField.getText().isEmpty()||maasField.getText().isEmpty()||izingunuField.getText().isEmpty()||isimField.getText().isEmpty()||soyisimField.getText().isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText(null);
            alert.setContentText("Alanların hepsini doldurunuz!");
            alert.showAndWait();
        }
        else{



            int yeniID=getSonID()+1;
            setSonID(yeniID);
            String yeniIsim=isimField.getText();
            String yeniSoyisim=soyisimField.getText();
            String yeniDepartman=departmanField.getText();
            String yeniPozsiyon=pozisyonField.getText();
            String yeniEposta=epostaField.getText();
            String yeniSifre="";
            long yeniTelefon=Long.parseLong(telefonField.getText());
            double yeniMaas=Double.parseDouble(maasField.getText());
            int yeniIzingunu=Integer.parseInt(izingunuField.getText());

            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Personel Ekle");
            alert.setHeaderText("Bu personeli eklemek istediğinize emin misiniz?");
            alert.setContentText("ID: "+yeniID+"\nİsim: "+yeniIsim+"\nSoyisim: "+yeniSoyisim);
            ButtonType yesButton=new ButtonType("Evet");
            ButtonType noButton=new ButtonType("Hayır");
            alert.getButtonTypes().setAll(yesButton, noButton);
            Optional<ButtonType> result=alert.showAndWait();
            if(result.isPresent() && result.get()==yesButton){
                Personel yeniPersonel=new Personel(yeniID, yeniIsim, yeniSoyisim, yeniEposta, yeniSifre, yeniTelefon, yeniDepartman, yeniPozsiyon, yeniMaas, yeniIzingunu);
                PersonelEkle.personelEkle(yeniPersonel);

                Alert infAlert=new Alert(Alert.AlertType.INFORMATION);
                infAlert.setTitle("Personel Ekle");
                infAlert.setHeaderText(null);
                infAlert.setContentText("İşlem başarıyla gerçekleşti.");
                infAlert.showAndWait();
                anaMenu();

            }




        }
    }
    public int getSonID() throws IOException{
        try(BufferedReader reader=new BufferedReader(new FileReader("id_counter.json"))){
            return Integer.parseInt(reader.readLine());
        }
    }
    public void setSonID(int id) throws IOException{
        try(BufferedWriter writer=new BufferedWriter(new FileWriter("id_counter.json"))){
            writer.write(String.valueOf(id));
        }
    }

}
