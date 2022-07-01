package com.nftworlds.avatarselector.mixin;

import com.nftworlds.avatarselector.screen.AvatarScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PlayerEntityRenderer.class, priority = 1100)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer {

    public MixinPlayerEntityRenderer(EntityRendererFactory.Context ctx, EntityModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Shadow protected abstract void setModelPose(AbstractClientPlayerEntity player);

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;setModelPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V"), method = "render")
    public void setModelPoseRedirect(PlayerEntityRenderer playerEntityRenderer, AbstractClientPlayerEntity player) {
        if((MinecraftClient.getInstance().currentScreen instanceof AvatarScreen)){

            PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = (PlayerEntityModel)getModel();

            playerEntityModel.setVisible(true);
            playerEntityModel.hat.visible = isPartVisible(PlayerModelPart.HAT);
            playerEntityModel.jacket.visible = isPartVisible(PlayerModelPart.JACKET);
            playerEntityModel.leftPants.visible = isPartVisible(PlayerModelPart.LEFT_PANTS_LEG);
            playerEntityModel.rightPants.visible = isPartVisible(PlayerModelPart.RIGHT_PANTS_LEG);
            playerEntityModel.leftSleeve.visible = isPartVisible(PlayerModelPart.LEFT_SLEEVE);
            playerEntityModel.rightSleeve.visible = isPartVisible(PlayerModelPart.RIGHT_SLEEVE);
            playerEntityModel.sneaking = false;

            //player.getInventory().main.set(0, new ItemStack(Items.BOW));

            playerEntityModel.rightArmPose = BipedEntityModel.ArmPose.EMPTY;
            playerEntityModel.leftArmPose = BipedEntityModel.ArmPose.EMPTY;

            // could run an animation loop starting here
        }

        else
            setModelPose(player);
    }

    private boolean isPartVisible(PlayerModelPart modelPart){
        if(((AvatarScreen)MinecraftClient.getInstance().currentScreen).parent instanceof SkinOptionsScreen)
            return(MinecraftClient.getInstance().options.isPlayerModelPartEnabled(modelPart));
        else
            return true;
    }
}
