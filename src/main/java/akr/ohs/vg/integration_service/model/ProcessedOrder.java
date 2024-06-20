package akr.ohs.vg.integration_service.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProcessedOrder {
    private String userPid;
    private String orderPid;
    private String supplierPid;
}
