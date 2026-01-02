package src;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class RecordStore {

    private static final Path APP_DIR =
        Paths.get(System.getProperty("user.home"),
                  "AppData","Local","KayitUygulamasi");

    private static final Path FILE = APP_DIR.resolve("records.txt");

    static {
        try {
            if (!Files.exists(APP_DIR)) Files.createDirectories(APP_DIR);
            if (!Files.exists(FILE)) Files.createFile(FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------- HAM ----------------
    private static List<String> loadRaw() {
        try {
            return Files.readAllLines(FILE);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private static void saveRaw(List<String> l) {
        try {
            Files.write(FILE, l);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------- EKLE ----------------
    public static boolean add(String tc, String name,
                              String address, boolean hasCopy) {

        List<String> raw = loadRaw();
        if (raw.stream().anyMatch(r -> r.startsWith(tc + ";")))
            return false;

        raw.add(tc + ";" + name + ";" + address + ";" + hasCopy);
        saveRaw(raw);
        return true;
    }

    // ---------------- TC SORGULA (STRICT) ----------------
    public static Optional<String> searchByTC(String tc) {
        return loadRaw().stream()
            .filter(r -> r.startsWith(tc + ";"))
            .map(RecordStore::format)
            .findFirst();
    }

    // ---------------- İSİM SORGULA (ESNEK) ----------------
    public static List<String> searchByName(String name) {
        String q = name.toLowerCase();

        List<String> out = new ArrayList<>();

        for (String r : loadRaw()) {
            String[] p = r.split(";");
            if (p.length < 2) continue;

            if (p[1].toLowerCase().contains(q)) {
                out.add(format(r));
            }
        }
        return out;
    }

    // ---------------- NORMAL LİSTE ----------------
    public static List<String> loadAll() {
        List<String> out = new ArrayList<>();
        for (String r : loadRaw()) out.add(format(r));
        return out;
    }

    // ---------------- TC'YE GÖRE SIRALI ----------------
    public static List<String> loadSortedByTC() {
        List<String> raw = loadRaw();

        raw.sort(Comparator.comparingLong(
            r -> Long.parseLong(r.split(";")[0])
        ));

        List<String> out = new ArrayList<>();
        for (String r : raw) out.add(format(r));
        return out;
    }

    // ---------------- SİL ----------------
    public static void deleteRecords(List<String> formatted) {
        Set<String> tcSet = new HashSet<>();

        for (String f : formatted) {
            extractTC(f).ifPresent(tcSet::add);
        }

        List<String> raw = loadRaw();
        raw.removeIf(r ->
            tcSet.stream().anyMatch(tc -> r.startsWith(tc + ";"))
        );

        saveRaw(raw);
    }

    // ---------------- FORMAT ----------------
    private static String format(String raw) {
        String[] p = raw.split(";");

        String tc = p[0];
        String name = p.length >= 4 ? p[1] : "";
        String address = p.length >= 4 ? p[2] : p[1];
        boolean hasCopy = Boolean.parseBoolean(p[p.length - 1]);

        return "Kimlik: " + tc +
               " | Ad Soyad: " + (name.isBlank() ? "-" : name) +
               " | Adres: " + (address.isBlank() ? "-" : address) +
               " | Fotokopi: " + (hasCopy ? "Var" : "Yok");
    }

    private static Optional<String> extractTC(String formatted) {
        try {
            return Optional.of(
                formatted.split("\\|")[0]
                         .replace("Kimlik:", "")
                         .trim()
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
