package akr.ohs.vg.integration_service.service;

import akr.ohs.vg.integration_service.client.OrderServiceClient;
import akr.ohs.vg.integration_service.client.UserServiceClient;
import akr.ohs.vg.integration_service.model.Order;
import akr.ohs.vg.integration_service.model.ProcessedOrder;
import akr.ohs.vg.integration_service.util.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    @Value("classpath:order-integration.csv")
    Resource csvResource;

    @Autowired
    UserServiceClient userServiceClient;

    @Autowired
    OrderServiceClient orderServiceClient;

    public List<ProcessedOrder> processOrders() {

        List<ProcessedOrder> processedOrders = new ArrayList<>();

        log.info("Loading orders from {}", csvResource.getFilename());
        var orders = CsvUtil.readOrdersFromFile(csvResource);
        log.info("Loaded {} order(s) from file", orders.size());

        orders.forEach(order -> {
            var processedOrder = processedOrder(order);
            processedOrder.ifPresent(processedOrders::add);
        });

        return processedOrders;
    }

    private Optional<ProcessedOrder> processedOrder(Order order) {
        // create user
        var createUserResponse = userServiceClient.createUser(order.getFullName(), order.getEmail(), order.getShippingAddress(),
                order.getCountry(), order.getCreditCardNumber(), order.getCreditCardType());
        if (createUserResponse.isEmpty()) {
            return Optional.empty();
        }

        // create order
        var createOrderResponse = orderServiceClient.createOrder(
                createUserResponse.get().getPid(), order.getProductPid(), order.getQuantity(), order.getDateCreated()
        );
        if (createOrderResponse.isEmpty()) {
            return Optional.empty();
        }

        // create ProcessedOrder
        ProcessedOrder processedOrder = new ProcessedOrder()
                .setUserPid(createUserResponse.get().getPid())
                .setOrderPid(order.getOrderId()) // should instead come from createOrderResponse, but is always empty
                .setSupplierPid(order.getSupplierPid()); // should instead come from createOrderResponse, but such field does not exist

        return Optional.of(processedOrder);
    }

}