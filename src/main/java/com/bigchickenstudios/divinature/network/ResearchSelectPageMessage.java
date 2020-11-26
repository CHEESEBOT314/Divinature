package com.bigchickenstudios.divinature.network;

import com.bigchickenstudios.divinature.client.DivinatureClient;
import com.bigchickenstudios.divinature.research.Research;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

public class ResearchSelectPageMessage implements IMessage {

    private ResourceLocation page;

    public ResearchSelectPageMessage() {}

    public ResearchSelectPageMessage(@Nullable ResourceLocation pageIn) {
        this.page = pageIn;
    }

    public ResourceLocation getPage() {
        return this.page;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeBoolean(this.page != null);
        if (this.page != null) {
            buffer.writeResourceLocation(this.page);
        }
    }

    @Override
    public void read(PacketBuffer buffer) {
        if (buffer.readBoolean()) {
            this.page = buffer.readResourceLocation();
        }
    }

    @Override
    public void handle(NetworkEvent.Context ctx) {
        Research research = null;
        if (this.page != null) {
            research = DivinatureClient.getResearchManager().getResearchList().getResearch(this.page);
        }
        DivinatureClient.getResearchManager().setSelectedPage(research, false);
    }

    public static ResearchSelectPageMessage create(@Nullable Research researchIn) {
        return new ResearchSelectPageMessage(researchIn == null ? null : researchIn.getId());
    }

    public void send(ServerPlayerEntity player) {
        this.send(PacketDistributor.PLAYER.with(() -> player));
    }
}
