package Hito5;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DAO {

    // ... (tus métodos obtenerPintores, obtenerPinturas e incrementarVisitas se mantienen igual)

    public static Pintor[] obtenerPintores() {
        ArrayList<Pintor> lista = new ArrayList<>();
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Pintores")) {
            while (rs.next()) {
                lista.add(new Pintor(rs.getInt("IdPintor"), rs.getString("Nombre"), rs.getBoolean("Premiado")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista.toArray(new Pintor[0]);
    }

    public static ArrayList<Pintura> obtenerPinturas(int idPintor, String fecha) {
        ArrayList<Pintura> lista = new ArrayList<>();
        try (Connection con = ConexionBD.conectar()) {
            PreparedStatement ps = (fecha == null) ?
                    con.prepareStatement("SELECT * FROM Pinturas WHERE IdPintor = ?") :
                    con.prepareStatement("SELECT * FROM Pinturas WHERE IdPintor = ? AND Fecha >= ?");
            ps.setInt(1, idPintor);
            if (fecha != null) ps.setString(2, fecha);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Pintura(rs.getInt("IdPintura"), rs.getString("Titulo"), rs.getString("Fecha"),
                        rs.getString("Archivo"), rs.getInt("Visitas"), rs.getInt("IdPintor")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public static void incrementarVisitas(Pintura p) {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement("UPDATE Pinturas SET Visitas = Visitas + 1 WHERE IdPintura = ?")) {
            ps.setInt(1, p.getId());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- NUEVOS MÉTODOS REQUERIDOS ---

    public static HashMap<Integer, Integer> crearMapaVisitas() {
        HashMap<Integer, Integer> mapa = new HashMap<>();
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT IdPintor, Visitas FROM Pinturas")) {
            while (rs.next()) {
                int id = rs.getInt("IdPintor");
                int v = rs.getInt("Visitas");
                mapa.put(id, mapa.getOrDefault(id, 0) + v);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return mapa;
    }

    public static void premiarPintor(int idPintor) {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement("UPDATE Pintores SET Premiado = 1 WHERE IdPintor = ?")) {
            ps.setInt(1, idPintor);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static ArrayList<Pintura> obtenerPinturasNoVistasYNoPremiados() {
        ArrayList<Pintura> lista = new ArrayList<>();
        String sql = "SELECT pi.* FROM Pinturas pi JOIN Pintores pr ON pi.IdPintor = pr.IdPintor " +
                "WHERE pi.Visitas = 0 AND pr.Premiado = 0";
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Pintura(rs.getInt("IdPintura"), rs.getString("Titulo"), rs.getString("Fecha"),
                        rs.getString("Archivo"), rs.getInt("Visitas"), rs.getInt("IdPintor")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public static void eliminarPintura(int idPintura) {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement("DELETE FROM Pinturas WHERE IdPintura = ?")) {
            ps.setInt(1, idPintura);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void eliminarPintoresSinObras() {
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement()) {
            st.executeUpdate("DELETE FROM Pintores WHERE IdPintor NOT IN (SELECT DISTINCT IdPintor FROM Pinturas)");
        } catch (Exception e) { e.printStackTrace(); }
    }
}