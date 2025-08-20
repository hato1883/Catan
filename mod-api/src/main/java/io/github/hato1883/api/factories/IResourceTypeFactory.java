package io.github.hato1883.api.factories;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.entities.resource.IResourceType;

public interface IResourceTypeFactory {
    IResourceType create(Identifier id, String name, String description);
    IResourceType create(Identifier id, String name);
}
