package com.thebigscale.inventory.impl;

import akka.Done;
import akka.NotUsed;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.cluster.sharding.typed.javadsl.Entity;
import akka.cluster.sharding.typed.javadsl.EntityRef;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.BadRequest;
import com.thebigscale.inventory.api.ProductDescView;
import com.thebigscale.inventory.api.ProductMessage;
import com.thebigscale.inventory.api.ProductService;
import com.thebigscale.inventory.impl.entity.ProductCommand;
import com.thebigscale.inventory.impl.entity.ProductEntity;
import com.thebigscale.inventory.impl.readside.Products;
import org.pcollections.PSequence;

import javax.inject.Inject;
import java.time.Duration;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class ProductServiceImpl implements ProductService {

    private Products products;
    private final ClusterSharding clusterSharing;

    private final Duration askTimeout = Duration.ofSeconds(5);

    @Inject
    public ProductServiceImpl(Products products,
                              ClusterSharding clusterSharing) {
        this.products = products;

        this.clusterSharing = clusterSharing;

        // register entity on shard
        this.clusterSharing.init(
                Entity.of(ProductEntity.ENTITY_TYPE_KEY,
                        ProductEntity::create)
        );
    }

    @Override
    public ServiceCall<NotUsed, String> healthCheck() {
        return notUsed -> supplyAsync(() -> "Product service is up!!!" );
    }

    @Override
    public ServiceCall<ProductMessage, Done> addProductListing(String id) {
        System.out.println("Added the product listing...");
        return request -> {
            // Look up the aggregate instance for the given ID.
            EntityRef<ProductCommand> productEntityActor = clusterSharing.entityRefFor(ProductEntity.ENTITY_TYPE_KEY, id);
            return productEntityActor.
                    <ProductCommand.Confirmation>ask(replyTo ->
                            new ProductCommand.UseProductMessage(request.getMessage(), replyTo), askTimeout)
                    .thenApply(this::handleConfirmation)
                    .thenApply(accepted -> Done.getInstance());
        };
    }

    @Override
    public ServiceCall<NotUsed, PSequence<ProductDescView>> getAllProducts() {
        return request -> products.getAllProducts();
    }

    /**
     * Try to converts Confirmation to a Accepted
     *
     * @throws BadRequest if Confirmation is a Rejected
     */
    private ProductCommand.Accepted handleConfirmation(ProductCommand.Confirmation confirmation) {
        if (confirmation instanceof ProductCommand.Accepted) {
            ProductCommand.Accepted accepted = (ProductCommand.Accepted) confirmation;
            return accepted;
        }

        ProductCommand.Rejected rejected = (ProductCommand.Rejected) confirmation;
        throw new BadRequest(rejected.getReason());
    }

}
