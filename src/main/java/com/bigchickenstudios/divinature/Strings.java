package com.bigchickenstudios.divinature;

public final class Strings {

    public static final String MODID = "divinature";

    public static String createResourceLocationString(String path) {
        return String.format("%s:%s", MODID, path);
    }
    public static ResourceLocation createResourceLocation(String path) {
        return new ResourceLocation(MODID, path);
    }

    private Strings() {}
}
