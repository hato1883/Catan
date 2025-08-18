package io.github.hato1883.api.game;

import io.github.hato1883.api.Identifier;

public interface IResourceType {
    Identifier getId();           // unique internal id e.g. "catan:brick"
    String getName();         // canonical name, e.g. "brick"
    String getDescription();  // optional description or tooltip e.g. "produced by 'catan:clay_patch'"
}
