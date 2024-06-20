package akr.ohs.vg.integration_service.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import order.Order;
import order.OrderServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static akr.ohs.vg.integration_service.util.ProtobufUtil.product;
import static akr.ohs.vg.integration_service.util.ProtobufUtil.str;

@Service
@Slf4j
public class OrderServiceClient {
    private OrderServiceGrpc.OrderServiceBlockingStub blockingStub;

    @Value("${order.host}")
    private String host;

    private int port = 50053;

    @PostConstruct
    public void init() {
        log.info("Creating gRPC client for {}:{}", host, port);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = OrderServiceGrpc.newBlockingStub(channel);
    }

    public Optional<Order.OrderResponse> createOrder(String userPid,
                                                     String productPid, int quantity, String dateCreated) {

        Order.CreateOrderRequest createOrderRequest = Order.CreateOrderRequest.newBuilder()
                .setUserPid(userPid)
                .addProducts(product(productPid))
                .setQuantity(quantity)
                .setDateCreated(str(dateCreated.substring(0,10)))
                .build();

        try {
            return Optional.of(blockingStub.createOrder(createOrderRequest));
        } catch (Exception e) {
            log.warn("Failed to create order with product-id {} and user-id {}. Error message: {}",
                    productPid, userPid, e.getMessage());
            return Optional.empty();
        }
    }

}
