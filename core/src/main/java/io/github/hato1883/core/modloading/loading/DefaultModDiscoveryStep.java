package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.load.IModDiscovery;
import org.slf4j.Logger;
import io.github.hato1883.api.LogManager;
import java.nio.file.Path;
import java.util.List;

/**
 * Step 1: Discover all mods and store their paths in the context.
 */
public class DefaultModDiscoveryStep implements ModLoadingStep {
    private static final Logger LOGGER = LogManager.getLogger("ModLoading");
    private final IModDiscovery discovery;
    private final Path modsDir;

    public DefaultModDiscoveryStep(IModDiscovery discovery, Path modsDir) {
        this.discovery = discovery;
        this.modsDir = modsDir;
    }

    @Override
    public void execute(ModLoadingContext context) throws Exception {
        LOGGER.info("Discovering mods in {}...", modsDir);
        List<Path> modPaths = discovery.discoverMods(modsDir);
        context.setDiscoveredModPaths(modPaths);
        LOGGER.info("Discovered {} mods.", modPaths.size());
    }
}

