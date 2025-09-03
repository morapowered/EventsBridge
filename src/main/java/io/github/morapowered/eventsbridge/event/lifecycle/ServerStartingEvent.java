package io.github.morapowered.eventsbridge.event.lifecycle;

import net.minecraft.server.MinecraftServer;

public final class ServerStartingEvent extends LifecycleEvent {

    public ServerStartingEvent(MinecraftServer server) {
        super(server);
    }

}
