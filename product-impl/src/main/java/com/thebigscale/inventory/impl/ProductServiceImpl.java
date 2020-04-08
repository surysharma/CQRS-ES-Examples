package com.thebigscale.inventory.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.thebigscale.inventory.api.ProductService;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class ProductServiceImpl implements ProductService {
    @Override
    public ServiceCall<NotUsed, String> test() {
        return notUsed -> supplyAsync(() -> "Product service is up!!!" );
    }
}
