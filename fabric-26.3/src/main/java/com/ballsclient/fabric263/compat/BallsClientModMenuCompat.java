package com.ballsclient.fabric263.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.ballsclient.fabric263.gui.BallsClientConfigScreen;

public class BallsClientModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BallsClientConfigScreen::new;
    }
}
