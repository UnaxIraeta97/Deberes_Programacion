package Hito3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Hito3 extends JFrame implements ActionListener {

    private static final String RUTA = "Files/fotos/";
    private static final String PASS = "damocles";

    JComboBox<String> combo;
    JLabel imagen;
    JTextField texto;
    JCheckBox check;
    JButton guardar;

    public Hito3() {

        String p = JOptionPane.showInputDialog("Introduce contraseña");
        if (p == null || !p.equals(PASS)) System.exit(0);

        setTitle("Hito3");
        setSize(500, 400);
        setLayout(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(null, "¡Adiós!");
                System.exit(0);
            }
        });

        combo = new JComboBox<>();
        combo.setBounds(20, 20, 200, 25);
        cargarCombo();
        combo.addActionListener(new ComboListener());
        add(combo);

        imagen = new JLabel();
        imagen.setBounds(20, 60, 250, 200);
        imagen.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(imagen);

        check = new JCheckBox("Guardar comentario", true);
        check.setBounds(20, 280, 180, 25);
        add(check);

        texto = new JTextField();
        texto.setBounds(200, 280, 200, 25);
        add(texto);

        guardar = new JButton("Guardar");
        guardar.setBounds(180, 320, 100, 30);
        guardar.addActionListener(this);
        add(guardar);

        if (combo.getItemCount() > 0) {
            cambiarImagen(combo.getItemAt(0));
        }

        setVisible(true);
    }

    public void cargarCombo() {
        File f = new File(RUTA);
        String[] lista = f.list((dir, name) -> name.endsWith(".jpg"));
        if (lista != null) {
            for (String s : lista) combo.addItem(s);
        }
    }

    public void cambiarImagen(String nombre) {
        ImageIcon icon = new ImageIcon(RUTA + nombre);
        Image img = icon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
        imagen.setIcon(new ImageIcon(img));
    }

    public void actionPerformed(ActionEvent e) {
        if (check.isSelected()) {
            String nombre = (String) combo.getSelectedItem();
            String comentario = texto.getText();

            assert nombre != null;
            String txt = nombre.substring(0, nombre.lastIndexOf(".")) + ".txt";

            try {
                PrintWriter pw = new PrintWriter(new FileWriter(RUTA + txt, true));
                pw.println(nombre + " " + comentario);
                pw.close();
                texto.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error");
            }
        }
    }

    class ComboListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String sel = (String) combo.getSelectedItem();
            if (sel != null) cambiarImagen(sel);
        }
    }

    public static void main(String[] args) {
        new Hito3();
    }
}