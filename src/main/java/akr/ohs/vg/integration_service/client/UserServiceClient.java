package akr.ohs.vg.integration_service.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import user.User;
import user.UserServiceGrpc;

import java.util.Optional;

import static akr.ohs.vg.integration_service.util.ProtobufUtil.*;

@Service
@Slf4j
public class UserServiceClient {
    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    @Value("${user.host}")
    private String host;

    private int port = 50054;

    @PostConstruct
    public void init() {
        log.info("Creating gRPC client for {}:{}", host, port);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    public Optional<User.UserResponse> createUser(String fullName, String email,
                                                 String address, String country,
                                                 String creditCardNumber, String creditCardType) {

        User.CreateUserRequest createUserRequest = User.CreateUserRequest.newBuilder()
                .setFullName(str(fullName))
                .setEmail(email)
                .setAddress(address(address, country))
                .addPaymentMethods(paymentMethod(creditCardNumber, creditCardType))
                .build();

        try {
            return Optional.of(blockingStub.createUser(createUserRequest));
        } catch (Exception e) {
            log.warn("Failed to create user '{}'. Error message: {}", fullName, e.getMessage());
            return Optional.empty();
        }

    }

}
