package src;
import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Kayıt Sistemi v1.1");
        setSize(450, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {

        JTextField tcField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JCheckBox photoCheck = new JCheckBox("Kimlik fotokopisi var");

        JButton addBtn = new JButton("Ekle");
        JButton searchBtn = new JButton("Sorgula");
        JButton recordsBtn = new JButton("Kayıtlar");

        JPanel panel = new JPanel(new GridLayout(0,1,5,5));

        panel.add(new JLabel("Kimlik Numarası:"));
        panel.add(tcField);
        panel.add(new JLabel("Ad Soyad:"));
        panel.add(nameField);
        panel.add(new JLabel("Adres:"));
        panel.add(addressField);
        panel.add(photoCheck);
        panel.add(addBtn);
        panel.add(searchBtn);
        panel.add(recordsBtn);

        add(panel);

        // EKLE
        addBtn.addActionListener(e -> {
            String tc = tcField.getText().trim();
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            boolean hasCopy = photoCheck.isSelected();

            if (!tc.matches("\\d{11}") ||
                Character.getNumericValue(tc.charAt(10)) % 2 != 0) {

                JOptionPane.showMessageDialog(this,
                        "TC 11 haneli ve son hanesi çift olmalıdır",
                        "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean added = RecordStore.add(tc, name, address, hasCopy);

            JOptionPane.showMessageDialog(this,
                    added ? "Kayıt eklendi" : "Bu TC zaten kayıtlı");

            tcField.setText("");
            nameField.setText("");
            addressField.setText("");
            photoCheck.setSelected(false);
        });

        // SORGULA
        searchBtn.addActionListener(e -> {
            Optional<String> result =
                    RecordStore.search(tcField.getText().trim(),
                                       addressField.getText().trim());

            JOptionPane.showMessageDialog(this,
                    result.orElse("Kayıt bulunamadı"));
        });

        // KAYITLAR
        recordsBtn.addActionListener(e ->
                new RecordsFrame().setVisible(true)
        );
    }
}
