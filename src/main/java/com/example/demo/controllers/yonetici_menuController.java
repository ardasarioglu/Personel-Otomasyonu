package com.example.demo.controllers;

import com.example.demo.nesneler.Personel;
import com.example.demo.nesneler.Yonetici;
import com.example.demo.islemler.PersonelCikart;
import com.google.gson.Gson;
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
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class yonetici_menuController {

    Stage stage;
    Scene scene;
    Parent root;
    private Yonetici yonetici;
    @FXML
    TextField duzenleField;
    @FXML
    TextField cikartField;


    public void cikis(ActionEvent event) throws IOException{
        //burda sadece ana ekrana geri dönüyo başka bi işlem yok
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/init.fxml"));
        root=loader.load();
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void personelleriListele(ActionEvent event) throws IOException{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/demo/personel_listele.fxml"));
        root=loader.load();
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void personelEkle(ActionEvent event) throws  IOException{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/demo/personel_ekle.fxml"));
        root=loader.load();
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void toplantiDuzenle(ActionEvent event) throws IOException{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/demo/toplanti_duzenle.fxml"));
        root=loader.load();
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void talepler(ActionEvent event) throws  IOException{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/demo/talepler.fxml"));
        root=loader.load();
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void setYonetici(Yonetici yonetici){
        this.yonetici=yonetici;
    }

    public void personelDuzenle(ActionEvent event) throws IOException{
        String id=duzenleField.getText();
        Gson gson=new Gson();
        try(FileReader reader=new FileReader("personel.json")){
            Type userListType=new TypeToken<List<Personel>>() {}.getType();
            List<Personel> yuklenenListe=gson.fromJson(reader, userListType);

            Personel currentPersonel=null;
            boolean check=false;
            for(Personel personel: yuklenenListe){
                if(String.valueOf(personel.getId()).equals(id)){
                    currentPersonel=personel;
                    check=true;
                    break;
                }

            }

            if(check){

                FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/demo/personel_duzenle.fxml"));
                root=loader.load();

                personel_duzenleController duzenleController=loader.getController();
                duzenleController.setCurrentPersonel(currentPersonel);

                stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                scene=new Scene(root);
                stage.setScene(scene);
                stage.show();


            }
            else{
                //id kısmı boş veya o id'ye sahip bir personel yoksa popup ile hata mesajı veriyor
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hata");
                alert.setHeaderText(null);
                alert.setContentText("Böyle bir personel yok!");
                alert.showAndWait();
            }
        }
    }
    public void personelCikart() throws IOException {
        String id=cikartField.getText();
        if(id==null || id.isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setContentText("Lütfen bir ID giriniz!");
            alert.setHeaderText(null);
            alert.showAndWait();
        }

        else{
            Gson gson=new Gson();
            try(FileReader reader=new FileReader("personel.json")){
                Type userListType = new TypeToken<List<Personel>>() {}.getType();
                List<Personel> personelListesi = gson.fromJson(reader, userListType);

                boolean check=false;
                Personel currentPersonel=null;
                for(Personel personel: personelListesi){
                    if(personel.getId()==Integer.parseInt(id)){
                        currentPersonel=personel;
                        check=true;
                    }
                }


                if(check){
                    PersonelCikart.personelCikart(currentPersonel, Integer.parseInt(id));
                }
                else{
                    Alert infAlert=new Alert(Alert.AlertType.ERROR);
                    infAlert.setTitle("Bulunamadı.");
                    infAlert.setHeaderText(null);
                    infAlert.setContentText("Bu ID'ye sahip bir personel yok!");
                    infAlert.showAndWait();
                }
            }
        }
        cikartField.setText("");


    }

}
