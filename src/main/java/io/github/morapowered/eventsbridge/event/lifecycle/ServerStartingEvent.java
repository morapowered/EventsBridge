package io.github.morapowered.eventsbridge.event.lifecycle;

import io.github.morapowered.eventsbridge.event.Event;
import net.minecraft.server.MinecraftServer;

public final class ServerStartingEvent implements Event {

    private final MinecraftServer server;

    public ServerStartingEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return server;
    }

}
