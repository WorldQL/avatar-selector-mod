package com.nftworlds.avatarselector.screen;

import com.nftworlds.avatarselector.enums.AvatarAge;
import net.minecraft.client.texture.NativeImage;

import java.io.File;

public record FakeAvatarEntry(String name, File file, NativeImage rawImage, AvatarAge age, String ipfs) {

}
