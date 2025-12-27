package src;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RecordsFrame extends JFrame {

    private JPanel listPanel;

    public RecordsFrame() {
        setTitle("Kayıtlar");
        setSize(650, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        loadNormal();

        JScrollPane scroll = new JScrollPane(listPanel);

        JButton sortBtn = new JButton("TC’ye Göre Sırala");
        JButton deleteBtn = new JButton("Seçilenleri Sil");

        sortBtn.addActionListener(e -> loadSorted());
        deleteBtn.addActionListener(e -> deleteSelected());

        JPanel bottom = new JPanel();
        bottom.add(sortBtn);
        bottom.add(deleteBtn);

        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void loadNormal() {
        listPanel.removeAll();
        for (String r : RecordStore.loadAll()) {
            listPanel.add(new JCheckBox(r));
        }
        refresh();
    }

    private void loadSorted() {
        listPanel.removeAll();
        for (String r : RecordStore.loadSortedByTC()) {
            listPanel.add(new JCheckBox(r));
        }
        refresh();
    }

    private void deleteSelected() {
        List<String> selected = new ArrayList<>();
        for (Component c : listPanel.getComponents()) {
            JCheckBox cb = (JCheckBox) c;
            if (cb.isSelected()) selected.add(cb.getText());
        }

        RecordStore.deleteRecords(selected);
        JOptionPane.showMessageDialog(this, "Seçilen kayıtlar silindi");
        loadNormal();
    }

    private void refresh() {
        listPanel.revalidate();
        listPanel.repaint();
    }
}
