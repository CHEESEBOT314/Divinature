package com.bigchickenstudios.divinature.client.multiplayer;

import com.bigchickenstudios.divinature.client.DivinatureClient;
import com.bigchickenstudios.divinature.client.gui.toasts.ResearchToast;
import com.bigchickenstudios.divinature.event.PlayerEvents;
import com.bigchickenstudios.divinature.network.ResearchInfoMessage;
import com.bigchickenstudios.divinature.research.Research;
import com.bigchickenstudios.divinature.research.ResearchList;
import com.bigchickenstudios.divinature.research.ResearchProgress;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@OnlyIn(Dist.CLIENT)
public final class ClientResearchManager {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Minecraft mc;
    private final ResearchList researchList = new ResearchList();
    private final Map<Research, ResearchProgress> progress = Maps.newHashMap();

    private IListener listener;
    private Research selectedPage;

    public ClientResearchManager(Minecraft mcIn) {
        this.mc = mcIn;
    }

    public void read(ResearchInfoMessage message) {
        if (message.isFirstSync()) {
            this.researchList.clear();
            this.progress.clear();
        }

        this.researchList.removeAll(message.getToRemove());
        this.researchList.loadResearch(message.getToAdd());

        for (Map.Entry<ResourceLocation, ResearchProgress.Builder> entry : message.getToUpdate().entrySet()) {
            Research research = this.researchList.getResearch(entry.getKey());
            if (research != null) {
                ResearchProgress progress = entry.getValue().build(research);
                if (!message.isFirstSync() && progress.isComplete()) {
                    this.mc.getToastGui().add(new ResearchToast(research));
                }
                this.progress.put(research, progress);
            }
            else {
                LOGGER.warn("Server informed client about unknown research: {}", entry.getKey());
            }
        }
    }

    public void setSelectedPage(Research page, boolean send) {
        if (page != null && send) {

        }
        if (page != this.selectedPage) {
            this.selectedPage = page;
            if (this.listener != null) {
                this.listener.setSelectedPage(this.selectedPage);
            }
        }
    }

    public void setListener(IListener listenerIn) {
        this.listener = listenerIn;
        this.researchList.setListener(this.listener);
        if (this.listener != null) {
            for (Map.Entry<Research, ResearchProgress> entry : this.progress.entrySet()) {
                this.listener.onUpdateProgress(entry.getKey(), entry.getValue());
            }
            this.listener.setSelectedPage(this.selectedPage);
        }
    }

    public ResearchList getResearchList() {
        return this.researchList;
    }

    public boolean isResearchComplete(ResourceLocation idIn) {
        return Optional.ofNullable(this.researchList.getResearch(idIn)).map(this.progress::get).map(ResearchProgress::isComplete).orElse(false);
    }

    public interface IListener extends ResearchList.IListener {
        void onUpdateProgress(Research research, ResearchProgress progress);
        void setSelectedPage(Research page);
    }

    public static BiFunction<PlayerEntity, ResourceLocation, Boolean> getResearchChecker() {
        return (p, r) -> DivinatureClient.getResearchManager().isResearchComplete(r);
    }
}
