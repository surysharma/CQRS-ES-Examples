package com.thebigscale.inventory.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;
import org.pcollections.PSequence;

import static com.lightbend.lagom.javadsl.api.Service.*;

public interface ProductService extends Service {

    ServiceCall<NotUsed,String> healthCheck();
    ServiceCall<ProductMessage, Done> addProductListing(String id);
    ServiceCall<NotUsed, PSequence<ProductDescView>> getAllProducts();


    //This will define the REST/gRPC calls
    @Override default Descriptor descriptor(){
        return named("product-service").withCalls(
                restCall(Method.GET, "/api/product/test", this::healthCheck),
                restCall(Method.POST, "/api/product/:id", this::addProductListing),
                restCall(Method.GET, "/api/products", this::getAllProducts))
                .withAutoAcl(true);
    }

}
