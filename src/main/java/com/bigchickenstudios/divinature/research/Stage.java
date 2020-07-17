package com.bigchickenstudios.divinature.research;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class Stage {

    private final List<Task<?, ?>> tasks;
    private ITextComponent info;

    public Stage(List<Task<?, ?>> tasksIn) {
        this.tasks = ImmutableList.copyOf(tasksIn);
    }

    public List<Task<?, ?>> getTasks() {
        return this.tasks;
    }

    public void addListeners(PlayerResearch playerResearchIn, Research researchIn, ResearchProgress researchProgressIn) {
        for (int i = 0; i < this.tasks.size(); i++) {
            if (!researchProgressIn.getTasks()[i]) {
                this.tasks.get(i).addListener(playerResearchIn, researchIn, researchProgressIn.getStage(), i);
            }
        }
    }

    private void setInfo(ITextComponent infoIn) {
        this.info = infoIn;
    }

    public ITextComponent getInfo() {
        return this.info;
    }

    public static Stage deserialize(JsonObject jsonObject) {
        JsonArray jsonArray = JSONUtils.getJsonArray(jsonObject, "tasks");
        List<Task<?, ?>> tasks = new ArrayList<>();
        for (JsonElement e : jsonArray) {
            tasks.add(Task.deserialize(JSONUtils.getJsonObject(e, "task")));
        }
        Stage stage = new Stage(tasks);
        if (jsonObject.has("info")) {
            stage.setInfo(ITextComponent.Serializer.func_240641_a_(jsonObject.get("info")));
        }
        else {
            throw new JsonSyntaxException("Missing info, expected to find a JsonElement");
        }
        return stage;
    }

    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(this.tasks.size());
        for (Task<?, ?> t : this.tasks) {
            t.write(buffer);
        }
        boolean flag = this.info != null;
        buffer.writeBoolean(flag);
        if (flag) {
            buffer.writeTextComponent(this.info);
        }
    }

    public static Stage read(PacketBuffer buffer) {
        List<Task<?, ?>> tasks = new ArrayList<>();
        int size = buffer.readVarInt();
        while(size > 0) {
            tasks.add(Task.read(buffer));
            size--;
        }
        Stage stage = new Stage(tasks);
        if (buffer.readBoolean()) {
            stage.setInfo(buffer.readTextComponent());
        }
        return stage;
    }

}
