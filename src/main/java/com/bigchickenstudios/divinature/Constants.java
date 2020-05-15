package com.bigchickenstudios.divinature;

import net.minecraft.util.ResourceLocation;

public final class Constants {

    public static final String MODID = "divinature";

    public static String rls(String path) {
        return String.format("%s:%s", MODID, path);
    }
    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MODID, path);
    }

    private Constants() {}
}
