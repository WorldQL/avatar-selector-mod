package com.nftworlds.avatarselector.screen;

import com.nftworlds.avatarselector.enums.AvatarAge;
import net.minecraft.client.texture.NativeImage;

import java.io.File;

public class FakeAvatarEntry {

    private final String name;
    private final File file;
    private final NativeImage rawImage;
    private final AvatarAge age;
    private final String ipfs;


    public FakeAvatarEntry(String name, File file, NativeImage rawImage, AvatarAge age, String ipfs) {
        this.name = name;
        this.file = file;
        this.rawImage = rawImage;
        this.age = age;
        this.ipfs = ipfs;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public NativeImage getRawImage() {
        return rawImage;
    }

    public AvatarAge getAge() {
        return age;
    }

    public String getIpfs() {
        return ipfs;
    }
}
