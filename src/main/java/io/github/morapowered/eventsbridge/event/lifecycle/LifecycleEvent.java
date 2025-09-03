package io.github.morapowered.eventsbridge.event.lifecycle;

import io.github.morapowered.eventsbridge.event.Event;
import net.minecraft.server.MinecraftServer;

public class LifecycleEvent implements Event {

    private final MinecraftServer server;

    public LifecycleEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return server;
    }
}
