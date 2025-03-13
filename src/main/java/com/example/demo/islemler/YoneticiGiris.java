package com.example.demo.islemler;

import com.example.demo.nesneler.Yonetici;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class YoneticiGiris {

    private Yonetici currentYonetici;
    public boolean login(String email, String password){
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        try(FileReader reader = new FileReader("yonetici.json")){

            //2 satırlık kalıp kod json'u okumaya yarıyo
            Type userListType=new TypeToken<List<Yonetici>>() {}.getType();
            List<Yonetici> yuklenenListe=gson.fromJson(reader, userListType);

            for(Yonetici yonetici: yuklenenListe){
                if (yonetici.getEposta().equals(email) && yonetici.getSifre().equals(password)){
                    currentYonetici=yonetici;
                    return true;
                }

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;

    }

    public Yonetici getCurrentYonetici(){
        return currentYonetici;
    }
}
