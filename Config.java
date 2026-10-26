package com.familiasantana.client;

import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Config {
    public static final String[] NAMES = {
            // HUD
            "Marca", "FPS", "CPS", "Ping", "Coordenadas", "Direcao", "Relogio",
            "Keystrokes", "Armor HUD", "Efeitos", "Velocidade", "Memoria", "Data",
            "Sessao", "Rotacao", "Grafico FPS",
            // Combate
            "Combo", "Reach", "TNT", "Itens", "Flash de Dano",
            "Alvo", "Hits", "Abates", "Aviso Vida Baixa",
            // Utilidades
            "Toggle Sprint", "Zoom", "Fullbright",
            "Toggle Sneak", "Zoom Scroll", "Notificacoes",
            // Interface
            "Hotbar Moderna", "Barras de Status", "Mira Moderna", "Menus Modernos",
            // Aparencia
            "Fundo HUD", "Sombra", "Fonte Moderna", "Blur"
    };

    /** Modulos que comecam desligados na primeira vez. */
    private static final Set<String> OFF_BY_DEFAULT = new HashSet<String>(Arrays.asList(
            "Fullbright", "Velocidade", "Memoria", "Data",
            "Sessao", "Rotacao", "Grafico FPS", "Hits", "Abates", "Toggle Sneak"));

    public static final int[] ACCENTS = {
            0xFF7C5CFF, 0xFF2D9CFF, 0xFF2ED573, 0xFFFF4757, 0xFFFFA502, 0xFFFF6BCB, 0xFF18DCFF, 0xFFF5F5F7
    };

    private static final Map<String, Boolean> MAP = new HashMap<String, Boolean>();
    private static final Map<String, String> EXTRA = new HashMap<String, String>();
    private static final Set<String> RESERVED = new HashSet<String>();
    private static int accent = 0;
    private static File file;

    static {
        RESERVED.addAll(Arrays.asList(NAMES));
        RESERVED.add("accent");
    }

    public static void load() {
        file = new File(Minecraft.func_71410_x().field_71412_D, "config/familiasantana.properties");
        Properties p = new Properties();
        if (file.exists()) {
            try (InputStream in = new FileInputStream(file)) {
                p.load(in);
            } catch (IOException ignored) {
            }
        }
        for (String n : NAMES) {
            boolean def = !OFF_BY_DEFAULT.contains(n);
            MAP.put(n, Boolean.parseBoolean(p.getProperty(n, String.valueOf(def))));
        }
        for (String key : p.stringPropertyNames()) {
            if (!RESERVED.contains(key)) EXTRA.put(key, p.getProperty(key));
        }
        try {
            accent = Integer.parseInt(p.getProperty("accent", "0"));
        } catch (NumberFormatException e) {
            accent = 0;
        }
        if (accent < 0 || accent >= ACCENTS.length) accent = 0;
    }

    public static void save() {
        if (file == null) return;
        Properties p = new Properties();
        for (String n : NAMES) p.setProperty(n, String.valueOf(get(n)));
        p.setProperty("accent", String.valueOf(accent));
        for (Map.Entry<String, String> en : EXTRA.entrySet()) p.setProperty(en.getKey(), en.getValue());
        try {
            file.getParentFile().mkdirs();
            try (OutputStream out = new FileOutputStream(file)) {
                p.store(out, "Familia Santana Client");
            }
        } catch (IOException ignored) {
        }
    }

    // ---------- opcoes ligar/desligar ----------

    public static boolean get(String name) {
        Boolean b = MAP.get(name);
        return b != null && b;
    }

    public static void toggle(String name) {
        MAP.put(name, !get(name));
    }

    // ---------- valores extras (posicao, escala, mira...) ----------

    public static float getFloat(String key, float def) {
        String v = EXTRA.get(key);
        if (v == null) return def;
        try {
            return Float.parseFloat(v);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static void setFloat(String key, float value) {
        EXTRA.put(key, String.valueOf(value));
    }

    public static int getInt(String key, int def) {
        String v = EXTRA.get(key);
        if (v == null) return def;
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static void setInt(String key, int value) {
        EXTRA.put(key, String.valueOf(value));
    }

    public static boolean getFlag(String key, boolean def) {
        String v = EXTRA.get(key);
        if (v == null) return def;
        return v.equals("1");
    }

    public static void setFlag(String key, boolean value) {
        EXTRA.put(key, value ? "1" : "0");
    }

    public static void removePrefix(String prefix) {
        Iterator<String> it = EXTRA.keySet().iterator();
        while (it.hasNext()) {
            if (it.next().startsWith(prefix)) it.remove();
        }
    }

    // ---------- aparencia ----------

    public static int accent() {
        return ACCENTS[accent];
    }

    public static int accentIndex() {
        return accent;
    }

    public static void setAccent(int i) {
        if (i >= 0 && i < ACCENTS.length) accent = i;
    }

    /** Cor de fundo dos paineis do HUD (opacidade ajustavel). */
    public static int bg() {
        float a = Math.max(0F, Math.min(1F, getFloat("opt.bg", 0.55F)));
        return ((int) (a * 255F) << 24) | 0x0E0E12;
    }

    public static float radius() {
        return Math.max(0F, Math.min(10F, getFloat("opt.radius", 4F)));
    }

    /** Escala final de um elemento do HUD (individual x geral). */
    public static float scale(String name) {
        float s = getFloat("scale." + name, 1F) * getFloat("scale.__all", 1F);
        return Math.max(0.3F, Math.min(3F, s));
    }
}
