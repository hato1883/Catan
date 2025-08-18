package io.github.hato1883.api.mod.load;

import java.nio.file.Path;

/** Creates a ClassLoader for a mod path (jar or exploded dir). */
public interface IModClassLoaderFactory {
    ClassLoader createClassLoader(Path modPath) throws Exception;
}
