package lab;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;
import org.h2.tools.Server;

public class DatabaseControl {

    private static Server server;

    public static void startDBWebServer() {
        // Start HTTP server for access H2 DB for look inside
        updateH2Properties();
        try {
            server = Server.createWebServer();
            System.out.println(server.getURL());
            server.start();
            System.out.println("DB Web server started!");
        } catch (SQLException e) {
            System.out.println("Cannot create DB web server.");
            e.printStackTrace();
        }
    }

    private static void updateH2Properties() {
        Path h2ServerProperties = Paths.get(System.getProperty("user.home"), ".h2.server.properties");
        Properties h2Properties = new Properties();
        try (Reader reader = new FileReader(h2ServerProperties.toFile())){
            h2Properties.load(reader);
        } catch (IOException e) {
            System.out.println("Cannot read h2 web console properties file.");
            e.printStackTrace();
        }
        int index = getLastConnectionIndex(h2Properties);
        String newConnectionString = "Generic H2 (Embedded)|org.h2.Driver|jdbc:h2:file:./db/score-db|";
        Object data = h2Properties.get(Integer.toString(index));
        String actualConnection = data != null?data.toString():"";
        if(!newConnectionString.equals(actualConnection)){
            h2Properties.put(Integer.toString(index+1), newConnectionString);
            try (Writer writer = new FileWriter(h2ServerProperties.toFile())){
                h2Properties.store(writer, "H2 Server Properties");
            } catch (IOException e) {
                System.out.println("Cannot write h2 web console properties file.");
                e.printStackTrace();
            }
        }
    }
    private static int getLastConnectionIndex(Properties h2Prop){
        int index = 0;
        while (h2Prop.getProperty(Integer.toString(index)) != null){
            index++;
        }
        return index-1;
    }

    public static void waitForKeyAndStopDBWebServer() {
        waitForKeyPress();
        stopDBWebServer();
    }

    public static void stopDBWebServer() {
        // Stop HTTP server for access H2 DB
        System.out.println("Ending DB web server BYE.");
        server.stop();
    }

    public static void waitForKeyPress() {
        System.out.println("Waiting for Key press (ENTER)");
        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println("Cannot read input from keyboard.");
            e.printStackTrace();
        }
    }

}
