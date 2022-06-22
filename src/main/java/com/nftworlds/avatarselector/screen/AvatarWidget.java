package com.nftworlds.avatarselector.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;

public class AvatarWidget extends AlwaysSelectedEntryListWidget<AvatarEntry>
{
    AvatarScreen parent;

    public AvatarWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight, AvatarScreen parent)
    {
        super(client, width, height, top, bottom, itemHeight);
        this.parent = parent;
    }

    @Override
    protected int getMaxPosition()
    {
        return super.getMaxPosition() + 4;
    }

    @Override
    public int getRowWidth()
    {
        return width - (Math.max(0, getMaxPosition() - (bottom - top - 4)) > 0 ? 18 : 12);
    }

    @Override
    protected int getScrollbarPositionX()
    {
		return width - 6;
    }


}