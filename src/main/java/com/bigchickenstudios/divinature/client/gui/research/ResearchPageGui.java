package com.bigchickenstudios.divinature.client.gui.research;

import com.bigchickenstudios.divinature.research.Research;
import net.minecraft.client.gui.AbstractGui;

public class ResearchPageGui extends AbstractGui {

    private final Research research;

    public ResearchPageGui(Research researchIn) {
        this.research = researchIn;
    }

    public Research getResearch() {
        return this.research;
    }
}
