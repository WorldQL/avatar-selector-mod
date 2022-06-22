package com.nftworlds.avatarselector.mixin;

import com.nftworlds.avatarselector.screen.AvatarScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public class MixinClientWorld {

    @Inject(at = @At("HEAD"), method = "getSpawnPos", cancellable = true)
    public void getSpawnPos(CallbackInfoReturnable<BlockPos> cir) {
        if(MinecraftClient.getInstance().currentScreen instanceof AvatarScreen)
            cir.setReturnValue(new BlockPos(0, 0, 0));
    }

    @Inject(at = @At("HEAD"), method = "getSpawnAngle", cancellable = true)
    public void getSpawnAngle(CallbackInfoReturnable<Float> cir) {
        if(MinecraftClient.getInstance().currentScreen instanceof AvatarScreen)
            cir.setReturnValue(0F);
    }
}
