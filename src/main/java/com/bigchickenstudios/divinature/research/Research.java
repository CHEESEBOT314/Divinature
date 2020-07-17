package com.bigchickenstudios.divinature.research;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Research {

    private final ResourceLocation id;
    private final List<Research> parents;
    private final Set<Research> children = new LinkedHashSet<>();
    private final List<Stage> stages;

    private final ITextComponent info;
    private final ItemStack icon;

    private final boolean isPageBase;
    private final ResourceLocation pageId;
    private final PageInfo pageInfo;


    private Research(ResourceLocation idIn, List<Research> parentsIn, List<Stage> stagesIn, ITextComponent infoIn, ItemStack iconIn, ResourceLocation pageIdIn) {
        this.id = idIn;
        this.parents = ImmutableList.copyOf(parentsIn);
        this.stages = ImmutableList.copyOf(stagesIn);

        this.info = infoIn;
        this.icon = iconIn;

        for (Research parent : this.parents) {
            parent.addChild(this);
        }
        
        this.isPageBase = false;
        this.pageId = pageIdIn;
        this.pageInfo = null;
    }
        
        
    private Research(ResourceLocation idIn, List<Research> parentsIn, List<Stage> stagesIn, ITextComponent infoIn, ItemStack iconIn, PageInfo pageInfoIn) {
        this.id = idIn;
        this.parents = ImmutableList.copyOf(parentsIn);
        this.stages = ImmutableList.copyOf(stagesIn);

        this.info = infoIn;
        this.icon = iconIn;

        for (Research parent : this.parents) {
            parent.addChild(this);
        }
        
        this.isPageBase = true;
        this.pageId = null;
        this.pageInfo = pageInfoIn;
    }

    public void addListeners(PlayerResearch playerResearchIn, ResearchProgress researchProgressIn) {
        this.stages.get(researchProgressIn.getStage()).addListeners(playerResearchIn, this, researchProgressIn);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public List<Research> getParents() {
        return this.parents;
    }

    public void addChild(Research child) {
        this.children.add(child);
    }

    public Iterable<Research> getChildren() {
        return this.children;
    }

    public List<Stage> getStages() {
        return this.stages;
    }

    public ITextComponent getInfo() {
        return this.info;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    public boolean isPageBase() {
        return this.isPageBase;
    }

    public ResourceLocation getPageId() {
        return this.isPageBase ? this.id : this.pageId;
    }

    public PageInfo getPageInfo() {
        return this.pageInfo;
    }

    public Builder copy() {
        Builder builder = Builder.create();
        this.parents.forEach((r) -> builder.withParent(r.getId()));
        this.stages.forEach(builder::withStage);
        builder.withInfo(this.info).withIcon(this.icon);
        return this.isPageBase ? builder.withPage(this.pageInfo) : builder.withPage(this.pageId);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public static final class Builder {

        private final List<ResourceLocation> parentIds;
        private List<Research> parents = new ArrayList<>();
        private final List<Stage> stages;

        private ITextComponent info;
        private ItemStack icon;

        private boolean isPageBase = false;
        private ResourceLocation pageId = null;
        private PageInfo pageInfo = null;

        private Builder() {
            this.parentIds = new ArrayList<>();
            this.stages = new ArrayList<>();
        }

        private Builder(List<ResourceLocation> parentIdsIn, List<Stage> stagesIn, ITextComponent infoIn, ItemStack iconIn, ResourceLocation pageIdIn) {
            this.parentIds = parentIdsIn;
            this.stages = stagesIn;
            this.info = infoIn;
            this.icon = iconIn;
            this.pageId = pageIdIn;
        }

        private Builder(List<ResourceLocation> parentIdsIn, List<Stage> stagesIn, ITextComponent infoIn, ItemStack iconIn, PageInfo pageInfoIn) {
            this.parentIds = parentIdsIn;
            this.stages = stagesIn;
            this.info = infoIn;
            this.icon = iconIn;
            this.isPageBase = true;
            this.pageInfo = pageInfoIn;
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withParent(ResourceLocation idIn) {
            this.parentIds.add(idIn);
            return this;
        }

        public Builder withStage(Stage stageIn) {
            this.stages.add(stageIn);
            return this;
        }
        
        public Builder withInfo(ITextComponent infoIn) {
            this.info = infoIn;
            return this;
        }
        
        public Builder withIcon(ItemStack iconIn) {
            this.icon = iconIn;
            return this;
        }

        public Builder withPage(ResourceLocation pageIdIn) {
            this.isPageBase = false;
            this.pageId = pageIdIn;
            this.pageInfo = null;
            return this;
        }

        public Builder withPage(PageInfo pageInfoIn) {
            this.isPageBase = true;
            this.pageId = null;
            this.pageInfo = pageInfoIn;
            return this;
        }

        public boolean resolveParentsAndPage(Function<ResourceLocation, Research> func) {
            if (!this.parentIds.isEmpty() && (this.parents == null || this.parents.size() != this.parentIds.size())) {
                List<Research> parentsTemp = new ArrayList<>();
                for (ResourceLocation parentId : this.parentIds) {
                    Research parent = func.apply(parentId);
                    if (parent == null) {
                        return false;
                    }
                    parentsTemp.add(parent);
                }
                this.parents = parentsTemp;
            }
            if (this.parentIds.isEmpty() || this.isPageBase) {
                return this.isPageBase;
            }
            for (Research parent : this.parents) {
                if (parent.getPageId() == this.pageId) {
                    return true;
                }
            }
            return false;
        }

        public Research build(ResourceLocation id) {
            return this.isPageBase ? new Research(id, this.parents, this.stages, this.info, this.icon, this.pageInfo) : new Research(id, this.parents, this.stages, this.info, this.icon, this.pageId);
        }

        public static Builder deserialize(JsonObject json) {
            Builder builder = create();
            JsonArray jsonArray = JSONUtils.getJsonArray(json, "parents");
            for (JsonElement e : jsonArray) {
                builder.withParent(new ResourceLocation(JSONUtils.getString(e, "parent")));
            }
            jsonArray = JSONUtils.getJsonArray(json, "stages");
            for (JsonElement e : jsonArray) {
                builder.withStage(Stage.deserialize(JSONUtils.getJsonObject(e, "stage")));
            }
            
            if (json.has("info")) {
                builder.withInfo(ITextComponent.Serializer.func_240641_a_(json.get("info")));
            }
            else {
                throw new JsonSyntaxException("Missing info, expect to find a JsonElement");
            }
            builder.withIcon(new ItemStack(JSONUtils.getItem(json, "icon")));

            JsonElement jsonElement = json.get("page");
            if (jsonElement == null) {
                throw new JsonSyntaxException("Missing page, expected to find a JsonObject or String");
            }
            if (JSONUtils.isString(jsonElement)) {
                builder.withPage(new ResourceLocation(jsonElement.getAsString()));
            }
            else if (jsonElement.isJsonObject()) {
                builder.withPage(PageInfo.deserialize(jsonElement.getAsJsonObject()));
            }
            else {
                throw new JsonSyntaxException("Expected page to be a JsonObject or String, was" + JSONUtils.toString(jsonElement));
            }
            return builder;
        }

        public void write(PacketBuffer buffer) {
            buffer.writeVarInt(this.parentIds.size());
            for (ResourceLocation rl : this.parentIds) {
                buffer.writeResourceLocation(rl);
            }
            buffer.writeVarInt(this.stages.size());
            for (Stage s : this.stages) {
                s.write(buffer);
            }
            buffer.writeTextComponent(this.info);
            buffer.writeItemStack(this.icon);
            
            buffer.writeBoolean(this.isPageBase);
            if (this.isPageBase) {
                this.pageInfo.write(buffer);
            }
            else {
                buffer.writeResourceLocation(this.pageId);
            }
        }

        public static Builder read(PacketBuffer buffer) {
            List<ResourceLocation> parentIds = new ArrayList<>();
            List<Stage> stages = new ArrayList<>();
            int size = buffer.readVarInt();
            while (size > 0) {
                parentIds.add(buffer.readResourceLocation());
                size--;
            }
            size = buffer.readVarInt();
            while (size > 0) {
                stages.add(Stage.read(buffer));
                size--;
            }
            ITextComponent info = buffer.readTextComponent();
            ItemStack icon = buffer.readItemStack();
            
            if (buffer.readBoolean()) {
                return new Research.Builder(parentIds, stages, info, icon, PageInfo.read(buffer));
            }
            else {
                return new Research.Builder(parentIds, stages, info, icon, buffer.readResourceLocation());
            }
        }
    }

    public static final class PageInfo {

        private final ITextComponent info;
        private final ItemStack icon;

        private PageInfo(ITextComponent infoIn, ItemStack iconIn) {
            this.info = infoIn;
            this.icon = iconIn;
        }

        public ITextComponent getInfo() {
            return this.info;
        }

        public ItemStack getIcon() {
            return this.icon;
        }

        public static PageInfo deserialize(JsonObject jsonObject) {
            ITextComponent info;
            if (jsonObject.has("info")) {
                info = ITextComponent.Serializer.func_240641_a_(jsonObject.get("info"));
            }
            else {
                throw new JsonSyntaxException("Missing info, expected to find a JsonElement");
            }
            ItemStack icon = new ItemStack(JSONUtils.getItem(jsonObject, "icon"));
            return new PageInfo(info, icon);
        }
        
        public void write(PacketBuffer buffer) {
            buffer.writeTextComponent(this.info);
            buffer.writeItemStack(this.icon);
        }
        
        public static PageInfo read(PacketBuffer buffer) {
            return new PageInfo(buffer.readTextComponent(), buffer.readItemStack());
        }
    }
}
