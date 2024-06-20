package akr.ohs.vg.integration_service.web;

import akr.ohs.vg.integration_service.model.ProcessedOrder;
import akr.ohs.vg.integration_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class ProcessOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/processed_orders")
    public List<ProcessedOrder> processOrders() {
        var processedOrders = orderService.processOrders();
        log.info("Returning {} processed order(s)", processedOrders.size());
        return processedOrders;
    }

}