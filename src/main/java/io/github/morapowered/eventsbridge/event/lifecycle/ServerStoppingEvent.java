package io.github.morapowered.eventsbridge.event.lifecycle;

import io.github.morapowered.eventsbridge.event.Event;
import net.minecraft.server.MinecraftServer;

public final class ServerStoppingEvent implements Event {

    private final MinecraftServer server;

    public ServerStoppingEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return server;
    }

}
