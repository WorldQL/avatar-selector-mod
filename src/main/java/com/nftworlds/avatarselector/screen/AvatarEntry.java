package com.nftworlds.avatarselector.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nftworlds.avatarselector.enums.AvatarAge;
import com.nftworlds.avatarselector.enums.AvatarType;
import com.nftworlds.avatarselector.utils.AvatarUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class AvatarEntry extends AlwaysSelectedEntryListWidget.Entry<AvatarEntry>
{
    protected final MinecraftClient client = MinecraftClient.getInstance();
    public String avatarName;
    public String ipfs; // ipfs hash is stored here.
    public File avatarPNGFile;
    public AvatarAge avatarAge;
    public AvatarType avatarType;
    protected final AvatarWidget widget;
    public Identifier rawAvatar;
    public Identifier processedAvatar; // will process differently based on AvatarAge
    private static int identify = 0;

    public AvatarEntry(String name, File file, AvatarWidget widget, NativeImage rawNativeImage, AvatarAge avatarAge, String ipfs) {
        identify++;
        avatarName = name;
        this.ipfs = ipfs;

        avatarPNGFile = file;
        this.widget = widget;
        this.avatarAge = avatarAge;

        rawAvatar = new Identifier("avatar_raw:"+ identify);
        processedAvatar = new Identifier("avatar_processed:"+ identify);

        avatarType = getAvatarType();

        NativeImageBackedTexture rawImageBackedTexture = new NativeImageBackedTexture(rawNativeImage);
        client.getTextureManager().registerTexture(rawAvatar, rawImageBackedTexture);

        NativeImage processedNativeImage;
        if (avatarAge.equals(AvatarAge.HD))
            processedNativeImage = rawImageBackedTexture.getImage();
        else
            processedNativeImage = AvatarUtils.remapTexture(rawNativeImage);
        NativeImageBackedTexture processedImageBackedTexture = new NativeImageBackedTexture(processedNativeImage);
        client.getTextureManager().registerTexture(processedAvatar, processedImageBackedTexture);
    }

    @Override
    public boolean mouseClicked(double v, double v1, int i) {
        widget.setSelected(this);
		return true;
    }

    public Text getNarration() {
        return new LiteralText(avatarName);
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
        TextRenderer font = client.textRenderer;
        renderIcon(x,y,matrices);
        int maxNameWidth = rowWidth - 32 - 3;
        if (font.getWidth(avatarName) > maxNameWidth)
            avatarName = font.trimToWidth(avatarName, maxNameWidth - font.getWidth("...")) + "...";
        font.draw(matrices, avatarName, x + 35, y + 5, 0xFFFFFF);

    }

    private void renderIcon(int k, int j, MatrixStack matrices) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, rawAvatar);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        /* Head icons  */
        if(avatarAge == AvatarAge.OLD)
            DrawableHelper.drawTexture(matrices, k, j, 32.0F, 32.0F, 32, 32, 256, 128);
        else {
            DrawableHelper.drawTexture(matrices, k, j, 32.0F, 32.0F, 32, 32, 256, 256);
            DrawableHelper.drawTexture(matrices, k, j, 160.0F, 32.0F, 32, 32, 256, 256);
        }
    }


    public void toggleAvatarType()
    {
        if (avatarType.equals(AvatarType.CLASSIC))
            avatarType = AvatarType.SLIM;
        else if (avatarType.equals(AvatarType.SLIM))
            avatarType = AvatarType.CLASSIC;
    }

    public AvatarType getAvatarType() {
        try {
            BufferedImage image = ImageIO.read(avatarPNGFile);
            int pixel = image.getRGB(50,19);

            if((pixel>>24) == 0x00)
                return AvatarType.SLIM;

            return AvatarType.CLASSIC;
        } catch(Exception e) {
            return AvatarType.CLASSIC;
        }
    }

    public void deleteAvatar() {
        avatarPNGFile.delete();
    }

}