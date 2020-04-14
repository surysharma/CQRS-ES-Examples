package com.thebigscale.inventory.impl.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

@Value
@JsonDeserialize
public final class ProductState implements CompressedJsonable {
    public static final ProductState INITIAL = new ProductState("");
    public final String message;

    public ProductState(String message) {
        this.message = Preconditions.checkNotNull(message, "message");
    }
    public ProductState withMessage(String message) {
        return new ProductState(message);
    }

}
