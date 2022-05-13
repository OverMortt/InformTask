module org.mortt.weather {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;

    opens org.mortt.weather to javafx.fxml;
    exports org.mortt.weather;
}