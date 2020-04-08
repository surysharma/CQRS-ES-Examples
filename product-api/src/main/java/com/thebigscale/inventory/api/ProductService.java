package com.thebigscale.inventory.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import static com.lightbend.lagom.javadsl.api.Service.*;

public interface ProductService extends Service {

    ServiceCall<NotUsed,String> test();

    //This will define the REST/gRPC calls
    @Override default Descriptor descriptor(){
        return named("product-service").withCalls(
                restCall(Method.GET, "/api/product/test", this::test))
                .withAutoAcl(true);
    }

}
