package io.github.morapowered.eventsbridge;

import com.mojang.logging.LogUtils;
import io.github.morapowered.eventsbridge.util.BuildConstants;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class EventsBridgeMod implements ModInitializer {

    private final Logger logger = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        logger.info("EventsBridge (version: {}, branch: {}, build: {})", BuildConstants.VERSION, BuildConstants.BRANCH, BuildConstants.BUILD);

        logger.info("EventsBridge initialized");
    }
}
