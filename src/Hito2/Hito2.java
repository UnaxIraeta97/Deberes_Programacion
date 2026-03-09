package Hito2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Hito2 extends JFrame implements ItemListener, ActionListener {

    JComboBox<String> comboArchivos;
    JTextArea areaTexto;
    JButton btnBorrar, btnCerrar;

    public Hito2() {
        super("Test Events: Files");

        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        comboArchivos = new JComboBox<>(new String[]{"python.txt", "c.txt", "java.txt"});
        comboArchivos.addItemListener(this);
        btnBorrar = new JButton("Borrar");
        panelIzq.add(new JLabel("Archivo:"));
        panelIzq.add(comboArchivos);
        panelIzq.add(btnBorrar);

        btnBorrar.addActionListener(e -> areaTexto.setText(""));

        JPanel panelDer = new JPanel(new BorderLayout(10, 10));
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaTexto);
        panelDer.add(scroll, BorderLayout.CENTER);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(this);
        JPanel panelCerrar = new JPanel();
        panelCerrar.add(btnCerrar);
        panelDer.add(panelCerrar, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzq, panelDer);
        split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> split.setDividerLocation(getWidth() / 2));
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String archivo = (String) comboArchivos.getSelectedItem();
            File f = new File("Files/" + archivo);
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                StringBuilder sb = new StringBuilder();
                String linea;
                while ((linea = br.readLine()) != null) {
                    sb.append(linea).append("\n");
                }
                areaTexto.setText(sb.toString());
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this,
                        "El archivo " + archivo + " no existe.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                areaTexto.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error leyendo el archivo " + archivo,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                areaTexto.setText("");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCerrar) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Hito2 ventana = new Hito2();
            ventana.setVisible(true);
        });
    }
}