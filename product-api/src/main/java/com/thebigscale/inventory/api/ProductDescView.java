package com.thebigscale.inventory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.Value;

@Value
@JsonDeserialize
public class ProductDescView {
    @NonNull String id;
    @NonNull String message;

    @JsonCreator
    public ProductDescView(String id, String message) {
        this.id = Preconditions.checkNotNull(id, "id field MUST not be null");
        this.message = Preconditions.checkNotNull(message, "message field MUST not be null");
    }
}
