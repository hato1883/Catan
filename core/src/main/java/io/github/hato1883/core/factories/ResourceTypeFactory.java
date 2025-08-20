package io.github.hato1883.core.factories;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.factories.IResourceTypeFactory;
import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.core.game.entities.resource.ResourceType;

public class ResourceTypeFactory implements IResourceTypeFactory {

    @Override
    public IResourceType create(Identifier id, String name, String description) {
        return new ResourceType(id, name, description);
    }

    @Override
    public IResourceType create(Identifier id, String name) {
        return new ResourceType(id, name);
    }
}
