package com.bigchickenstudios.divinature.research;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ResearchList {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<ResourceLocation, Research> research = Maps.newHashMap();
    private final Set<Research> pageBases = Sets.newLinkedHashSet();

    private IListener listener;

    public void loadResearch(Map<ResourceLocation, Research.Builder> map) {
        Function<ResourceLocation, Research> func = Functions.forMap(this.research, null);

        while (!map.isEmpty()) {
            boolean flag = false;
            Iterator<Map.Entry<ResourceLocation, Research.Builder>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<ResourceLocation, Research.Builder> next = iterator.next();
                Research.Builder builder = next.getValue();
                if (builder.resolveParentsAndPage(func)) {
                    ResourceLocation rl = next.getKey();
                    Research r = builder.build(rl);
                    this.research.put(rl, r);
                    flag = true;
                    iterator.remove();

                    if (r.isPageBase()) {
                        this.pageBases.add(r);
                        if (this.listener != null) {
                            this.listener.pageBaseAdded(r);
                        }
                    }
                    else {
                        if (this.listener != null) {
                            this.listener.nonPageBaseAdded(r);
                        }
                    }
                }
            }
            if (!flag) {
                for (Map.Entry<ResourceLocation, Research.Builder> entry : map.entrySet()) {
                    LOGGER.error("Couldn't load research {}: {}", entry.getKey(), entry.getValue());
                }
                break;
            }
        }
        LOGGER.info("Loaded {} research", this.research.size());
    }

    public Research getResearch(ResourceLocation id) {
        return this.research.get(id);
    }

    public Collection<Research> getAll() {
        return this.research.values();
    }

    public Iterable<Research> getPageBases() {
        return this.pageBases;
    }

    @OnlyIn(Dist.CLIENT)
    public void setListener(IListener listenerIn) {
        this.listener = listenerIn;
        if (this.listener != null) {
            for (Research r : this.research.values()) {
                if (r.isPageBase()) {
                    this.listener.pageBaseAdded(r);
                }
                else {
                    this.listener.nonPageBaseAdded(r);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void remove(Research researchIn) {
        for (Research child : researchIn.getChildren()) {
            this.remove(child);
        }
        this.research.remove(researchIn.getId());
        LOGGER.info("Forgot about research {}", researchIn.getId());
        if (this.listener != null) {
            if (researchIn.isPageBase()) {
                this.listener.pageBaseRemoved(researchIn);
            }
            else {
                this.listener.nonPageBaseRemoved(researchIn);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void removeAll(Set<ResourceLocation> ids) {
        for (ResourceLocation id : ids) {
            Research r = this.research.get(id);
            if (r == null) {
                LOGGER.warn("Told to remove unknown research {}", id);
            }
            else {
                this.remove(r);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void clear() {
        this.research.clear();
        if (this.listener != null) {
            this.listener.researchCleared();
        }
    }

    public interface IListener {
        void pageBaseAdded(Research r);
        void nonPageBaseAdded(Research r);
        @OnlyIn(Dist.CLIENT)
        void pageBaseRemoved(Research r);
        @OnlyIn(Dist.CLIENT)
        void nonPageBaseRemoved(Research r);
        @OnlyIn(Dist.CLIENT)
        void researchCleared();
    }
}
