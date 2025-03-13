package com.example.demo.islemler;

import com.example.demo.nesneler.Personel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Alert;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class SifreOlustur {

    public static void sifre_olustur(Personel personel, String sifre) throws IOException{
        List<Personel> liste=null;
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        personel.setSifre(sifre);
        try(FileReader reader=new FileReader("personel.json")){
            Type userListType=new TypeToken<List<Personel>>() {}.getType();
            List<Personel> personelListesi=gson.fromJson(reader, userListType);
            for(Personel personel1: personelListesi){
                if(personel.getEposta().equals(personel1.getEposta())){
                    personel1.setSifre(personel.getSifre());
                    break;
                }
            }
            liste=personelListesi;
        }
        try(FileWriter writer=new FileWriter("personel.json")){
            gson.toJson(liste, writer);
        }









        Alert infalert=new Alert(Alert.AlertType.INFORMATION);
        infalert.setTitle("Şifre Değiştirme");
        infalert.setHeaderText(null);
        infalert.setContentText("Şifre Başarıyla Değiştirildi.");
        infalert.showAndWait();



    }
}
