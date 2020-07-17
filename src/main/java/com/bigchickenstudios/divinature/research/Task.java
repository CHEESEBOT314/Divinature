package com.bigchickenstudios.divinature.research;

import com.bigchickenstudios.divinature.research.tasks.TaskTrigger;
import com.bigchickenstudios.divinature.research.tasks.Triggers;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class Task<S, T extends ITaskInstance<S>> {

    private final TaskTrigger<S, T> trigger;
    private final T instance;

    private ITextComponent info;

    public Task(TaskTrigger<S, T> triggerIn, T instanceIn) {
        this.trigger = triggerIn;
        this.instance = instanceIn;
    }

    public void addListener(PlayerResearch playerResearchIn, Research researchIn, int stageIn, int taskIn) {
        this.trigger.addTask(playerResearchIn, new TaskTrigger.Listener<>(researchIn, stageIn, taskIn, this.instance));
    }

    private void setInfo(ITextComponent infoIn) {
        this.info = infoIn;
    }

    public ITextComponent getInfo() {
        return this.info;
    }

    public static Task<?, ?> deserialize(JsonObject jsonObject) {
        ResourceLocation id = new ResourceLocation(JSONUtils.getString(jsonObject, "type"));
        TaskTrigger<?, ?> taskTrigger = Triggers.get(id);
        if (taskTrigger == null) {
            throw new IllegalArgumentException("No TaskTrigger of type: " + id.toString());
        }
        JsonObject conditions = JSONUtils.getJsonObject(jsonObject, "conditions");
        Task<?, ?> task = taskTrigger.deserializeTask(conditions);
        if (jsonObject.has("info")) {
            task.setInfo(ITextComponent.Serializer.func_240641_a_(jsonObject.get("info")));
        }
        else {
            throw new JsonSyntaxException("Missing info, expected to find a JsonElement");
        }
        return task;
    }

    public void write(PacketBuffer buffer) {
        boolean flag = this.info != null;
        buffer.writeBoolean(flag);
        if (flag) {
            buffer.writeTextComponent(this.info);
        }
    }

    public static Task<?, ?> read(PacketBuffer buffer) {
        Task<?, ?> task = new Task<>(null, null);
        if (buffer.readBoolean()) {
            task.setInfo(buffer.readTextComponent());
        }
        return task;
    }
}
