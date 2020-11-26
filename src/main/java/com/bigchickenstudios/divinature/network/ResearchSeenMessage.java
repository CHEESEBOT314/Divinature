package com.bigchickenstudios.divinature.network;

import com.bigchickenstudios.divinature.research.PlayerResearch;
import com.bigchickenstudios.divinature.research.Research;
import com.bigchickenstudios.divinature.research.ResearchManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

public class ResearchSeenMessage implements IMessage {

    private Action action;
    private ResourceLocation tab;

    public ResearchSeenMessage() {}

    private ResearchSeenMessage(Action actionIn, @Nullable ResourceLocation tabIn) {
        this.action = actionIn;
        this.tab = tabIn;
    }

    public Action getAction() {
        return this.action;
    }

    public ResourceLocation getTab() {
        return this.tab;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeEnumValue(this.action);
        if (this.action == Action.OPEN_PAGE) {
            buffer.writeResourceLocation(this.tab);
        }
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.action = buffer.readEnumValue(Action.class);
        if (this.action == Action.OPEN_PAGE) {
            this.tab = buffer.readResourceLocation();
        }
    }

    @Override
    public void handle(NetworkEvent.Context ctx) {
        if (this.action == Action.OPEN_PAGE) {
            Research research = ResearchManager.getInstance().getResearch(this.tab);
            ServerPlayerEntity player = ctx.getSender();
            if (player != null) {
                PlayerResearch.getPlayer(player).setSelectedPage(research);
            }
        }
    }

    public static ResearchSeenMessage openPage(Research researchIn) {
        return new ResearchSeenMessage(Action.OPEN_PAGE, researchIn.getId());
    }

    public static ResearchSeenMessage close() {
        return new ResearchSeenMessage(Action.CLOSE_SCREEN, null);
    }

    public void send() {
        this.send(PacketDistributor.SERVER.noArg());
    }

    public enum Action {
        OPEN_PAGE,
        CLOSE_SCREEN
    }
}
