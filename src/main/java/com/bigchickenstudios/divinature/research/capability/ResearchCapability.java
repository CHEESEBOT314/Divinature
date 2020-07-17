package com.bigchickenstudios.divinature.research.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class ResearchCapability {

    @CapabilityInject(IResearch.class)
    public static final Capability<IResearch> CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IResearch.class, new Capability.IStorage<IResearch>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IResearch> capabilityIn, IResearch instanceIn, Direction sideIn) {
                throw new RuntimeException("IResearch capability does not use the IStorage!");
            }
            @Override
            public void readNBT(Capability<IResearch> capabilityIn, IResearch instanceIn, Direction sideIn, INBT nbtIn) {
                throw new RuntimeException("IResearch capability does not use the IStorage!");
            }
        }, () -> { throw new RuntimeException("There is no default implementation!"); });
    }
}
