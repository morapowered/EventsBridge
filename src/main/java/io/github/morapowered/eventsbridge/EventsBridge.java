package io.github.morapowered.eventsbridge;

import com.mojang.logging.LogUtils;
import io.github.morapowered.eventsbridge.event.lifecycle.ServerStartingEvent;
import io.github.morapowered.eventsbridge.event.lifecycle.ServerStoppedEvent;
import io.github.morapowered.eventsbridge.event.lifecycle.ServerStoppingEvent;
import io.github.morapowered.eventsbridge.event.manager.EventManager;
import io.github.morapowered.eventsbridge.util.BuildConstants;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;


public final class EventsBridge {

    public static final EventsBridge INSTANCE = new EventsBridge();

    private final Logger logger = LogUtils.getLogger();
    private final EventManager eventManager = new EventManager(logger);

    void onInitialize() {
        logger.info("EventsBridge (version: {}, branch: {}, build: {})", BuildConstants.VERSION, BuildConstants.BRANCH, BuildConstants.BUILD);
        ServerLifecycleEvents.SERVER_STARTING.register(server -> eventManager.post(new ServerStartingEvent(server)));
        ServerLifecycleEvents.SERVER_STARTED.register(server -> eventManager.post(new ServerStartingEvent(server)));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> eventManager.post(new ServerStoppingEvent(server)));
        ServerLifecycleEvents.SERVER_STOPPED.register(this::onStopped);
        logger.info("EventsBridge initialized");
    }

    void onStopped(MinecraftServer server) {
        eventManager.post(new ServerStoppedEvent(server));
        eventManager.unregister(EventManager.DEFAULT_MOD_ID);
    }

    /**
     * Get EventManager
     *
     * @return the event manager
     * @see EventManager
     */
    public @NotNull EventManager getEventManager() {
        return eventManager;
    }

    public static EventsBridge get() {
        return INSTANCE;
    }

}
