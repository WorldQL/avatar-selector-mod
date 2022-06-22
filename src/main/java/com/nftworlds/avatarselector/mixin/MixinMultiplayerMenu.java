package com.nftworlds.avatarselector.mixin;

import com.nftworlds.avatarselector.screen.AvatarScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MixinMultiplayerMenu extends Screen {
	public MixinMultiplayerMenu(Text title) {
		super(title);
	}

	@Inject(at = @At("RETURN"), method = "init")
	private void init(CallbackInfo info) {
		int buttonX =25; //var just in case we need to resize it.

		addDrawableChild(new ButtonWidget(buttonX, 6, 100, 20, Text.of("Change Skin"),
				button -> MinecraftClient.getInstance().setScreen(new AvatarScreen(this))));
		addSelectableChild(new ButtonWidget(buttonX, 6, 100, 20, Text.of("Change Skin"),
				button -> MinecraftClient.getInstance().setScreen(new AvatarScreen(this))));
	}

}