package com.bigchickenstudios.divinature.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public interface IMessage {

    void write(PacketBuffer buffer);
    void read(PacketBuffer buffer);
    void handle(NetworkEvent.Context ctx);

    default void send(PacketDistributor.PacketTarget target) {
        DivinaturePacketHandler.send(this, target);
    }
}
