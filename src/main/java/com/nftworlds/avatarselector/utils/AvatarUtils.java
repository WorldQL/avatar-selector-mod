package com.nftworlds.avatarselector.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.authlib.GameProfile;
import com.nftworlds.avatarselector.enums.AvatarAge;
import com.nftworlds.avatarselector.screen.FakeAvatarEntry;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.world.ClientWorld;
import org.apache.commons.io.FileUtils;
import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.nftworlds.avatarselector.AvatarSelector.avatarWidget;

public class AvatarUtils {

    public static OtherClientPlayerEntity fakePlayer;

    public static List<FakeAvatarEntry> loadAvatarEntries(File folder) {
        NativeImage nativeImage;
        AvatarAge age;
        List<FakeAvatarEntry> avatarEntries = new ArrayList<>();

        for (File fileEntry : folder.listFiles()) {

            String name = null;
            String ipfs = null;

            try {
                JsonObject jsonObject = new Gson().fromJson(new FileReader(fileEntry), JsonObject.class);
                name = jsonObject.get("name").getAsString();
                ipfs = jsonObject.get("texture").getAsString().substring(7);

                URL url = new URL("https://routing.nftworlds.com/ipfs/" + ipfs);
                File file = new File(name + ".png");
                FileUtils.copyURLToFile(url, file);

                nativeImage = AvatarUtils.toNativeImage(file);
            } catch (JsonParseException | IOException e) {
                // not a .json file, treat it as a .png (normal skin)
                nativeImage = AvatarUtils.toNativeImage(fileEntry);
            }

            if(nativeImage != null){
                if(nativeImage.getWidth() == 64 && nativeImage.getHeight() == 32)
                    age = AvatarAge.OLD;
                else if(nativeImage.getWidth() == 64 && nativeImage.getHeight() == 64)
                    age = AvatarAge.NEW;
                else
                    age = AvatarAge.HD;

                if (name == null)
                    name = fileEntry.getName().substring(0, fileEntry.getName().length()-4);

                avatarEntries.add(new FakeAvatarEntry(name, fileEntry, nativeImage, age, ipfs));
            }
        }
        return avatarEntries;
    }


    public static OtherClientPlayerEntity getDummyPlayer() throws Exception{
        if(fakePlayer == null)  {
            Field f =Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);

            ClientWorld clientWorld = (ClientWorld) unsafe.allocateInstance(ClientWorld.class);
            GameProfile profile = (GameProfile) unsafe.allocateInstance(GameProfile.class);
            fakePlayer = new OtherClientPlayerEntity(clientWorld, profile);
        }
        return fakePlayer;
    }


    public static void drawPlayer(int x, int y, int height, int mouseX, int mouseY) throws Exception{
            InventoryScreen.drawEntity(x, y, height, x-mouseX, (y-160)-mouseY, getDummyPlayer());
    }

    public static NativeImage toNativeImage(File file){
        try {
            InputStream inputStream = new FileInputStream(file);
            NativeImage nativeImage = NativeImage.read(inputStream);
            inputStream.close();
            return nativeImage;
        }
        catch(Exception e) {
            return null;
        }
    }

    /***
     * If it's not an HD skin then we need to remap it. This is from yarn mappings.
     * @param nativeImage - skin image
     * @return - remapped image
     */
    public static NativeImage remapTexture(NativeImage nativeImage) {
        int x = nativeImage.getWidth();
        int y = nativeImage.getHeight();
        if (x == 64 && (y == 32 || y == 64)) {
            boolean bl = y == 32;
            if (bl) {
                NativeImage nativeImage2 = new NativeImage(64, 64, true);
                nativeImage2.copyFrom(nativeImage);
                nativeImage.close();
                nativeImage = nativeImage2;
                nativeImage2.fillRect(0, 32, 64, 32, 0);
                nativeImage2.copyRect(4, 16, 16, 32, 4, 4, true, false);
                nativeImage2.copyRect(8, 16, 16, 32, 4, 4, true, false);
                nativeImage2.copyRect(0, 20, 24, 32, 4, 12, true, false);
                nativeImage2.copyRect(4, 20, 16, 32, 4, 12, true, false);
                nativeImage2.copyRect(8, 20, 8, 32, 4, 12, true, false);
                nativeImage2.copyRect(12, 20, 16, 32, 4, 12, true, false);
                nativeImage2.copyRect(44, 16, -8, 32, 4, 4, true, false);
                nativeImage2.copyRect(48, 16, -8, 32, 4, 4, true, false);
                nativeImage2.copyRect(40, 20, 0, 32, 4, 12, true, false);
                nativeImage2.copyRect(44, 20, -8, 32, 4, 12, true, false);
                nativeImage2.copyRect(48, 20, -16, 32, 4, 12, true, false);
                nativeImage2.copyRect(52, 20, -8, 32, 4, 12, true, false);
            }

            stripAlpha(nativeImage, 0, 0, 32, 16);
            if (bl)
                stripColor(nativeImage);

            stripAlpha(nativeImage, 0, 16, 64, 32);
            stripAlpha(nativeImage, 16, 48, 48, 64);
            return nativeImage;
        } else {
            nativeImage.close();
            return null;
        }
    }

    private static void stripColor(NativeImage image) {
        int l;
        int m;
        for(l = 32; l < 64; ++l) {
            for(m = 0; m < 32; ++m) {
                int k = image.getColor(l, m);
                if ((k >> 24 & 255) < 128)
                    return;
            }
        }

        for(l = 32; l < 64; ++l) {
            for(m = 0; m < 32; ++m)
                image.setColor(l, m, image.getColor(l, m) & 16777215);
        }

    }

    private static void stripAlpha(NativeImage image, int x1, int y1, int x2, int y2) {
        for(int i = x1; i < x2; ++i) {
            for(int j = y1; j < y2; ++j)
                image.setColor(i, j, image.getColor(i, j) | -16777216);
        }
    }

}
