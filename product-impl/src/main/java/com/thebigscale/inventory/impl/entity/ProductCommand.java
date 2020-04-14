package com.thebigscale.inventory.impl.entity;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

public interface ProductCommand extends Jsonable {

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class UseProductMessage implements ProductCommand, CompressedJsonable {
        public final String message;
        public final ActorRef<Confirmation> replyTo;

        @JsonCreator
        public UseProductMessage(String message, ActorRef<Confirmation> replyTo) {
            this.message = Preconditions.checkNotNull(message, "message");
            this.replyTo = replyTo;
        }
    }

    // The commands above will use different reply types (see below all the reply types).
    interface Confirmation {
    }

    @Value
    @JsonDeserialize
    final class Accepted implements Confirmation {
    }

    @Value
    @JsonDeserialize
    final class Rejected implements Confirmation {
        public final String reason;

        public Rejected(String reason) {
            this.reason = reason;
        }
    }

}
