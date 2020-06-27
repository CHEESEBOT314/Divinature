package com.bigchickenstudios.divinature.block;

import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;

import java.util.function.Supplier;

public class StrippableRotatedPillarBlock extends RotatedPillarBlock implements IStrippable {

    private final Supplier<? extends Block> strippedVersion;

    public StrippableRotatedPillarBlock(Properties properties, Supplier<? extends Block> strippedVersionIn) {
        super(properties);
        this.strippedVersion = strippedVersionIn;
    }

    @Override
    public Block getStrippedVersion() {
        return this.strippedVersion.get();
    }
}
