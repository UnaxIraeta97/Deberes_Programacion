package es;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorCocheBD
{
    final String DB_IP = "localhost";
    final String DB_NOMBRE = "cochebd";
    /*final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    final String DB_URL = "jdbc:mariadb://" + DB_IP + ":3306/" + DB_NOMBRE;*/

    final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    final String DB_URL = "jdbc:mysql://" + DB_IP + ":3306/" + DB_NOMBRE;

    // Credenciales de la BD
    final String USUARIO = "root";
    final String CONTRASENA = "root";

    private Connection conn;

    public GestorCocheBD()
    {
        try
        {
            Class.forName(JDBC_DRIVER);
            System.out.println("Conectando a la base de datos...");
            conn = DriverManager.getConnection(DB_URL, USUARIO, CONTRASENA);
            System.out.println("Conectado.");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void cerrar() {
        try {
            conn.close();
            System.out.println("Desconectado de la base de datos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] obtenerDuenyos()
    {
        try
        {
            String sql = "SELECT duenyos.* FROM duenyos";
            Statement consulta = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultado = consulta.executeQuery(sql);
            int cont = 0;
            while (resultado.next())
            {
                cont++;
            }
            String[] duenyos = new String[cont];
            resultado.beforeFirst();
            cont = 0;
            while (resultado.next())
            {
                duenyos[cont] = resultado.getString("DNI") + " - " +  resultado.getString("Nombre");
                cont++;
            }
            return duenyos;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> obtenerDuenyos2()
    {
        List<String> lista = new ArrayList<>();
        try
        {
            String sql = "SELECT duenyos.* FROM duenyos";
            Statement consulta = conn.createStatement();
            ResultSet resultado = consulta.executeQuery(sql);
            while (resultado.next())
            {
                lista.add(resultado.getString("DNI") + " - " +  resultado.getString("Nombre"));
            }
            return lista;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return lista;
        }
    }

    public List<String> obtenerCochesDuenyo(String dni)
    {
        List<String> coches = new ArrayList<>();
        try
        {
            String sql = "SELECT coches.* FROM coches WHERE DNI = ?";
            PreparedStatement consulta = conn.prepareStatement(sql);
            consulta.setString(1,dni);
            ResultSet resultado = consulta.executeQuery();
            while (resultado.next())
            {
                coches.add(resultado.getString("Matricula") + " - " + resultado.getString("Marca"));
            }
            return coches;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return coches;
        }
    }

    public String obtenerInfoCoche(String matricula)
    {
        try
        {
            String sql = "SELECT coches.* FROM coches WHERE Matricula = ?";
            PreparedStatement consulta = conn.prepareStatement(sql);
            consulta.setString(1,matricula);
            ResultSet resultado = consulta.executeQuery();
            resultado.next();
            return resultado.getString("Matricula") + " | " + resultado.getString("Marca") + " | " + resultado.getInt("Precio");
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Map<String, Integer> mapDeRevisiones(){
        Map<String, Integer> map = new HashMap<>();
        try {
            PreparedStatement select = conn.prepareStatement("SELECT * FROM Coches");
            ResultSet rs = select.executeQuery();

            while (rs.next()){
                String dni = rs.getString("DNI");
                if (!map.containsKey(dni)){
                    map.put(dni, 0);
                }
                if (rs.getBoolean("Revision")){
                 map.put(dni,map.get(dni)+1);
                }
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return map;
    }

    public static void main(String[] args) {
        GestorCocheBD gestor = new GestorCocheBD();
        Map<String, Integer> map = gestor.mapDeRevisiones();
        for (Map.Entry<String,Integer> entry:map.entrySet()) {
            System.out.println("DNI "+ entry.getKey() + "Revisiones: "+ entry.getValue());
        }
    }

}
