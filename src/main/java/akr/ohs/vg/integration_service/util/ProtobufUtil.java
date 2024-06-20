package akr.ohs.vg.integration_service.util;

import com.google.protobuf.StringValue;
import order.Order;
import user.User;

public class ProtobufUtil {

    private ProtobufUtil() {}

    public static StringValue str(String value) {
        return StringValue.newBuilder().setValue(value).build();
    }

    public static User.ShippingAddress address(String address, String country) {
        return User.ShippingAddress.newBuilder()
                .setAddress(str(address))
                .setCountry(str(country))
                .build();
    }

    public static User.PaymentMethod paymentMethod(String creditCardNumber, String creditCardType) {
        return User.PaymentMethod.newBuilder()
                .setCreditCardNumber(str(creditCardNumber))
                .setCreditCardType(str(creditCardType))
                .build();
    }

    public static Order.Product product(String pid) {
        return Order.Product.newBuilder()
                .setPid(pid)
                .build();
    }

}
