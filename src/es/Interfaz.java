package es;

/*import eus.KotxeKudeatzaile_db_swing;*/

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Interfaz extends JFrame
{
    private JTextArea texto;
    private JComboBox duenyosCombo;
    private JTextField duenyosTField;
    private JLabel duenyos2Label;
    private JList listaCoches;
    private JButton botonBuscar;
    private JLabel textoError;
    private JButton botonRevisar;
    private JButton botonLimpiar;
    private JButton botonInforme;



    private GestorCocheBD gestorBD;

    public static void main(String[] args)
    {
        Interfaz interfaz = new Interfaz();
    }

    public Interfaz()
    {
        gestorBD = new GestorCocheBD();

        this.setLayout(new GridLayout(1,2));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setSize(new Dimension(600, 300));
        this.setTitle("Gestor de Coches");

        this.add(parteIzquierda());
        this.add(parteDerecha());

        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                gestorBD.cerrar();
            }
        });

        this.pack();
        this.setVisible(true);
    }

    private JPanel parteIzquierda()
    {
        JPanel izquierda = new JPanel(new GridLayout(5,1));

        JPanel arribaIzq = new JPanel();
        arribaIzq.setBackground(Color.red);
        JPanel medioIzq = new JPanel();
        medioIzq.setBackground(Color.blue);
        JPanel medio2Izq = new JPanel();
        medio2Izq.setBackground(Color.green);
        JPanel abajoIzq = new JPanel();
        abajoIzq.setBackground(Color.ORANGE);
        
        //String[] elementos = gestorBD.obtenerDuenyos();
        //duenyosCombo = new JComboBox<>(elementos);
        List<String> elementos = gestorBD.obtenerDuenyos2();
        DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>();
        modeloCombo.addAll(elementos);
        duenyosCombo = new JComboBox<>(modeloCombo);

        duenyosCombo.setSelectedIndex(-1);
        duenyosCombo.setPreferredSize(new Dimension(200, 30));
        duenyosCombo.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (duenyosCombo.getSelectedIndex() != -1)
                {
                    texto.setText("");
                    duenyosTField.setText("");
                    medio2Izq.removeAll();
                    medio2Izq.revalidate();
                    medio2Izq.repaint();
                    String dni = (String) duenyosCombo.getSelectedItem();
                    dni = dni.split(" - ")[0];
                    DefaultListModel<String> modeloLista = new DefaultListModel<>();
                    modeloLista.addAll(gestorBD.obtenerCochesDuenyo(dni));
                    listaCoches.setModel(modeloLista);
                }
            }
        });
        arribaIzq.add(duenyosCombo);

        duenyos2Label = new JLabel("Introduce el DNI del dueño:");
        duenyosTField = new JTextField();
        duenyosTField.setPreferredSize(new Dimension(200, 30));
        duenyosTField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                texto.setText("");
                duenyosCombo.setSelectedIndex(-1);
                listaCoches.setModel(new DefaultListModel());
                String dni = duenyosTField.getText();
                DefaultListModel<String> modeloLista = new DefaultListModel<>();
                modeloLista.addAll(gestorBD.obtenerCochesDuenyo(dni));
                if (!modeloLista.isEmpty())
                {
                    medio2Izq.removeAll();
                    medio2Izq.revalidate();
                    medio2Izq.repaint();
                    listaCoches.setModel(modeloLista);
                }
                else
                {
                    medio2Izq.removeAll();
                    medio2Izq.revalidate();
                    medio2Izq.repaint();
                    textoError = new JLabel("No existe ningún dueño con ese DNI.");
                    textoError.setForeground(Color.RED);
                    medio2Izq.add(textoError);
                    medio2Izq.revalidate();
                    medio2Izq.repaint();
                }
            }
        });

        botonBuscar = new JButton("Buscar");
        botonBuscar.setPreferredSize(new Dimension(80,30));
        botonBuscar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                texto.setText("");
                duenyosCombo.setSelectedIndex(-1);
                listaCoches.setModel(new DefaultListModel());
                String dni = duenyosTField.getText();
                DefaultListModel<String> modeloLista = new DefaultListModel<>();
                modeloLista.addAll(gestorBD.obtenerCochesDuenyo(dni));
                if (!modeloLista.isEmpty())
                {
                    medio2Izq.removeAll();
                    medio2Izq.revalidate();
                    medio2Izq.repaint();
                    listaCoches.setModel(modeloLista);
                }
                else
                {
                    medio2Izq.removeAll();
                    medio2Izq.revalidate();
                    medio2Izq.repaint();
                    textoError = new JLabel("No existe ningún dueño con ese DNI.");
                    textoError.setForeground(Color.RED);
                    medio2Izq.add(textoError);
                    medio2Izq.revalidate();
                    medio2Izq.repaint();
                }
            }
        });

        medioIzq.add(duenyos2Label);
        medioIzq.add(duenyosTField);
        medioIzq.add(botonBuscar);

        listaCoches = new JList();
        listaCoches.setSize(new Dimension(150, 100));
        listaCoches.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    String matricula = (String) listaCoches.getSelectedValue();
                    matricula = matricula.split(" -")[0];
                    texto.setText(gestorBD.obtenerInfoCoche(matricula));
                }
            }
        });

        botonRevisar = new JButton("Revisar");
        abajoIzq.add(botonRevisar);

        botonLimpiar = new JButton("Limpiar");
        abajoIzq.add(botonLimpiar);

        botonInforme = new JButton("Informe");
        abajoIzq.add(botonInforme);



        izquierda.add(arribaIzq);
        izquierda.add(medioIzq);
        izquierda.add(new JScrollPane(listaCoches));
        izquierda.add(medio2Izq);
        izquierda.add(abajoIzq);

        return izquierda;
    }

    private JPanel parteDerecha()
    {
        JPanel derecha = new JPanel();
        derecha.setLayout(new BoxLayout(derecha, BoxLayout.Y_AXIS));

        texto = new JTextArea(25, 30);
        texto.setAlignmentX(Component.LEFT_ALIGNMENT);
        texto.setEditable(false);
        texto.setBackground(Color.WHITE);
        texto.setBorder(new LineBorder(Color.BLACK));
        JScrollPane scrolla = new JScrollPane();
        scrolla.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrolla.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolla.setViewportView(texto);
        derecha.add(scrolla);

        JButton botonCerrar = new JButton("Cerrar");
        botonCerrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        botonCerrar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                gestorBD.cerrar();
                dispose();
                //System.exit(0); -> Este mejor evitar
            }
        });
        derecha.add(botonCerrar);

        return derecha;
    }
}
