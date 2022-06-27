package com.nftworlds.avatarselector.screen;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nftworlds.avatarselector.AvatarManager;
import com.nftworlds.avatarselector.enums.AvatarType;
import com.nftworlds.avatarselector.utils.AvatarUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import static com.nftworlds.avatarselector.AvatarSelector.BACKGROUND_TEXTURE;
import static com.nftworlds.avatarselector.AvatarSelector.avatarWidget;

public class AvatarScreen extends Screen {
    public final Screen parent;

    public AvatarScreen(Screen screen) {
        super(Text.of("Change Skin"));
        parent = screen;
    }

    @Override
    protected void init() {
        int topY = 52;
        if (avatarWidget == null) {
            avatarWidget = new AvatarWidget(client, width / 2, height, topY, height - 28, 36, this);
            avatarWidget.setLeftPos(0);
        }
        addSelectableChild(avatarWidget);

        if (avatarWidget.children().isEmpty())
            addAvatar();

        //back button
        addDrawableChild(new ButtonWidget(width/4 - 100 - 2, height - 24, 80, 20, new TranslatableText("gui.back"),
                button -> MinecraftClient.getInstance().setScreen(parent)));

        //change skin button
        addDrawableChild(new ButtonWidget(width/5 + 3, height - 24, 80, 20, Text.of("Change Skin"),
                button -> client.setScreen(new ConfirmScreen(this::changeSkin, Text.of("Are you sure?"), new LiteralText( "Change skin to '" + getSelected().avatarName + "'"), new TranslatableText("gui.yes"), new TranslatableText("gui.cancel")))) {
            @Override
            public void render(MatrixStack matrices, int var1, int var2, float var3) {
                visible = true;
                active = (getSelected() != null);
                super.render(matrices, var1, var2, var3);
            }
        });

        //open avatar folder
//        addDrawableChild(new ButtonWidget((width - (width/5)) + 2, height - 24, 80, 20, Text.of("Open Folder"), button -> Util.getOperatingSystem().open(new File("avatars"))));

        //classic select button
        addDrawableChild(new ButtonWidget(width/5 + 3, 28, 80, 20, Text.of("Classic"), button -> getSelected().toggleAvatarType()) {
            @Override
            public void render(MatrixStack matrices, int var1, int var2, float var3) {
                visible = true;
                active = (getSelected() != null && getSelected().avatarType.equals(AvatarType.SLIM));
                super.render(matrices, var1, var2, var3);
            }
        });

        //slim select button
        addDrawableChild(new ButtonWidget(width/4 - 100 - 2, 28, 80, 20, Text.of("Slim"), button -> getSelected().toggleAvatarType()) {
            @Override
            public void render(MatrixStack matrices, int var1, int var2, float var3) {
                visible = true;
                active = (getSelected() != null && getSelected().avatarType.equals(AvatarType.CLASSIC));
                super.render(matrices, var1, var2, var3);
            }
        });

        int topRowY = 4;

        //refresh button
        addDrawableChild(new ButtonWidget(width/4 - 100 - 2, topRowY, 80, 20, new TranslatableText("selectServer.refresh"), button -> {
            AvatarManager.getInstance().loadAvatarFolder();
            addAvatar();
            avatarWidget.setSelected(null);
        }));

    }

    private void changeSkin(boolean confirmed) {
        if (confirmed) {
            try {
                // they confirmed the skin change. Skin -> getSelected() to get the ipfs do getSelected().ipfs
                JsonObject jsonObject = new JsonObject();
                JsonObject innerObj = new JsonObject();

                innerObj.addProperty("avataripfshash", getSelected().ipfs);
                jsonObject.add("data", innerObj);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://players-api.nftworlds.com/cosmetics"))
                        .header("Authorization", System.getProperty("NFTWAuthToken"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                        .build();

                String response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            MinecraftClient.getInstance().setScreen(parent); // when done do this

        }
        else { //they cancelled the skin change
            client.setScreen(this);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        renderBackgroundTexture(1);
        avatarWidget.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);

        // draws skin preview
        if (getSelected() != null) {
            try {
                AvatarUtils.drawPlayer(width - (width / 4), height - 36, 92, mouseX, mouseY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addAvatar() {

        avatarWidget.children().clear();

        for (FakeAvatarEntry entry : AvatarManager.getInstance().getAvatarEntryList())
            avatarWidget.children().add(new AvatarEntry(
                    entry.name(),
                    entry.file(),
                    avatarWidget,
                    entry.rawImage(),
                    entry.age(),
                    entry.ipfs()));
    }

    public static AvatarEntry getSelected(){
        return avatarWidget.getSelectedOrNull();
    }

}