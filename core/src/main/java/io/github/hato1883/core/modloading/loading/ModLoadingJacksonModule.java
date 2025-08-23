package io.github.hato1883.core.modloading.loading;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.hato1883.api.mod.load.dependency.VersionConstraint;

public class ModLoadingJacksonModule extends SimpleModule {
    public ModLoadingJacksonModule() {
        super("ModLoadingJacksonModule", Version.unknownVersion());
        addDeserializer(VersionConstraint.class, new VersionConstraintDeserializer());
        addDeserializer(io.github.hato1883.api.mod.load.dependency.ModDependency.class, new ModDependencyDeserializer());
    }
}
