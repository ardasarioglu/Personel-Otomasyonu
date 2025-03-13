package com.example.demo.islemler;

import com.example.demo.nesneler.Personel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class PersonelGiris {
    private static final String JSON_FILE_PATH="personel.json";

    //burası giriş yapıldığı zaman giriş yapan personelin bilgilerinin o ekranda kullanılabilmesi için var aşağıda getCurrentPersonelin amacı da bu
    private Personel currentPersonel;

    public boolean login(String email, String password){
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        try(FileReader reader = new FileReader(JSON_FILE_PATH)){
            //sonraki 2 satır tamamen kalıp kod ne anlama geldiğini bende bilmiyom
            Type userListType=new TypeToken<List<Personel>>() {}.getType();
            List<Personel> yuklenenListe=gson.fromJson(reader, userListType);

            for(Personel personel: yuklenenListe){
                if (personel.getEposta().equals(email) && personel.getSifre().equals(password)){
                    currentPersonel=personel;
                    return true;
                }

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;

    }

    public Personel getCurrentPersonel(){
        return currentPersonel;
    }
}
