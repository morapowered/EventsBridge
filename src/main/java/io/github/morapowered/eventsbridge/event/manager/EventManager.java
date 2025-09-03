package io.github.morapowered.eventsbridge.event.manager;


import io.github.morapowered.eventsbridge.event.Event;
import net.engio.mbassy.bus.IMessagePublication;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;
import net.engio.mbassy.bus.error.PublicationError;
import net.minecraft.Util;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    public static final String DEFAULT_MOD_ID = "EventsBridge";

    private final Logger logger;
    private final HashMap<String, MBassador<Event>> listeners = new HashMap<>();
    private final HashMap<Object, String> objectListeners = new HashMap<>();

    public EventManager(Logger logger) {
        this.logger = logger;
    }

    /**
     * Register a listener from mod
     *
     * @param modId    who is the mod
     * @param listener the listener to register
     */
    public void register(String modId, Object listener) {
        MBassador<Event> bus = listeners.computeIfAbsent(modId, id -> createBus());
        bus.subscribe(listener);
        objectListeners.put(listener, modId);
    }

    /**
     * Register a listener
     * Listeners register by this method are unregistered in {@link net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents#SERVER_STOPPED}, for something more controlled, use register {@link EventManager#register(String, Object)}.
     *
     * @param listener the listener to register
     */
    public void register(Object listener) {
        this.register(DEFAULT_MOD_ID, listener);
    }

    /**
     * Unregister a listener
     *
     * @param listener listener to unregister
     * @return if it's unregistered
     */
    public boolean unregister(Object listener) {
        String modId = objectListeners.get(listener);
        if (modId != null) {
            return listeners.get(modId).unsubscribe(listener);
        }
        return false;
    }

    /**
     * Unregister listeners from mod
     *
     * @param modId mod to unregister
     * @return ig the mod bus was removed
     */
    public boolean unregister(String modId) {
        MBassador<Event> bus = listeners.remove(modId);
        if (bus == null) {
            return false;
        }

        List<Object> busListeners = objectListeners.entrySet().stream()
                .filter(entry -> entry.getValue().equals(modId))
                .map(Map.Entry::getKey)
                .toList();

        busListeners.forEach(listener -> {
            bus.unsubscribe(listener);
            objectListeners.remove(listener);
        });

        return true;
    }

    /**
     * Post event on the all listeners
     *
     * @param event event to post
     */
    public List<IMessagePublication> post(Event event) {
        List<IMessagePublication> publications = new ArrayList<>();
        listeners.forEach((modId, bus) -> publications.add(bus.post(event).now()));
        return publications;
    }

    private MBassador<Event> createBus() {
        // TODO: setup async message invocation/dispatch
        return new MBassador<>(new BusConfiguration()
                .addFeature(Feature.SyncPubSub.Default())
                .addFeature(new Feature.AsynchronousHandlerInvocation().setExecutor(Util.backgroundExecutor()))
                .addFeature(Feature.AsynchronousMessageDispatch.Default())
                .addPublicationErrorHandler(this::errorHandler));
    }

    private void errorHandler(PublicationError error) {
        if (error.getCause() != null) {
            if (error.getMessage() != null) {
                logger.error(error.getMessage(), error.getCause());
            } else {
                error.getCause().printStackTrace(System.err);
            }
        } else {
            if (error.getMessage() != null) {
                logger.error(error.getMessage());
            } else {
                logger.error("Error on publication: {}.", error);
            }
        }
    }

}
