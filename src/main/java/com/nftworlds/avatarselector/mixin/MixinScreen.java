package com.nftworlds.avatarselector.mixin;

import com.nftworlds.avatarselector.screen.AvatarScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.nftworlds.avatarselector.AvatarSelector.BACKGROUND_TEXTURE;

@Mixin(Screen.class)
public abstract class MixinScreen {

    @ModifyArg(method = "renderBackgroundTexture", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V"), index = 1)
    private Identifier render(Identifier identifier) {
        if(MinecraftClient.getInstance().currentScreen instanceof AvatarScreen)
            return BACKGROUND_TEXTURE;
        return identifier;
    }


}
