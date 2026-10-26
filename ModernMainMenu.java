package com.familiasantana.client;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;

import java.io.IOException;
import java.util.Random;

public class ModernMainMenu extends GuiScreen {
    private static final String[] LABELS = {"Um jogador", "Multijogador", "Opcoes", "Sair do jogo"};
    private final float[] hover = new float[LABELS.length];
    private final Random rnd = new Random();
    private float[][] parts;

    @Override
    public void func_73866_w_() {
        if (parts == null) {
            parts = new float[50][4];
            for (float[] p : parts) reset(p, true);
        }
    }

    private void reset(float[] p, boolean anywhere) {
        p[0] = rnd.nextFloat() * Math.max(1, field_146294_l);
        p[1] = anywhere ? rnd.nextFloat() * Math.max(1, field_146295_m) : field_146295_m + 4;
        p[2] = 0.1F + rnd.nextFloat() * 0.45F;
        p[3] = 0.8F + rnd.nextFloat() * 1.8F;
    }

    private float bw() {
        return 200;
    }

    private float bx() {
        return field_146294_l / 2F - bw() / 2F;
    }

    private float by(int i) {
        return field_146295_m / 2F - 20 + i * 34;
    }

    @Override
    public void func_73863_a(int mx, int my, float partialTicks) {
        int accent = Config.accent();
        func_73733_a(0, 0, field_146294_l, field_146295_m, 0xFF0B0B10, 0xFF181426);
        RenderUtil.circle(field_146294_l * 0.15F, field_146295_m * 0.85F, 190, RenderUtil.withAlpha(accent, 0x16));
        RenderUtil.circle(field_146294_l * 0.88F, field_146295_m * 0.15F, 150, RenderUtil.withAlpha(accent, 0x10));

        for (float[] p : parts) {
            p[1] -= p[2];
            if (p[1] < -4) reset(p, false);
            RenderUtil.circle(p[0], p[1], p[3], 0x30FFFFFF);
        }

        String a = "Familia ";
        String b = "Santana";
        float aw = Fonts.width(a, 3F, true);
        float bwid = Fonts.width(b, 3F, true);
        float tx = field_146294_l / 2F - (aw + bwid) / 2F;
        float ty = by(0) - 62;
        Fonts.drawC(a, tx, ty, 0xFFFFFFFF, 3F, true, false);
        Fonts.drawC(b, tx + aw, ty, accent, 3F, true, false);

        String sub = "CLIENT 5.1   |   Minecraft 1.8.9";
        float sw = Fonts.width(sub, 0.9F, false);
        Fonts.drawC(sub, field_146294_l / 2F - sw / 2F, ty + 26, 0xFF8A8A98, 0.9F, false, false);

        for (int i = 0; i < LABELS.length; i++) {
            boolean hov = RenderUtil.hover(mx, my, bx(), by(i), bx() + bw(), by(i) + 26);
            hover[i] = Ui.approach(hover[i], hov);
            Ui.button(LABELS[i], bx(), by(i), bw(), 26, hover[i], i == 0);
        }
    }

    @Override
    protected void func_73864_a(int mx, int my, int btn) throws IOException {
        super.func_73864_a(mx, my, btn);
        if (btn != 0) return;
        for (int i = 0; i < LABELS.length; i++) {
            if (!RenderUtil.hover(mx, my, bx(), by(i), bx() + bw(), by(i) + 26)) continue;
            if (i == 0) field_146297_k.func_147108_a(new GuiSelectWorld(this));
            else if (i == 1) field_146297_k.func_147108_a(new GuiMultiplayer(this));
            else if (i == 2) field_146297_k.func_147108_a(new GuiOptions(this, field_146297_k.field_71474_y));
            else field_146297_k.func_71400_g();
            return;
        }
    }

    @Override
    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        // sem ESC na tela inicial
    }

    @Override
    public boolean func_73868_f() {
        return false;
    }
}
