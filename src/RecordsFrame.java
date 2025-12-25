package src;
import javax.swing.*;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class RecordsFrame extends JFrame {

    private List<JCheckBox> checkBoxes = new ArrayList<>();

    public RecordsFrame() {

        setTitle("Kayıtlar");
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        // KAYITLARI YÜKLE
        List<String> records = RecordStore.loadAll();

        for (String record : records) {
            JCheckBox cb = new JCheckBox(record);
            checkBoxes.add(cb);
            listPanel.add(cb);
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);

        JButton deleteBtn = new JButton("Seçilenleri Sil");
        deleteBtn.addActionListener(e -> deleteSelected());

        add(scrollPane, BorderLayout.CENTER);
        add(deleteBtn, BorderLayout.SOUTH);
    }

    private void deleteSelected() {
        List<String> selected = new ArrayList<>();

        for (JCheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                selected.add(cb.getText());
            }
        }

        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Silmek için kayıt seçiniz.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // SİLME (VOID)
        RecordStore.deleteRecords(selected);

        JOptionPane.showMessageDialog(
                this,
                "Silinen kayıtlar:\n\n" + String.join("\n", selected),
                "Silme Tamamlandı",
                JOptionPane.INFORMATION_MESSAGE);

        // EKRANI YENİLE
        dispose();
        new RecordsFrame().setVisible(true);
    }
}

