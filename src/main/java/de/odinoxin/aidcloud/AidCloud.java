package de.odinoxin.aidcloud;

import de.odinoxin.aidcloud.plugins.addresses.AddressProvider;
import de.odinoxin.aidcloud.plugins.contact.information.ContactInformationProvider;
import de.odinoxin.aidcloud.plugins.contact.types.ContactTypeProvider;
import de.odinoxin.aidcloud.plugins.countries.CountryProvider;
import de.odinoxin.aidcloud.plugins.languages.LanguageProvider;
import de.odinoxin.aidcloud.plugins.people.PersonProvider;
import de.odinoxin.aidcloud.translation.Translator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.hibernate.cfg.Configuration;

import javax.xml.ws.Endpoint;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class AidCloud extends Application {
    public static final String INVALID_SESSION = "Invalid session, or session is over!";
    private static final String ADDRESS_PREFIX = "http://";
    private static final String ADDRESS = ADDRESS_PREFIX + "%s:%s/AidCloud/";
    private static final int PORT = 15123;

    public static void main(String[] args) {
        AidCloud.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("AidCloud");
        primaryStage.setOnCloseRequest(ev -> System.exit(0));
        GridPane root = FXMLLoader.load(AidCloud.class.getResource("/AidCloud.fxml"));

        TextField txfURL = (TextField) root.lookup("#txfURL");
        TextField txfPort = (TextField) root.lookup("#txfPort");
        TextField txfDBURL = (TextField) root.lookup("#txfDBURL");
        TextField txfDB = (TextField) root.lookup("#txfDB");
        ComboBox<DBSetting> cboDB = (ComboBox<DBSetting>) root.lookup("#cboDB");
        TextField txfUser = (TextField) root.lookup("#txfUser");
        PasswordField pwfPwd = (PasswordField) root.lookup("#pwfPwd");

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> addresses;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp())
                    continue;
                addresses = networkInterface.getInetAddresses();
                if (addresses.hasMoreElements()) {
                    txfURL.setText(addresses.nextElement().getHostAddress());
                    break;
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        txfPort.setText(String.valueOf(AidCloud.PORT));

        txfDBURL.setText("localhost");
        txfDB.setText("AidCloud");
        cboDB.setCellFactory(param -> new ListCell<DBSetting>() {
            @Override
            protected void updateItem(DBSetting item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    this.setText(item.getName());
                    return;
                }
                this.setText(null);
                this.setGraphic(null);
            }
        });
        cboDB.getItems().addAll(
                new DBSetting("MSSQL 2012", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "org.hibernate.dialect.SQLServer2012Dialect", "jdbc:sqlserver://"));
        cboDB.getSelectionModel().selectFirst();

        Button btnLaunch = (Button) root.lookup("#btnLaunch");
        btnLaunch.setOnAction(ev -> {
            Scene setup = primaryStage.getScene();

            double width = primaryStage.getWidth();
            double height = primaryStage.getHeight();
            primaryStage.setScene(new Scene(new ProgressIndicator()));
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
            new Thread(() -> {
                try {
                    Configuration cfg = new Configuration();
                    DBSetting dbSetting = cboDB.getSelectionModel().getSelectedItem();
                    cfg.setProperty("hibernate.dialect", dbSetting.getDialect());
                    cfg.setProperty("hibernate.connection.driver_class", dbSetting.getDriverClass());
                    cfg.setProperty("hibernate.connection.url", cboDB.getSelectionModel().getSelectedItem().getJdbc() + txfDBURL.getText() + ";databaseName=" + txfDB.getText());
                    cfg.setProperty("hibernate.connection.username", txfUser.getText());
                    cfg.setProperty("hibernate.connection.password", pwfPwd.getText());
                    DB.setSessionFactory(cfg.configure().buildSessionFactory());
                    String url = txfURL.getText();
                    if (url.startsWith(AidCloud.ADDRESS_PREFIX))
                        url = url.substring(AidCloud.ADDRESS_PREFIX.length());
                    if (url.endsWith("/"))
                        url = url.substring(0, url.length() - 1);
                    String port = txfPort.getText();
                    if (port == null || port.isEmpty())
                        port = String.valueOf(AidCloud.PORT);
                    String adr = String.format(AidCloud.ADDRESS, url, port);
                    Endpoint.publish(adr + "Login", new Login());
                    Endpoint.publish(adr + "LanguageProvider", new LanguageProvider());
                    Endpoint.publish(adr + "Translator", Translator.get());
                    Endpoint.publish(adr + "PersonProvider", new PersonProvider());
                    Endpoint.publish(adr + "AddressProvider", new AddressProvider());
                    Endpoint.publish(adr + "CountryProvider", new CountryProvider());
                    Endpoint.publish(adr + "ContactTypeProvider", new ContactTypeProvider());
                    Endpoint.publish(adr + "ContactInformationProvider", new ContactInformationProvider());
                    Platform.runLater(() -> {
                        Button btnExit = new Button("Exit");
                        btnExit.setDefaultButton(true);
                        btnExit.setOnAction(event -> System.exit(0));
                        primaryStage.setScene(new Scene(btnExit));
                    });
                    System.out.println("AidCloud is online now!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Platform.runLater(() -> {
                        primaryStage.setScene(setup);
                        new Alert(Alert.AlertType.ERROR, ex.getLocalizedMessage(), ButtonType.OK).showAndWait();
                    });
                }
            }).start();
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private class DBSetting {
        private String name;
        private String driverClass;
        private String dialect;
        private String jdbc;

        public DBSetting(String name, String driverClass, String dialect, String jdbc) {
            this.name = name;
            this.driverClass = driverClass;
            this.dialect = dialect;
            this.jdbc = jdbc;
        }

        public String getName() {
            return name;
        }

        public String getDriverClass() {
            return driverClass;
        }

        public String getDialect() {
            return dialect;
        }

        public String getJdbc() {
            return jdbc;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
