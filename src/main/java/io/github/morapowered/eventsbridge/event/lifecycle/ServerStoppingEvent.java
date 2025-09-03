package io.github.morapowered.eventsbridge.event.lifecycle;

import net.minecraft.server.MinecraftServer;

public final class ServerStoppingEvent extends LifecycleEvent {

    public ServerStoppingEvent(MinecraftServer server) {
        super(server);
    }

}
