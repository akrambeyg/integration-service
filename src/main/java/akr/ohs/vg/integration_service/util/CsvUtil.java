package akr.ohs.vg.integration_service.util;

import akr.ohs.vg.integration_service.model.Order;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvUtil {

    public static List<Order> readOrdersFromFile(Resource resource) {

        List<Order> orders;

        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            CsvToBean<Order> csvToBean = new CsvToBeanBuilder<Order>(reader)
                    .withType(Order.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
            log.error("Failed to parse CSV file {}", resource.getFilename(), e);
            orders = new ArrayList<>();
        }

        return orders;
    }

}
