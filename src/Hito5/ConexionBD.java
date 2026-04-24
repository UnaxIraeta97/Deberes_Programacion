package Hito5;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {
    public static Connection conectar() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/galeria?useSSL=false&serverTimezone=UTC",
                    "root",
                    "root"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}