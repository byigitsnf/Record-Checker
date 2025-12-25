package src;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class RecordStore {

    private static final Path APP_DIR =
        Paths.get(System.getProperty("user.home"), "AppData", "Local", "KayitUygulamasi");

    private static final Path FILE =
        APP_DIR.resolve("records.txt");

    static {
        try {
            if (!Files.exists(APP_DIR)) {
                Files.createDirectories(APP_DIR);
            }
            if (!Files.exists(FILE)) {
                Files.createFile(FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ================= RAW ================= */

    private static List<String> loadRaw() {
        try {
            return Files.readAllLines(FILE);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private static void saveRaw(List<String> lines) {
        try {
            Files.write(FILE, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ================= ADD ================= */

    public static boolean add(String tc, String address, boolean hasCopy) {
        List<String> raw = loadRaw();

        boolean exists = raw.stream()
                .anyMatch(line -> line.startsWith(tc + ";"));

        if (exists) return false;

        raw.add(tc + ";" + address + ";" + hasCopy);
        saveRaw(raw);
        return true;
    }

    /* ================= SEARCH ================= */

    public static Optional<String> search(String tc, String address) {
        return loadRaw().stream()
                .filter(line ->
                        (!tc.isBlank() && line.startsWith(tc + ";")) ||
                        (!address.isBlank() && line.contains(";" + address + ";"))
                )
                .map(RecordStore::format)
                .findFirst();
    }

    /* ================= LOAD ALL ================= */

    public static List<String> loadAll() {
        List<String> list = new ArrayList<>();
        for (String r : loadRaw()) {
            list.add(format(r));
        }
        return list;
    }

    /* ================= DELETE ================= */

    public static void deleteRecords(List<String> formattedRecords) {

        Set<String> tcSet = new HashSet<>();

        for (String f : formattedRecords) {
            extractTC(f).ifPresent(tcSet::add);
        }

        List<String> raw = loadRaw();
        raw.removeIf(line -> {
            for (String tc : tcSet) {
                if (line.startsWith(tc + ";")) return true;
            }
            return false;
        });

        saveRaw(raw);
    }

    /* ================= FORMAT ================= */

    private static String format(String raw) {
        String[] p = raw.split(";");
        if (p.length < 3) return raw;

        return "Kimlik: " + p[0] +
               " | Adres: " + (p[1].isBlank() ? "-" : capitalize(p[1])) +
               " | Fotokopi: " + (Boolean.parseBoolean(p[2]) ? "Var" : "Yok");
    }

    public static Optional<String> extractTC(String formatted) {
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

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
}

