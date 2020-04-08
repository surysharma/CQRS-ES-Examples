package com.thebigscale.inventory.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.thebigscale.inventory.api.ProductService;

/**
 * The module that binds the Service api with the implementation.
 */
public class ProductServiceModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(ProductService.class, ProductServiceImpl.class);
    }
}
