package com.bigchickenstudios.divinature.research;

import com.bigchickenstudios.divinature.network.ResearchInfoMessage;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.LogicalSide;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiFunction;

public class PlayerResearch {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<Research, ResearchProgress> progressMap = Maps.newIdentityHashMap();
    private final Set<Research> visible = Sets.newLinkedHashSet();
    private final Set<Research> progressChanged = Sets.newLinkedHashSet();
    private final Set<Research> visibilityChanged = Sets.newLinkedHashSet();

    private final ServerPlayerEntity player;
    private final File progressFile;

    private boolean sentFirstPacket = false;

    public PlayerResearch(ServerPlayerEntity playerIn, File progressFileIn, ResearchManager researchManagerIn) {
        this.player = playerIn;
        this.progressFile = progressFileIn;

        this.load(researchManagerIn);
    }

    public void load(ResearchManager researchManagerIn) {
        if (this.progressFile.isFile()) {
            try (JsonReader jsonReader = new JsonReader(new StringReader(Files.toString(this.progressFile, StandardCharsets.UTF_8)))) {
                jsonReader.setLenient(false);
                JsonElement jsonElement = Streams.parse(jsonReader);
                if (!jsonElement.isJsonObject()) {
                    throw new JsonParseException("Research file is not an object!");
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    try {
                        Research research = researchManagerIn.getResearch(new ResourceLocation(entry.getKey()));
                        if (research != null) {
                            if (entry.getValue().isJsonObject()) {
                                this.progressMap.put(research, ResearchProgress.Builder.deserialize(entry.getValue().getAsJsonObject()).build(research));
                            } else {
                                LOGGER.warn("Unable to read research progress for {}", entry.getKey());
                            }
                        } else {
                            LOGGER.warn("Unknown research progress found for {}", entry.getKey());
                        }
                    }
                    catch (ResourceLocationException e) {
                        LOGGER.warn("{} is not a valid ResourceLocation", entry.getKey(), e);
                    }
                }
            }
            catch(IOException e) {
                LOGGER.error("Couldn't access player research in {}", this.progressFile, e);
            }
            catch(JsonParseException e) {
                LOGGER.error("Couldn't parse player research in {}", this.progressFile, e);
            }
        }
        Map<Research, Boolean> completeMap = Maps.newHashMap();
        researchManagerIn.getAllResearch().forEach((r) -> this.checkProgressInit(r, completeMap));
    }

    private boolean checkProgressInit(Research researchIn, Map<Research, Boolean> map) {
        if (map.containsKey(researchIn)) {
            return map.get(researchIn);
        }
        for (Research parent : researchIn.getParents()) {
            if (this.checkProgressInit(parent, map)) {
                map.put(researchIn, false);
                return false;
            }
        }
        this.init(researchIn);
        boolean b = this.progressMap.get(researchIn).isComplete();
        map.put(researchIn, b);
        return b;
    }

    public Optional<ResearchProgress> getProgress(Research researchIn) {
        return Optional.ofNullable(researchIn).map(this.progressMap::get);
    }

    public void reload(ResearchManager researchManagerIn) {
        this.save();
        this.progressMap.clear();
        this.visible.clear();
        this.progressChanged.clear();
        this.visibilityChanged.clear();
        this.load(researchManagerIn);
    }

    public void save() {
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<Research, ResearchProgress> entry : this.progressMap.entrySet()) {
            jsonObject.add(entry.getKey().getId().toString(), entry.getValue().copy().serialize());
        }

        if (this.progressFile.getParentFile() != null) {
            this.progressFile.getParentFile().mkdirs();
        }

        try {
            Files.write(jsonObject.toString(), this.progressFile, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            LOGGER.error("Couldn't save player research to {}", this.progressFile, e);
        }
    }

