package com.example.demo.islemler;

import com.example.demo.nesneler.Personel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class PersonelCikart{

    public static void personelCikart(Personel personel, int id) throws IOException {
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        try(FileReader reader=new FileReader("personel.json")){
            Type userListType = new TypeToken<List<Personel>>() {}.getType();
            List<Personel> personelListesi = gson.fromJson(reader, userListType);

            Alert confAlert=new Alert(Alert.AlertType.CONFIRMATION);
            confAlert.setTitle("Personel Çıkartma");
            confAlert.setHeaderText("Bu personeli işten çıkartmak istediğinize emin misiniz?");
            confAlert.setContentText("İsim: "+personel.getIsim()+"\nSoyisim: "+personel.getSoyisim()+"\nID: "+personel.getId());
            ButtonType yesButton=new ButtonType("Evet");
            ButtonType noButton=new ButtonType("Hayır");
            confAlert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result=confAlert.showAndWait();
            if(result.isPresent() && result.get()==yesButton){
                personelListesi.removeIf(p->p.getId()==id);

                Alert infAlert=new Alert(Alert.AlertType.INFORMATION);
                infAlert.setTitle("İşlem Başarılı");
                infAlert.setHeaderText(null);
                infAlert.setContentText("İşlem başarıyla gerçekleşti.");
                infAlert.showAndWait();
            }
            try(FileWriter writer=new FileWriter("personel.json")){
                gson.toJson(personelListesi, writer);
            }
        }


    }
}
