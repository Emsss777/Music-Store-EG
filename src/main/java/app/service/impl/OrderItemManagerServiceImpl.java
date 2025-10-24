package app.service.impl;

import app.model.entity.Order;
import app.model.entity.OrderItem;
import app.repository.OrderItemRepo;
import app.repository.OrderRepo;
import app.service.OrderItemManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderItemManagerServiceImpl implements OrderItemManagerService {

    private final OrderItemRepo orderItemRepo;
    private final OrderRepo orderRepo;

    @Override
    public int deleteAllItemsByAlbumId(UUID albumId) {

        List<OrderItem> items = orderItemRepo.findByAlbumId(albumId);

        if (!items.isEmpty()) {
            orderItemRepo.deleteAll(items);
        }

        return items.size();
    }

    @Override
    public int deleteEmptyOrders() {

        List<Order> emptyOrders = orderRepo.findAll().stream()
                .filter(order -> order.getItems().isEmpty())
                .toList();

        if (!emptyOrders.isEmpty()) {
            orderRepo.deleteAll(emptyOrders);
        }

        return emptyOrders.size();
    }
}
