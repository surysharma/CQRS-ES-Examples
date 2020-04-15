package com.thebigscale.inventory.impl.readside;

import com.google.common.collect.ImmutableMap;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.jpa.JpaReadSide;
import com.lightbend.lagom.javadsl.persistence.jpa.JpaSession;
import com.thebigscale.inventory.api.ProductDescView;
import com.thebigscale.inventory.impl.entity.ProductEvent;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Singleton
public class Products {
    private static final String SELECT_ALL_QUERY =
            // JPA entities are mutable and cannot be safely shared across threads.
            // The "SELECT NEW" syntax is used to return immutable result objects.
            "SELECT NEW com.thebigscale.inventory.api.ProductDescView(r.id, r.message)" +
                    " FROM ProductRecord r";

    // JpaSession provides an asynchronous, non-blocking API to
    // perform JPA actions in Slick's database execution context.
    private final JpaSession jpaSession;


    @Inject
    Products(JpaSession jpaSession, ReadSide readSide) {
        this.jpaSession = jpaSession;

        // This registers an event processor with Lagom.
        // Event processors are used to update the read-side
        // database with changes made to persistent entities.
        readSide.register(ProductReadSideProcessor.class);
    }

    public CompletionStage<PSequence<ProductDescView>> getAllProducts() {
        return jpaSession
                .withTransaction(this::selectAllProducts)
                .thenApply(TreePVector::from);
    }

    private List<ProductDescView> selectAllProducts(EntityManager entityManager) {
        return entityManager
                .createQuery(SELECT_ALL_QUERY, ProductDescView.class)
                .getResultList();
    }

    static class ProductReadSideProcessor extends ReadSideProcessor<ProductEvent>{
        private final JpaReadSide jpaReadSide;

        @Inject
        ProductReadSideProcessor(JpaReadSide jpaReadSide) {
            this.jpaReadSide = jpaReadSide;
        }
        @Override
        public ReadSideHandler<ProductEvent> buildHandler() {
            return jpaReadSide.<ProductEvent>builder("ProductReadSideProcessor")
                    .setGlobalPrepare(entityManager -> createSchema())
                    .setEventHandler(ProductEvent.ProductMessageChanged.class, this::processProductMessageChanged)
                    .build();
        }


        private void createSchema() {
            // This is a convenience for creating the read-side table in development mode.
            // It relies on a Hibernate-specific property to provide idempotent schema updates.
            Persistence.generateSchema("default",
                    ImmutableMap.of("hibernate.hbm2ddl.auto", "update")
            );
        }

        private void processProductMessageChanged(EntityManager entityManager, ProductEvent.ProductMessageChanged productMessageChanged) {
            System.out.println("Inside processProductMessageChanged, productMessageChanged=" + productMessageChanged);
            ProductRecord record = entityManager.find(ProductRecord.class, productMessageChanged.getName());
            if (record == null) {
                record = new ProductRecord();
                record.setId(productMessageChanged.getName());
                entityManager.persist(record);
            }
            record.setMessage(productMessageChanged.getMessage());
        }

        @Override
        public PSequence<AggregateEventTag<ProductEvent>> aggregateTags() {
            return ProductEvent.TAG.allTags();
        }
    }

}

