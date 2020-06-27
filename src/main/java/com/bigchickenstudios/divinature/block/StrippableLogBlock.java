package com.bigchickenstudios.divinature.block;

import net.minecraft.block.Block;
import net.minecraft.block.LogBlock;
import net.minecraft.block.material.MaterialColor;

import java.util.function.Supplier;

public class StrippableLogBlock extends LogBlock implements IStrippable {

    private final Supplier<? extends Block> strippedVersion;

    public StrippableLogBlock(MaterialColor verticalColor, Properties properties, Supplier<? extends Block> strippedVersionIn) {
        super(verticalColor, properties);
        this.strippedVersion = strippedVersionIn;
    }

    @Override
    public Block getStrippedVersion() {
        return this.strippedVersion.get();
    }
}
