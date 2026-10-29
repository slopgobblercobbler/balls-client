package com.ballsclient.fabric263.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.ballsclient.fabric263.gui.clickgui.ModernClickGuiScreen;

public class BallsClientModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModernClickGuiScreen::new;
    }
}
