package Hito4;

import java.sql.*;
import java.util.ArrayList;

public class DAO {

    public static Pintor[] obtenerPintores() {
        ArrayList<Pintor> lista = new ArrayList<>();

        try {
            Connection con = ConexionBD.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Pintores");

            while (rs.next()) {
                lista.add(new Pintor(
                        rs.getInt("IdPintor"),
                        rs.getString("Nombre"),
                        rs.getBoolean("Premiado")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista.toArray(new Pintor[0]);
    }

    public static ArrayList<Pintura> obtenerPinturas(int idPintor, String fecha) {
        ArrayList<Pintura> lista = new ArrayList<>();

        try {
            Connection con = ConexionBD.conectar();
            PreparedStatement ps;

            if (fecha == null) {
                ps = con.prepareStatement(
                        "SELECT * FROM Pinturas WHERE IdPintor = ?"
                );
                ps.setInt(1, idPintor);
            } else {
                ps = con.prepareStatement(
                        "SELECT * FROM Pinturas WHERE IdPintor = ? AND Fecha >= ?"
                );
                ps.setInt(1, idPintor);
                ps.setString(2, fecha);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Pintura(
                        rs.getInt("IdPintura"),
                        rs.getString("Titulo"),
                        rs.getString("Fecha"),
                        rs.getString("Archivo"),
                        rs.getInt("Visitas"),
                        rs.getInt("IdPintor")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static void incrementarVisitas(Pintura p) {
        try {
            Connection con = ConexionBD.conectar();

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE Pinturas SET Visitas = Visitas + 1 WHERE IdPintura = ?"
            );

            ps.setInt(1, p.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}