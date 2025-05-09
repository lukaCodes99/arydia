module hr.tvz.arydia.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens hr.tvz.arydia.server to javafx.fxml;
    exports hr.tvz.arydia.server;
    exports hr.tvz.arydia.server.controller;
    opens hr.tvz.arydia.server.controller to javafx.fxml;
}