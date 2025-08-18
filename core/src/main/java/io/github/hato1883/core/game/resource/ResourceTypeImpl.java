package io.github.hato1883.core.game.resource;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.game.IResourceType;

public class ResourceTypeImpl implements IResourceType {
    private final Identifier id;
    private final String name;
    private final String desc;

    ResourceTypeImpl(Identifier id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    ResourceTypeImpl(Identifier id, String name) {
        this.id = id;
        this.name = name;
        this.desc = "";
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return desc;
    }
}
