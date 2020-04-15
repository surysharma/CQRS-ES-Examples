package com.thebigscale.inventory.impl.entity;

import akka.cluster.sharding.typed.javadsl.EntityContext;
import akka.cluster.sharding.typed.javadsl.EntityTypeKey;
import akka.persistence.typed.PersistenceId;
import akka.persistence.typed.javadsl.*;

public class ProductEntity extends EventSourcedBehaviorWithEnforcedReplies<ProductCommand, ProductEvent, ProductState> {

    public static EntityTypeKey<ProductCommand> ENTITY_TYPE_KEY =
            EntityTypeKey
                    .create(ProductCommand.class, "ProductEntity");

    final private EntityContext<ProductCommand> entityContext;
    final private String entityId;

    public ProductEntity(EntityContext<ProductCommand> entityContext) {
        super(PersistenceId.of(
                entityContext.getEntityTypeKey().name(),
                entityContext.getEntityId()
        ));
        this.entityContext = entityContext;
        this.entityId = entityContext.getEntityId();
    }

    public static ProductEntity create(EntityContext<ProductCommand> entityContext) {
        return new ProductEntity(entityContext);
    }

    @Override
    public ProductState emptyState() {
        return ProductState.INITIAL;
    }

    @Override
    public CommandHandlerWithReply<ProductCommand, ProductEvent, ProductState> commandHandler() {
        System.out.println("Inside the command handler!!!");
        CommandHandlerWithReplyBuilder<ProductCommand, ProductEvent, ProductState> builder = newCommandHandlerWithReplyBuilder();

        /*
         * Command handler for the UseProductMessage command.
         */
        builder.forAnyState()
                .onCommand(ProductCommand.UseProductMessage.class, (state, cmd) ->
                        Effect()
                                // In response to this command, we want to first persist it as a
                                // ProductMessageChanged event
                                .persist(new ProductEvent.ProductMessageChanged(entityId, cmd.message))
                                // Then once the event is successfully persisted, we respond with done.
                                .thenReply(cmd.replyTo, __ -> new ProductCommand.Accepted())
                );

        return builder.build();
    }

    @Override
    public EventHandler<ProductState, ProductEvent> eventHandler() {
        System.out.println("Inside the event handler!!!");

        EventHandlerBuilder<ProductState, ProductEvent> builder = newEventHandlerBuilder();

        /*
         * Event handler for the ProductMessageChanged event.
         */
        builder.forAnyState()
                .onEvent(ProductEvent.ProductMessageChanged.class, (state, evt) ->
                        // We simply update the current state to use the greeting message from
                        // the event.
                        state.withMessage(evt.message)
                );
        return builder.build();
    }
}
