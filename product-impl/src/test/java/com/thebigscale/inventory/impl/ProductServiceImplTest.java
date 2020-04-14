package com.thebigscale.inventory.impl;

import com.thebigscale.inventory.api.ProductService;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.*;
import static org.hamcrest.core.Is.is;

public class ProductServiceImplTest {

    @Test public void shouldRunProductService() {
        withServer(defaultSetup(), server -> {
            ProductService service = server.client(ProductService.class);

            String message = service.healthCheck().invoke().toCompletableFuture().get(5, TimeUnit.SECONDS);
            Assert.assertThat("Product service is up!!!", is(message));

        });
    }
}