    public boolean sendDirty() {
        boolean sent = false;
        if (!this.sentFirstPacket || !this.progressChanged.isEmpty()) {
            Set<Research> toAdd = Sets.newLinkedHashSet();
            Set<ResourceLocation> toRemove = Sets.newLinkedHashSet();
            Map<ResourceLocation, ResearchProgress> toUpdate = Maps.newHashMap();

            for (Research r : this.visibilityChanged) {
                if (this.visible.contains(r)) {
                    toAdd.add(r);
                }
                else {
                    toRemove.add(r.getId());
                }
            }

            for (Research r : this.progressChanged) {
                if (this.progressMap.containsKey(r)) {
                    toUpdate.put(r.getId(), this.progressMap.get(r));
                }
            }

            if (!this.sentFirstPacket || !toRemove.isEmpty() || !toUpdate.isEmpty()) {
                (new ResearchInfoMessage(!this.sentFirstPacket, toAdd, toRemove, toUpdate)).send(this.player);
                this.progressChanged.clear();
                sent = true;
            }
        }
        this.sentFirstPacket = true;
        return sent;
    }

    public void grant(Research researchIn, int stageIn, int taskIn) {
        ResearchProgress progress = this.progressMap.get(researchIn);
        if (progress != null && progress.grant(researchIn, stageIn, taskIn)) {
            this.init(researchIn);
            if (progress.isComplete()) {
                for (Research child : researchIn.getChildren()) {
                    boolean flag = true;
                    for (Research parent : child.getParents()) {
                        ResearchProgress parentProgress = this.progressMap.get(parent);
                        if (!this.visible.contains(parent) || parentProgress == null || !parentProgress.isComplete()) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        this.init(child);
                    }
                }
            }
        }
    }
    private void init(Research researchIn) {
        ResearchProgress progress = this.progressMap.computeIfAbsent(researchIn, (r) -> new ResearchProgress(r, 0));
        if (!progress.isComplete()) {
            researchIn.addListeners(this, progress);
        }
        if (this.visible.add(researchIn)) {
            this.visibilityChanged.add(researchIn);
        }
        this.progressChanged.add(researchIn);
    }

    private static final FolderName FOLDER_NAME = new FolderName("divinature/research");

    private static final Map<UUID, PlayerResearch> PLAYER_RESEARCH_MAP = Maps.newHashMap();

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(PlayerResearch::tickPlayer);
        MinecraftForge.EVENT_BUS.addListener(PlayerResearch::loadPlayer);
        MinecraftForge.EVENT_BUS.addListener(PlayerResearch::savePlayer);
    }

    public static PlayerResearch getPlayer(ServerPlayerEntity entity) {
        return PLAYER_RESEARCH_MAP.computeIfAbsent(entity.getUniqueID(), (uuid) -> {
            File file = new File(entity.getServer().func_240776_a_(FOLDER_NAME).toFile(), uuid + ".json");
            return new PlayerResearch(entity, file, ResearchManager.getInstance());
        });
    }

    private static void tickPlayer(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER) {
            if (PLAYER_RESEARCH_MAP.get(event.player.getUniqueID()).sendDirty()) {
                LOGGER.info("Sent research to player {}", event.player.getDisplayNameAndUUID().getString());
            }
        }
    }

    private static void loadPlayer(PlayerEvent.LoadFromFile event) {
        getPlayer((ServerPlayerEntity)event.getPlayer());
        LOGGER.info("Loaded research for player {}", event.getPlayer().getDisplayNameAndUUID().getString());
    }

    private static void savePlayer(PlayerEvent.SaveToFile event) {
        PlayerResearch playerResearch = PLAYER_RESEARCH_MAP.get(event.getPlayer().getUniqueID());
        if (playerResearch != null) {
            playerResearch.save();
        }
        LOGGER.info("Saved research for player {}", event.getPlayer().getDisplayNameAndUUID().getString());
    }

    public static void reloadAll(ResearchManager researchManagerIn) {
        for (PlayerResearch playerResearch : PLAYER_RESEARCH_MAP.values()) {
            playerResearch.reload(researchManagerIn);
        }
    }

    public static BiFunction<PlayerEntity, ResourceLocation, Boolean> getResearchChecker() {
        return (p, r) -> getPlayer((ServerPlayerEntity)p).getProgress(ResearchManager.getInstance().getResearch(r)).map(ResearchProgress::isComplete).orElse(false);
    }
}
