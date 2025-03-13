module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.security.jgss;
    requires com.google.gson;
    requires java.management;

    exports com.example.demo;
    exports com.example.demo.controllers;

    // Gson tarafından kullanılacak alanlara erişimi açıyoruz
    opens com.example.demo to com.google.gson, javafx.fxml;
    opens com.example.demo.controllers to javafx.fxml, com.google.gson;
    exports com.example.demo.nesneler;
    opens com.example.demo.nesneler to com.google.gson, javafx.fxml;
}
