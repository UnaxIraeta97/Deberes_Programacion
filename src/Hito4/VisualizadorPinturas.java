package Hito4;
import javax.swing.*;
import org.jdesktop.swingx.JXDatePicker;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class VisualizadorPinturas extends JFrame {

    JComboBox<Pintor> combo;
    JList<Pintura> lista;
    DefaultListModel<Pintura> modelo;
    JLabel imagen;
    JXDatePicker selectorFecha;

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

        add(new Box.Filler(new Dimension(0,0), new Dimension(0,0), new Dimension(0, 32767)), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 10, 15));

        JPanel bottomLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JScrollPane scrollLista = new JScrollPane(lista);
        scrollLista.setPreferredSize(new Dimension(280, 200));
        bottomLeft.add(scrollLista);
        bottomPanel.add(bottomLeft, BorderLayout.WEST);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imagePanel.setPreferredSize(new Dimension(350, 250));

        JPanel imageWrapper = new JPanel(new GridBagLayout());
        imageWrapper.add(imagen);
        imagePanel.add(imageWrapper, BorderLayout.CENTER);

        bottomPanel.add(imagePanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        combo.addActionListener(e -> cargarPinturas());

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

        setSize(850, 550);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarPinturas() {

        modelo.clear();
        Pintor p = (Pintor) combo.getSelectedItem();
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

        ImageIcon imagen = new ImageIcon(ruta);

        double ancho = imagen.getImage().getWidth(null);
        double alto = imagen.getImage().getHeight(null);

        if (ancho > alto) {
            alto = imagen.getImage().getHeight(null) * ((double) 400 / imagen.getImage().getWidth(null));
            ancho = 400;
        } else {
            ancho = imagen.getImage().getWidth(null) * ((double) 250 / imagen.getImage().getHeight(null));
            alto = 250;
        }

        return new ImageIcon(
                imagen.getImage().getScaledInstance((int) ancho, (int) alto, Image.SCALE_DEFAULT)
        );
    }

    public static void main(String[] args) {
        new VisualizadorPinturas();
    }
}