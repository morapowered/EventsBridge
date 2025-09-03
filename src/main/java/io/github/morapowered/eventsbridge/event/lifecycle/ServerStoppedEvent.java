package io.github.morapowered.eventsbridge.event.lifecycle;

import net.minecraft.server.MinecraftServer;

public final class ServerStoppedEvent extends LifecycleEvent {

    public ServerStoppedEvent(MinecraftServer server) {
        super(server);
    }

}
