package com.thebigscale.inventory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.Value;

@Value
@JsonDeserialize
public final class ProductMessage {
  @NonNull String message;

  @JsonCreator
  public ProductMessage(@NonNull String message) {
    this.message = Preconditions.checkNotNull(message, "message field MUST not be null");
  }
}
