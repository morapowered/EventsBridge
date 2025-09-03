package io.github.morapowered.eventsbridge;


import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class EventsBridgeEntrypoint {

    public static void entrypoint() {
        EventsBridge.INSTANCE.onInitialize();
    }

}
