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

    private static class ModClassLoader extends URLClassLoader {
        public ModClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            if (name.startsWith("io.github.hato1883.api.")) {
                return getParent().loadClass(name);
            }
            return super.loadClass(name, resolve);
        }
    }

    @Override
    public ClassLoader createClassLoader(Path modPath) throws Exception {
        URL url = modPath.toUri().toURL();
        return new ModClassLoader(new URL[] { url }, parent);
    }
}
