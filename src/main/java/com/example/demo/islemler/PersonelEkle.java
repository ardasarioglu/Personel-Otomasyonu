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

public class PersonelEkle {
    public static void personelEkle(Personel personel) throws IOException {
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        try(FileReader reader=new FileReader("personel.json")){
            Type userListType = new TypeToken<List<Personel>>() {}.getType();
            List<Personel> personelListesi = gson.fromJson(reader, userListType);
            personelListesi.add(personel);
            try(FileWriter writer=new FileWriter("personel.json")){
                gson.toJson(personelListesi, writer);
            }
        }
    }
}
