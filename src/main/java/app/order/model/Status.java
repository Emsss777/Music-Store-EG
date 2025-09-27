package app.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    PENDING,
    PAID,
    SHIPPED,
    COMPLETED,
    CANCELED
}
