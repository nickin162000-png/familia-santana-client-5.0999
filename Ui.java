package com.familiasantana.client;

public class Ui {
    public static void button(String label, float x, float y, float w, float h, float hoverAnim, boolean primary) {
        int accent = Config.accent();
        int bg = primary
                ? RenderUtil.lerpColor(RenderUtil.withAlpha(accent, 0xB4), accent, hoverAnim)
                : RenderUtil.lerpColor(0xFF1E1E26, 0xFF2D2D39, hoverAnim);
        RenderUtil.roundedRect(x, y, x + w, y + h, 7, bg);
        // em acento claro (branco) o texto precisa ser escuro
        int tc = (primary && isLight(accent)) ? 0xFF111114 : 0xFFFFFFFF;
        float tw = Fonts.width(label, 1F, true);
        Fonts.drawC(label, x + (w - tw) / 2F, y + h / 2F, tc, 1F, true, false);
    }

    public static boolean isLight(int argb) {
        int r = (argb >> 16) & 255;
        int g = (argb >> 8) & 255;
        int b = argb & 255;
        return (r * 299 + g * 587 + b * 114) / 1000 > 190;
    }

    public static float approach(float cur, boolean on) {
        return cur + ((on ? 1F : 0F) - cur) * 0.25F;
    }
}
