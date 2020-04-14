package com.thebigscale.inventory.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.thebigscale.inventory.api.ProductService;
import com.thebigscale.inventory.impl.readside.Products;

public class ProductModule  extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(ProductService.class, ProductServiceImpl.class);
        bind(Products.class).asEagerSingleton();
    }
}
