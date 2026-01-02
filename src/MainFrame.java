package src;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Kayıt Sistemi v1.1");
        setSize(480, 400);
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
        JButton tcSearchBtn = new JButton("TC Sorgula");
        JButton nameSearchBtn = new JButton("İsim Sorgula");
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
        panel.add(tcSearchBtn);
        panel.add(nameSearchBtn);
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

        // TC SORGULA (STRICT)
        tcSearchBtn.addActionListener(e -> {
            Optional<String> r =
                RecordStore.searchByTC(tcField.getText().trim());

            JOptionPane.showMessageDialog(this,
                    r.orElse("Kayıt bulunamadı"));
        });

        // İSİM SORGULA (ESNEK + ÇOKLU)
        nameSearchBtn.addActionListener(e -> {
            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "İsim giriniz");
                return;
            }

            List<String> results =
                RecordStore.searchByName(name);

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Kayıt bulunamadı");
                return;
            }

            JTextArea area = new JTextArea();
            area.setEditable(false);
            for (String r : results)
                area.append(r + "\n");

            JOptionPane.showMessageDialog(this,
                    new JScrollPane(area),
                    "İsim Sorgu Sonuçları",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // KAYITLAR
        recordsBtn.addActionListener(e ->
                new RecordsFrame().setVisible(true)
        );
    }
}
