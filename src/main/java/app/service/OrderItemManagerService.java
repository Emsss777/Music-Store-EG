package app.service;

import java.util.UUID;

public interface OrderItemManagerService {

    int deleteAllItemsByAlbumId(UUID albumId);

    int deleteEmptyOrders();
}
