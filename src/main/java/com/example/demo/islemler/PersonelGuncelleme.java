package com.example.demo.islemler;

import com.example.demo.nesneler.Personel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class PersonelGuncelleme {
    public static void guncellePersonel(int id, String departman, String pozisyon, double maas, int izinGunu) throws IOException {
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        try(FileReader reader=new FileReader("personel.json")){
            Type userListType=new TypeToken<List<Personel>>() {}.getType();
            List<Personel> personelListesi=gson.fromJson(reader, userListType);
            for(Personel personel: personelListesi){
                if(personel.getId()==id){
                    personel.setDepartman(departman);
                    personel.setPozisyon(pozisyon);
                    personel.setMaas(maas);
                    personel.setIzin_gunu(izinGunu);
                }
            }
            try(FileWriter writer=new FileWriter("personel.json")){
                gson.toJson(personelListesi, writer);
            }
        }
    }
}
