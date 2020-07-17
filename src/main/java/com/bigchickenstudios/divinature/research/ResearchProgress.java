package com.bigchickenstudios.divinature.research;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

import java.util.List;

public class ResearchProgress {

    private int stage;
    private boolean[] tasks;
    private boolean complete;

    public ResearchProgress(Research researchIn, int stageIn) {
        this.stage = stageIn;
        List<Stage> stages = researchIn.getStages();
        if (stageIn < stages.size()) {
            this.complete = false;
            this.tasks = new boolean[stages.get(stageIn).getTasks().size()];
        }
        else {
            this.complete = true;
            this.tasks = new boolean[0];
        }
    }

    public boolean grant(Research researchIn, int stageIn, int taskIn) {
        if (this.stage == stageIn) {
            if (taskIn < this.tasks.length && -1 < taskIn) {
                this.tasks[taskIn] = true;

                for (boolean b : this.tasks) {
                    if (!b) {
                        return false;
                    }
                }
                this.stage++;
                if (this.stage == researchIn.getStages().size()) {
                    this.complete = true;
                    this.tasks = new boolean[0];
                }
                return true;
            }
        }
        return false;
    }

    public int getStage() {
        return this.stage;
    }

    public boolean[] getTasks() {
        return this.tasks;
    }

    public boolean isComplete() {
        return this.complete;
    }

    public Builder copy() {
        return new Builder(this.stage, this.tasks);
    }

    public static final class Builder {

        private final int stage;
        private final boolean[] tasks;

        private Builder(int stageIn, boolean[] tasksIn) {
            this.stage = stageIn;
            this.tasks = tasksIn;
        }

        public ResearchProgress build(Research researchIn) {
            ResearchProgress progress = new ResearchProgress(researchIn, this.stage);
            System.arraycopy(this.tasks, 0, progress.tasks, 0, Math.min(this.tasks.length, progress.tasks.length));
            return progress;
        }

        public JsonObject serialize() {
            JsonObject json = new JsonObject();
            json.addProperty("stage", this.stage);
            if (this.tasks.length > 0) {
                JsonArray jsonArray = new JsonArray();
                for (boolean b : this.tasks) {
                    jsonArray.add(b);
                }
                json.add("tasks", jsonArray);
            }
            return json;
        }

        public static Builder deserialize(JsonObject jsonObject) {
            int stage = JSONUtils.getInt(jsonObject, "stage", 0);
            boolean[] tasks;
            if (JSONUtils.isJsonArray(jsonObject, "tasks")) {
                JsonArray jsonArray = JSONUtils.getJsonArray(jsonObject, "tasks");
                tasks = new boolean[jsonArray.size()];
                for (int i = 0; i < tasks.length; i++) {
                    JsonElement e = jsonArray.get(i);
                    if (e.isJsonPrimitive() && e.getAsJsonPrimitive().isBoolean()) {
                        tasks[i] = e.getAsBoolean();
                    }
                }
            }
            else {
                tasks = new boolean[0];
            }
            return new Builder(stage, tasks);
        }

        public void write(PacketBuffer bufferIn) {
            bufferIn.writeVarInt(this.stage);
            bufferIn.writeVarInt(this.tasks.length);
            for (boolean b : this.tasks) {
                bufferIn.writeBoolean(b);
            }
        }

        public static Builder read(PacketBuffer buffer) {
            int stage = buffer.readVarInt();
            boolean[] tasks = new boolean[buffer.readVarInt()];
            for (int i = 0; i < tasks.length; i++) {
                tasks[i] = buffer.readBoolean();
            }
            return new Builder(stage, tasks);
        }
    }
}
