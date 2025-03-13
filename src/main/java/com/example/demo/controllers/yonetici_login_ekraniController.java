package com.example.demo.controllers;

import com.example.demo.nesneler.Yonetici;
import com.example.demo.islemler.YoneticiGiris;
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

public class yonetici_login_ekraniController {

    @FXML
    TextField epostaField;
    @FXML
    PasswordField parolaField;
    @FXML
    Label bilgiLabel;

    YoneticiGiris yoneticiGiris=new YoneticiGiris();



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
        //email ve şifre alınıp doğru mu diye kontrol ediyo doğruysa bi sonraki ekrana geçiyo değilse hata mesajı veriyor
        String email=epostaField.getText();
        String parola= parolaField.getText();
        if(yoneticiGiris.login(email, parola)){
            Yonetici currentYonetici=yoneticiGiris.getCurrentYonetici();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/demo/yonetici_menu.fxml"));
            root=loader.load();

            //giriş yapan kullanıcıyı menucontrollera gönderiyoruz
            yonetici_menuController menuController=loader.getController();
            menuController.setYonetici(currentYonetici);

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
