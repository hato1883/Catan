package io.github.hato1883.core.game.entities.resource;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.entities.resource.IResourceType;

public class ResourceType implements IResourceType {
    private final Identifier id;
    private final String name;
    private final String desc;

    public ResourceType(Identifier id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public ResourceType(Identifier id, String name) {
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
