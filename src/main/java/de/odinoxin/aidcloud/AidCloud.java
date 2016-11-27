package de.odinoxin.aidcloud;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
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

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.Endpoint;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

public class AidCloud extends Application {
    public static final String INVALID_SESSION = "Invalid session, or session is over!";
    private static final int PORT = 15123;

    private static final String KEY_PWD = "AidWare_AidCloud";

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

                    HashMap<String, Object> providers = new HashMap<>();
                    providers.put("Login", new Login());
                    providers.put("LanguageProvider", new LanguageProvider());
                    providers.put("Translator", Translator.get());
                    providers.put("PersonProvider", new PersonProvider());
                    providers.put("AddressProvider", new AddressProvider());
                    providers.put("CountryProvider", new CountryProvider());
                    providers.put("ContactTypeProvider", new ContactTypeProvider());
                    providers.put("ContactInformationProvider", new ContactInformationProvider());

                    // BEGIN HTTPS
                    String url = txfURL.getText();
                    int port = Integer.parseInt(txfPort.getText());
                    SSLContext ssl = SSLContext.getInstance("TLS");
                    KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    KeyStore store = KeyStore.getInstance("JKS");
                    store.load(AidCloud.class.getResource("/AidCloudCertificate").openStream(), KEY_PWD.toCharArray());
                    keyFactory.init(store, KEY_PWD.toCharArray());
                    TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    trustFactory.init(store);
                    ssl.init(keyFactory.getKeyManagers(), trustFactory.getTrustManagers(), new SecureRandom());
                    HttpsConfigurator configurator = new HttpsConfigurator(ssl);
                    HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(url, port), port);
                    httpsServer.setHttpsConfigurator(configurator);
                    httpsServer.start();
                    Iterator<String> it = providers.keySet().iterator();
                    String key;
                    while (it.hasNext()) {
                        key = it.next();
                        HttpContext httpContext = httpsServer.createContext("/AidCloud/" + key);
                        Endpoint endpoint = Endpoint.create(providers.get(key));
                        endpoint.publish(httpContext);
                    }
                    // END HTTPS
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
