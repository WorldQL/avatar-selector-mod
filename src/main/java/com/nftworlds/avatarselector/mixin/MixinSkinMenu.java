package com.nftworlds.avatarselector.mixin;

import com.nftworlds.avatarselector.screen.AvatarScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkinOptionsScreen.class)
public class MixinSkinMenu extends GameOptionsScreen {

    public MixinSkinMenu(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo info) {
        int buttonX = 25;

        addDrawableChild(new ButtonWidget(buttonX, 6, 100, 20, Text.of("Change Skin"),
                button -> MinecraftClient.getInstance().setScreen(new AvatarScreen(this))));
        addSelectableChild(new ButtonWidget(buttonX, 6, 100, 20, Text.of("Change Skin"),
                button -> MinecraftClient.getInstance().setScreen(new AvatarScreen(this))));
    }

}