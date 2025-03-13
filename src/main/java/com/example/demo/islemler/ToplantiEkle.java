package com.example.demo.islemler;

import com.example.demo.nesneler.Toplanti;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ToplantiEkle {
    // Toplantıyı JSON dosyasına ekleyen metot
    public static void toplantiEkle(Toplanti toplanti) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileReader reader = new FileReader("toplantilar.json")) {
            // Toplantı listesinin tipini belirleyin
            Type toplantiListType = new TypeToken<List<Toplanti>>() {}.getType();
            List<Toplanti> toplantilar = gson.fromJson(reader, toplantiListType);

            // Eğer dosyada henüz toplantılar yoksa, yeni bir liste oluşturun
            if (toplantilar == null) {
                toplantilar = new ArrayList<>();
            }

            // Yeni toplantıyı ekle
            toplantilar.add(toplanti);

            // JSON dosyasına toplantıları kaydedin
            try (FileWriter writer = new FileWriter("toplantilar.json")) {
                gson.toJson(toplantilar, writer);
            }
        }
    }
}
