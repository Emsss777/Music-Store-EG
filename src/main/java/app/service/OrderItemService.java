package app.service;

import java.util.UUID;

public interface OrderItemService {

    int deleteAllItemsByAlbumId(UUID albumId);

    int deleteEmptyOrders();
}
