package com.ballsclient.fabric263.gui.clickgui.component;

import com.ballsclient.fabric263.gui.clickgui.ClickGuiCanvas;
import net.minecraft.client.gui.Font;

/**
 * Base abstract class for expandable setting components inside ModuleCard.
 */
public abstract class SettingComponent {
    public abstract int getHeight();

    public abstract void render(ClickGuiCanvas context, Font font, int x, int y, int width, int mouseX, int mouseY);

    public abstract boolean mouseClicked(double mouseX, double mouseY, int button, int x, int y, int width);

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }
}
