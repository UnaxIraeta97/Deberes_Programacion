package Hito5;

import org.jdesktop.swingx.JXDatePicker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class VisualizadorPinturas extends JFrame {

    JComboBox<Pintor> combo;
    JList<Pintura> lista;
    DefaultListModel<Pintura> modelo;
    JLabel imagen;
    JXDatePicker selectorFecha;
    // Nuevos botones
    JButton btnPremiar, btnEliminar;

    public VisualizadorPinturas() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modelo = new DefaultListModel<>();
        lista = new JList<>(modelo);

        combo = new JComboBox<>(DAO.obtenerPintores());
        combo.setPreferredSize(new Dimension(160, 25));

        selectorFecha = new JXDatePicker();
        selectorFecha.setPreferredSize(new Dimension(160, 25));

        imagen = new JLabel();
        imagen.setHorizontalAlignment(SwingConstants.CENTER);

        // --- DISEÑO ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 75, 5, 75));

        JPanel topLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        topLeft.add(new JLabel("Pintor:"));
        topLeft.add(combo);
        topPanel.add(topLeft, BorderLayout.WEST);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        topRight.add(new JLabel("Pinturas posteriores a:"));
        topRight.add(selectorFecha);
        topPanel.add(topRight, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 10, 15));

        JPanel bottomLeft = new JPanel(new BorderLayout());
        JScrollPane scrollLista = new JScrollPane(lista);
        scrollLista.setPreferredSize(new Dimension(280, 200));
        bottomLeft.add(scrollLista, BorderLayout.CENTER);
        bottomPanel.add(bottomLeft, BorderLayout.WEST);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imagePanel.setPreferredSize(new Dimension(350, 250));
        JPanel imageWrapper = new JPanel(new GridBagLayout());
        imageWrapper.add(imagen);
        imagePanel.add(imageWrapper, BorderLayout.CENTER);
        bottomPanel.add(imagePanel, BorderLayout.EAST);

        // --- NUEVO: PANEL DE BOTONES ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPremiar = new JButton("PREMIAR");
        btnEliminar = new JButton("ELIMINAR");
        panelBotones.add(btnPremiar);
        panelBotones.add(btnEliminar);
        bottomPanel.add(panelBotones, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.CENTER);

        // --- EVENTOS ---
        combo.addActionListener(e -> cargarPinturas());
        selectorFecha.addActionListener(e -> cargarPinturas());

        lista.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Pintura p = lista.getSelectedValue();
                    if (p != null) {
                        imagen.setIcon(cargarImagen("Files/Imagenes/" + p.getArchivo()));
                        DAO.incrementarVisitas(p);
                    }
                }
            }
        });

        // Evento Premiar
        btnPremiar.addActionListener(e -> {
            String nStr = JOptionPane.showInputDialog(this, "Numero minimo de visitas para ganar un premio:");
            if (nStr != null && !nStr.isEmpty()) {
                int n = Integer.parseInt(nStr);
                HashMap<Integer, Integer> mapa = DAO.crearMapaVisitas();
                Pintor[] pintores = DAO.obtenerPintores();

                for (Pintor p : pintores) {
                    if (mapa.getOrDefault(p.getId(), 0) >= n) {
                        DAO.premiarPintor(p.getId());
                    }
                }
                JOptionPane.showMessageDialog(this, "Proceso de premiación completado.");
            }
        });

        // Evento Eliminar
        btnEliminar.addActionListener(e -> {
            ArrayList<Pintura> candidatas = DAO.obtenerPinturasNoVistasYNoPremiados();
            for (Pintura p : candidatas) {
                int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar '" + p.getTitulo() + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    DAO.eliminarPintura(p.getId());
                }
            }

            int respPintores = JOptionPane.showConfirmDialog(this, "¿Eliminar pintores sin obras?", "Limpieza", JOptionPane.YES_NO_OPTION);
            if (respPintores == JOptionPane.YES_OPTION) {
                DAO.eliminarPintoresSinObras();
                combo.setModel(new DefaultComboBoxModel<>(DAO.obtenerPintores()));
            }
        });

        setSize(850, 550);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarPinturas() {
        modelo.clear();
        Pintor p = (Pintor) combo.getSelectedItem();
        if (p == null) return;
        String fecha = null;
        if (selectorFecha.getDate() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            fecha = formatter.format(selectorFecha.getDate());
        }
        ArrayList<Pintura> listaP = DAO.obtenerPinturas(p.getId(), fecha);
        for (Pintura pintura : listaP) {
            modelo.addElement(pintura);
        }
    }

    private ImageIcon cargarImagen(String ruta) {
        ImageIcon img = new ImageIcon(ruta);
        if (img.getIconWidth() == -1) return null;
        double ancho = img.getIconWidth();
        double alto = img.getIconHeight();
        if (ancho > alto) {
            alto = alto * (400.0 / ancho);
            ancho = 400;
        } else {
            ancho = ancho * (250.0 / alto);
            alto = 250;
        }
        return new ImageIcon(img.getImage().getScaledInstance((int) ancho, (int) alto, Image.SCALE_SMOOTH));
    }

    public static void main(String[] args) {
        new VisualizadorPinturas();
    }
}