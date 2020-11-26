package com.bigchickenstudios.divinature.research.tasks;

import com.bigchickenstudios.divinature.research.PlayerResearch;
import com.bigchickenstudios.divinature.research.Research;
import com.bigchickenstudios.divinature.research.Task;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Set;

public abstract class TaskTrigger<S, T extends ITaskInstance<S>> {

    private final Map<PlayerResearch, Set<Listener<S, T>>> tasks = Maps.newIdentityHashMap();

    private final ResourceLocation id;

    public TaskTrigger(ResourceLocation idIn) {
        this.id = idIn;
    }

    public final ResourceLocation getId() {
        return this.id;
    }

    public final void addTask(PlayerResearch playerResearch, Listener<S, T> taskInstance) {
        this.tasks.computeIfAbsent(playerResearch, (pr) -> Sets.newHashSet()).add(taskInstance);
    }
    public final void removeTask(PlayerResearch playerResearch, Listener<S, T> taskInstance) {
        Set<Listener<S, T>> s = this.tasks.get(playerResearch);
        if (s != null) {
            s.remove(taskInstance);
            if (s.isEmpty()) {
                this.tasks.remove(playerResearch);
            }
        }
    }
    public final void removeAllTasks(PlayerResearch playerResearch) {
        this.tasks.remove(playerResearch);
    }

    protected final void test(PlayerResearch playerResearch, S s) {
        Set<Listener<S, T>> set = this.tasks.get(playerResearch);
        if (set != null) {
            for (Listener<S, T> listener : set) {
                if (listener.getTaskInstance().test(s)) {
                    listener.grant(playerResearch);
                    set.remove(listener);
                }
            }
        }
    }
    public Task<S, T> deserializeTask(JsonObject jsonObject) {
        return new Task<>(this, this.deserializeInstance(jsonObject));
    }

    protected abstract T deserializeInstance(JsonObject jsonObject);

    public static final class Listener<S, T extends ITaskInstance<S>> {

        private final T taskInstance;
        private final Research research;
        private final int stage;
        private final int task;

        public Listener(Research researchIn, int stageIn, int taskIn, T taskInstanceIn) {
            this.research = researchIn;
            this.stage = stageIn;
            this.task = taskIn;
            this.taskInstance = taskInstanceIn;
        }

        private T getTaskInstance() {
            return this.taskInstance;
        }

        private void grant(PlayerResearch playerResearch) {
            playerResearch.grant(this.research, this.stage, this.task);
        }
    }
}
