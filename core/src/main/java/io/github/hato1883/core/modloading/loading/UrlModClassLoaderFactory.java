package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.load.IModClassLoaderFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class UrlModClassLoaderFactory implements IModClassLoaderFactory {

    private final ClassLoader parent;

    public UrlModClassLoaderFactory(ClassLoader parent) {
        this.parent = parent;
    }

    @Override
    public ClassLoader createClassLoader(Path modPath) throws Exception {
        URL url = modPath.toUri().toURL();
        return new URLClassLoader(new URL[] { url }, parent);
    }
}
