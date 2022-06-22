package com.nftworlds.avatarselector;

import com.nftworlds.avatarselector.screen.AvatarWidget;
import com.nftworlds.avatarselector.utils.AvatarUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class AvatarSelector implements ModInitializer {
    public static final String ID = "avatarselector";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    public static File folder;

    public static AvatarWidget avatarWidget;

    public static Identifier id(String name) {
        return new Identifier(ID, name);
    }

    @Override
    public void onInitialize() {
        folder = new File("avatars");
        folder.mkdirs();

        // pre loads avatar folder
        AvatarManager.getInstance().loadAvatarFolder();
    }
}
