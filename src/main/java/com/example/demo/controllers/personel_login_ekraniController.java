package com.example.demo.controllers;

import com.example.demo.nesneler.Personel;
import com.example.demo.islemler.PersonelGiris;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class personel_login_ekraniController {

    @FXML
    Label bilgiLabel;
    @FXML
    TextField epostaField;
    @FXML
    PasswordField parolaField;

    PersonelGiris personelGiris=new PersonelGiris();


    Stage stage;
    Scene scene;
    Parent root;


    public void geri_don(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/init.fxml"));
        root=loader.load();
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void login(ActionEvent event) throws IOException{
        String eposta=epostaField.getText();
        String password=parolaField.getText();
        if(personelGiris.login(eposta, password)){
            Personel currentPersonel=personelGiris.getCurrentPersonel();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/demo/personel_menu.fxml"));
            root=loader.load();

            //Sonraki 2 satırın amacı currentPersonel'i bir sonraki sayfada kullanabilmemizi sağlıyo yine kalıp kod denebilir
            personel_menuController menuController=loader.getController();
            menuController.setPersonel(currentPersonel);

            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else{
            bilgiLabel.setText("Kullanıcı adı ya da şifre hatalı.");
        }

    }

    public void sifre_olustur(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/demo/sifre_olustur_unuttum.fxml"));
        root=loader.load();
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
