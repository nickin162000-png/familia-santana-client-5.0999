package com.familiasantana.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;

import java.io.IOException;

public class ModernPauseMenu extends GuiScreen {
    private final float[] hover = new float[5];

    private String label(int i) {
        switch (i) {
            case 0:
                return "Voltar ao jogo";
            case 1:
                return "Menu do Client";
            case 2:
                return "Editar HUD";
            case 3:
                return "Opcoes";
            default:
                return field_146297_k.func_71387_A() ? "Salvar e sair" : "Desconectar";
        }
    }

    private float bx() {
        return field_146294_l / 2F - 100;
    }

    private float by(int i) {
        return field_146295_m / 2F - 66 + i * 34;
    }

    @Override
    public void func_73863_a(int mx, int my, float partialTicks) {
        Gui.func_73734_a(0, 0, field_146294_l, field_146295_m, 0x99000000);

        String t = "Pausado";
        float tw = Fonts.width(t, 2F, true);
        Fonts.drawC(t, field_146294_l / 2F - tw / 2F, by(0) - 28, 0xFFFFFFFF, 2F, true, false);
        RenderUtil.roundedRect(field_146294_l / 2F - 14, by(0) - 14, field_146294_l / 2F + 14, by(0) - 12, 1F, Config.accent());

        for (int i = 0; i < 5; i++) {
            boolean hov = RenderUtil.hover(mx, my, bx(), by(i), bx() + 200, by(i) + 26);
            hover[i] = Ui.approach(hover[i], hov);
            Ui.button(label(i), bx(), by(i), 200, 26, hover[i], i == 0);
        }
    }

    @Override
    protected void func_73864_a(int mx, int my, int btn) throws IOException {
        super.func_73864_a(mx, my, btn);
        if (btn != 0) return;
        for (int i = 0; i < 5; i++) {
            if (!RenderUtil.hover(mx, my, bx(), by(i), bx() + 200, by(i) + 26)) continue;
            if (i == 0) {
                field_146297_k.func_147108_a(null);
                field_146297_k.func_71381_h();
            } else if (i == 1) {
                field_146297_k.func_147108_a(new ModernGui());
            } else if (i == 2) {
                field_146297_k.func_147108_a(new HudEditorGui(this));
            } else if (i == 3) {
                field_146297_k.func_147108_a(new GuiOptions(this, field_146297_k.field_71474_y));
            } else {
                boolean singleplayer = field_146297_k.func_71387_A();
                field_146297_k.field_71441_e.func_72882_A();
                field_146297_k.func_71403_a((WorldClient) null);
                if (singleplayer) field_146297_k.func_147108_a(new GuiMainMenu());
                else field_146297_k.func_147108_a(new GuiMultiplayer(new GuiMainMenu()));
            }
            return;
        }
    }

    @Override
    public boolean func_73868_f() {
        return true;
    }
}
