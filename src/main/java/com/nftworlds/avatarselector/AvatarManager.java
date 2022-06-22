package com.nftworlds.avatarselector;

import com.nftworlds.avatarselector.screen.FakeAvatarEntry;
import com.nftworlds.avatarselector.utils.AvatarUtils;

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
        setAvatarEntryList(AvatarUtils.loadAvatarEntries(folder));
    }
}
