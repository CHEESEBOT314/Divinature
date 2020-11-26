package com.bigchickenstudios.divinature.client.gui.research;

import com.bigchickenstudios.divinature.Strings;
import com.bigchickenstudios.divinature.client.multiplayer.ClientResearchManager;
import com.bigchickenstudios.divinature.network.ResearchSeenMessage;
import com.bigchickenstudios.divinature.research.Research;
import com.bigchickenstudios.divinature.research.ResearchProgress;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ResearchScreen extends Screen implements ClientResearchManager.IListener {

    private static final ResourceLocation WINDOW = Strings.createResourceLocation("textures/gui/research/window.png");

    private final ClientResearchManager clientResearchManager;
    private final Map<Research, ResearchPageGui> pages = Maps.newLinkedHashMap();
    private ResearchPageGui selectedPage;
    private boolean isScrolling = false;

    public ResearchScreen(ClientResearchManager clientResearchManagerIn) {
        super(NarratorChatListener.EMPTY);
        this.clientResearchManager = clientResearchManagerIn;
    }

    @Override
    protected void init() {
        this.pages.clear();
        this.selectedPage = null;
        this.clientResearchManager.setListener(this);
        if (this.selectedPage == null && !this.pages.isEmpty()) {
            this.clientResearchManager.setSelectedPage(this.pages.values().iterator().next().getResearch(), true);
        }
        else {
            this.clientResearchManager.setSelectedPage(this.selectedPage == null ? null : this.selectedPage.getResearch(), true);
        }
    }

    @Override
    public void onClose() {
        this.clientResearchManager.setListener(null);
        ResearchSeenMessage.close().send();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public void onUpdateProgress(Research research, ResearchProgress progress) {

    }

    @Override
    public void setSelectedPage(Research page) {
        this.selectedPage = this.pages.get(page);
    }

    @Override
    public void pageBaseAdded(Research r) {

    }

    @Override
    public void nonPageBaseAdded(Research r) {

    }

    @Override
    public void pageBaseRemoved(Research r) {

    }

    @Override
    public void nonPageBaseRemoved(Research r) {

    }

    @Override
    public void researchCleared() {
        this.pages.clear();
        this.selectedPage = null;
    }
}
