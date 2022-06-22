package com.nftworlds.avatarselector;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nftworlds.avatarselector.enums.AvatarAge;
import com.nftworlds.avatarselector.screen.FakeAvatarEntry;
import com.nftworlds.avatarselector.utils.AvatarUtils;
import net.minecraft.client.texture.NativeImage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.nftworlds.avatarselector.AvatarSelector.folder;

public final class AvatarManager {

    private static final AvatarManager instance = new AvatarManager();

    public static AvatarManager getInstance() {
        return instance;
    }

    private List<FakeAvatarEntry> avatarEntryList;

    private AvatarManager()  {
        avatarEntryList = new ArrayList<>();
    }

    public List<FakeAvatarEntry> getAvatarEntryList() {
        return avatarEntryList;
    }

    public void setAvatarEntryList(List<FakeAvatarEntry> avatarEntryList) {
        this.avatarEntryList = avatarEntryList;
    }

    public void loadAvatarFolder() {
        avatarEntryList.clear();
        setAvatarEntryList(loadAvatarEntries());
    }

    private List<FakeAvatarEntry> loadAvatarEntries() {
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
}
