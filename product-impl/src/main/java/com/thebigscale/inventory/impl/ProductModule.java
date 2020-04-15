package com.thebigscale.inventory.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.internal.javadsl.persistence.jdbc.JavadslJdbcOffsetStore;
import com.lightbend.lagom.internal.javadsl.persistence.jdbc.SlickProvider;
import com.lightbend.lagom.internal.persistence.jdbc.SlickOffsetStore;
import com.lightbend.lagom.javadsl.persistence.jdbc.GuiceSlickProvider;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.thebigscale.inventory.api.ProductService;
import com.thebigscale.inventory.impl.readside.Products;

public class ProductModule  extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(ProductService.class, ProductServiceImpl.class);
        bind(Products.class).asEagerSingleton();

        // JdbcPersistenceModule is disabled in application.conf to avoid conflicts with CassandraPersistenceModule.
        // We need to explicitly re-add the SlickOffsetStore binding that is required by the JpaPersistenceModule.
        bind(SlickProvider.class).toProvider(GuiceSlickProvider.class);
        bind(SlickOffsetStore.class).to(JavadslJdbcOffsetStore.class);
    }
}
