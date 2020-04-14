package com.thebigscale.inventory.impl.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

public interface ProductEvent extends Jsonable, AggregateEvent<ProductEvent> {
    /**
     * Tags are used for getting and publishing streams of events. Each event
     * will have this tag, and in this case, we are partitioning the tags into
     * 4 shards, which means we can have 4 concurrent processors/publishers of
     * events.
     */
    AggregateEventShards<ProductEvent> TAG = AggregateEventTag.sharded(ProductEvent.class, 4);

    /**
     * An event that represents a change in greeting message.
     */
    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class ProductMessageChanged implements ProductEvent {
        public final String name;
        public final String message;

        @JsonCreator
        ProductMessageChanged(String name, String message) {
            this.name = Preconditions.checkNotNull(name, "name");
            this.message = Preconditions.checkNotNull(message, "message");
        }
    }

    @Override
    default AggregateEventTagger<ProductEvent> aggregateTag() {
        return TAG;
    }
}