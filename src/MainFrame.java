package src;
import javax.swing.*;

import java.awt.*;
import java.util.Optional;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Kayıt Sistemi");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JTextField tcField = new JTextField();
        JTextField addressField = new JTextField();

        JCheckBox photoCheckBox = new JCheckBox("Kimlik fotokopisi var");

        JButton addBtn = new JButton("Ekle");
        JButton searchBtn = new JButton("Sorgula");
        JButton recordsBtn = new JButton("Kayıtlar");

        JPanel panel = new JPanel(new GridLayout(8, 1, 5, 5));
        panel.add(new JLabel("Kimlik Numarası:"));
        panel.add(tcField);
        panel.add(new JLabel("Adres:"));
        panel.add(addressField);
        panel.add(photoCheckBox);
        panel.add(addBtn);
        panel.add(searchBtn);
        panel.add(recordsBtn);

        add(panel, BorderLayout.CENTER);

        // ================= EKLE =================
        addBtn.addActionListener(e -> {
            String tc = tcField.getText().trim();
            String address = addressField.getText().trim();
            boolean hasPhoto = photoCheckBox.isSelected();

            if (tc.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Kimlik numarası boş olamaz.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!tc.matches("\\d{11}")) {
                JOptionPane.showMessageDialog(this,
                        "Kimlik numarası 11 haneli ve sadece rakam olmalıdır.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int lastDigit = Character.getNumericValue(tc.charAt(10));
            if (lastDigit % 2 != 0) {
                JOptionPane.showMessageDialog(this,
                        "Kimlik numarasının son hanesi çift olmalıdır.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean added = RecordStore.add(tc, address, hasPhoto);

            if (!added) {
                JOptionPane.showMessageDialog(this,
                        "Bu kimlik numarası zaten kayıtlı.",
                        "Uyarı",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Kayıt başarıyla eklendi.",
                        "Bilgi",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            tcField.setText("");
            addressField.setText("");
            photoCheckBox.setSelected(false);
        });

        // ================= SORGULA =================
        searchBtn.addActionListener(e -> {
            String tc = tcField.getText().trim();
            String address = addressField.getText().trim();

            if (tc.isEmpty() && address.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Kimlik numarası veya adres giriniz.",
                        "Uyarı",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Optional<String> result = RecordStore.search(tc, address);

            if (result.isEmpty()) {
                Object[] options = { "Ekle", "İptal" };
                int choice = JOptionPane.showOptionDialog(
                        this,
                        "Kayıt bulunamadı.\nYeni kayıt eklemek ister misiniz?",
                        "Kayıt Yok",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (choice != JOptionPane.YES_OPTION) {
                    tcField.setText("");
                    addressField.setText("");
                }
                return;
            }

            String record = result.get();

            if (record.contains(";;")) {
                JOptionPane.showMessageDialog(this,
                        "Kayıt bulundu ancak adres bilgisi yok:\n\n" + record,
                        "Sonuç",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Kayıt bulundu:\n\n" + record,
                        "Sonuç",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            tcField.setText("");
            addressField.setText("");
        });

        // ================= KAYITLAR =================
        recordsBtn.addActionListener(e -> {
            new RecordsFrame().setVisible(true);
        });
    }
}
