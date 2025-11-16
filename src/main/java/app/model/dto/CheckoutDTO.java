package app.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String cardName;
}
