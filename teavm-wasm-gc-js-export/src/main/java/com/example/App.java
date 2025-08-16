package com.example;

import java.util.*;
import org.teavm.jso.JSExport;

/**
 * TeaVM WebAssembly GC interactive demo.
 * main(String[]) intentionally does nothing; all features are exposed via exported methods.
 */
public class App {
    public static void main(String[] args) {
        // Intentionally empty – invoked functions are exported and called from JS.
    }

    // ===== Primitive operations =====
    @JSExport
    public static int add(int a, int b) { return a + b; }

    @JSExport
    public static boolean isGreater(int a, int b) { return a > b; }

    // ===== String operations =====
    @JSExport
    public static String concat(String a, String b) { return a + " " + b + "!"; }

    @JSExport
    public static String substring(String s, int begin, int end) {
        if (s == null) return "";
        int b = Math.max(0, Math.min(begin, s.length()));
        int e = Math.max(b, Math.min(end, s.length()));
        return s.substring(b, e);
    }

    // ===== List operations (CSV-based interop kept simple) =====
    @JSExport
    public static String sortListCSV(String csv) {
        List<String> list = tokenize(csv);
        Collections.sort(list);
        return String.join(",", list);
    }

    @JSExport
    public static String listGetAt(String csv, int index) {
        List<String> list = tokenize(csv);
        if (index < 0 || index >= list.size()) return "";
        return list.get(index);
    }

    // ===== Set operations (CSV-based) =====
    @JSExport
    public static String sortSetCSV(String csv) {
        Set<String> set = new HashSet<>(tokenize(csv));
        List<String> list = new ArrayList<>(set);
        Collections.sort(list);
        return String.join(",", list);
    }

    @JSExport
    public static boolean setContains(String csv, String value) {
        Set<String> set = new HashSet<>(tokenize(csv));
        return set.contains(value);
    }

    // ===== Map cache =====
    private static final Map<String, String> CACHE = new HashMap<>();

    @JSExport
    public static String cachePut(String key, String value) {
        String prev = CACHE.put(key, value);
        return prev == null ? "" : prev;
    }

    @JSExport
    public static String cacheGet(String key) {
        String val = CACHE.get(key);
        return val == null ? "" : val;
    }

    // ===== Helpers =====
    private static List<String> tokenize(String csv) {
        List<String> out = new ArrayList<>();
        if (csv == null || csv.isEmpty()) return out;
        for (String part : csv.split(",")) {
            String t = part.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }
}
