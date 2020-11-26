package com.bigchickenstudios.divinature.network;

import com.bigchickenstudios.divinature.client.DivinatureClient;
import com.bigchickenstudios.divinature.research.Research;
import com.bigchickenstudios.divinature.research.ResearchProgress;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ResearchInfoMessage implements IMessage {

    private boolean firstSync;
    private Map<ResourceLocation, Research.Builder> toAdd;
    private Set<ResourceLocation> toRemove;
    private Map<ResourceLocation, ResearchProgress.Builder> toUpdate;

    public ResearchInfoMessage() {}

    public ResearchInfoMessage(boolean firstSyncIn, Collection<Research> toAddIn, Set<ResourceLocation> toRemoveIn, Map<ResourceLocation, ResearchProgress> toUpdateIn) {
        this.firstSync = firstSyncIn;
        this.toAdd = Maps.newHashMap();
        toAddIn.forEach((r) -> this.toAdd.put(r.getId(), r.copy()));
        this.toRemove = toRemoveIn;
        this.toUpdate = Maps.newHashMap();
        toUpdateIn.forEach((rl, rp) -> this.toUpdate.put(rl, rp.copy()));
    }

    public boolean isFirstSync() {
        return this.firstSync;
    }

    public Map<ResourceLocation, Research.Builder> getToAdd() {
        return this.toAdd;
    }

    public Set<ResourceLocation> getToRemove() {
        return this.toRemove;
    }

    public Map<ResourceLocation, ResearchProgress.Builder> getToUpdate() {
        return this.toUpdate;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeBoolean(this.firstSync);

        buffer.writeVarInt(this.toAdd.size());
        for (Map.Entry<ResourceLocation, Research.Builder> entry : this.toAdd.entrySet()) {
            buffer.writeResourceLocation(entry.getKey());
            entry.getValue().write(buffer);
        }

        buffer.writeVarInt(this.toRemove.size());
        for (ResourceLocation rl : this.toRemove) {
            buffer.writeResourceLocation(rl);
        }

        buffer.writeVarInt(this.toUpdate.size());
        for (Map.Entry<ResourceLocation, ResearchProgress.Builder> entry : this.toUpdate.entrySet()) {
            buffer.writeResourceLocation(entry.getKey());
            entry.getValue().write(buffer);
        }

    }

    @Override
    public void read(PacketBuffer buffer) {
        this.firstSync = buffer.readBoolean();

        this.toAdd = Maps.newHashMap();
        for (int i = buffer.readVarInt(); i > 0; i--) {
            this.toAdd.put(buffer.readResourceLocation(), Research.Builder.read(buffer));
        }

        this.toRemove = Sets.newLinkedHashSet();
        for (int i = buffer.readVarInt(); i > 0; i--) {
            this.toRemove.add(buffer.readResourceLocation());
        }

        this.toUpdate = Maps.newHashMap();
        for (int i = buffer.readVarInt(); i > 0; i--) {
            this.toUpdate.put(buffer.readResourceLocation(), ResearchProgress.Builder.read(buffer));
        }
    }

    @Override
    public void handle(NetworkEvent.Context ctx) {
        DivinatureClient.getResearchManager().read(this);
    }

    public static ResearchInfoMessage create(boolean firstSyncIn, Collection<Research> toAddIn, Set<ResourceLocation> toRemoveIn, Map<ResourceLocation, ResearchProgress> toUpdateIn) {
        return new ResearchInfoMessage(firstSyncIn, toAddIn, toRemoveIn, toUpdateIn);
    }

    public void send(ServerPlayerEntity entity) {
        this.send(PacketDistributor.PLAYER.with(() -> entity));
    }
}
