package com.bigchickenstudios.divinature.research;

import net.minecraftforge.registries.ForgeRegistryEntry;

public class Research extends ForgeRegistryEntry<Research> {

    private Research(Builder builder) {

    }

    public static class Builder {

        private Builder() {}

        public static Builder create() {
            return new Builder();
        }





        public Research build() {
            return new Research(this);
        }
    }
}
