package com.example.demo.controllers;

import com.example.demo.nesneler.Personel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class personel_listeleController {
    @FXML
    private Button anaMenuButton;

    @FXML
    public void anaMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/yonetici_menu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) anaMenuButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private TableView<Personel> personelTable;

    @FXML
    private TableColumn<Personel, Integer> idColumn;

    @FXML
    private TableColumn<Personel, String> isimColumn;

    @FXML
    private TableColumn<Personel, String> soyisimColumn;

    @FXML
    private TableColumn<Personel, String> departmanColumn;

    @FXML
    private TableColumn<Personel, String> pozisyonColumn;

    @FXML
    private TableColumn<Personel, Double> maasColumn;

    @FXML
    private TableColumn<Personel, Integer> izinGunuColumn;

    @FXML
    private TableColumn<Personel, String> epostaColumn;

    @FXML
    private TableColumn<Personel, Long> telefonColumn;

    @FXML
    public void initialize() throws  IOException{
        // Kolonları getter metodlarına bağlama
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        isimColumn.setCellValueFactory(new PropertyValueFactory<>("isim"));
        soyisimColumn.setCellValueFactory(new PropertyValueFactory<>("soyisim"));
        departmanColumn.setCellValueFactory(new PropertyValueFactory<>("departman"));
        pozisyonColumn.setCellValueFactory(new PropertyValueFactory<>("pozisyon"));
        maasColumn.setCellValueFactory(new PropertyValueFactory<>("maas"));
        izinGunuColumn.setCellValueFactory(new PropertyValueFactory<>("izin_gunu"));
        epostaColumn.setCellValueFactory(new PropertyValueFactory<>("eposta"));
        telefonColumn.setCellValueFactory(new PropertyValueFactory<>("telefon"));

        Gson gson=new Gson();
        List<Personel> personelListesi = null;
        try(FileReader reader=new FileReader("personel.json")){
            Type userListType=new TypeToken<List<Personel>>() {}.getType();
            personelListesi=gson.fromJson(reader, userListType);

        }
        ObservableList<Personel> personeller = FXCollections.observableArrayList(personelListesi);

        // Örnek veri ekleme


        // Tabloya verileri ekleme
        personelTable.setItems(personeller);
    }
}
