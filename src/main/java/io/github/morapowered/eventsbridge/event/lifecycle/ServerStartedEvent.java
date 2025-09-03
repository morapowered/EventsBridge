package io.github.morapowered.eventsbridge.event.lifecycle;

import net.minecraft.server.MinecraftServer;

public final class ServerStartedEvent extends LifecycleEvent {

    public ServerStartedEvent(MinecraftServer server) {
        super(server);
    }

}
