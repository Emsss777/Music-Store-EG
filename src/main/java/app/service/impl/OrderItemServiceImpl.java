package app.service.impl;

import app.model.entity.Order;
import app.model.entity.OrderItem;
import app.repository.OrderItemRepo;
import app.repository.OrderRepo;
import app.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import java.util.List;
import java.util.UUID;

import static app.util.LogMessages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepo orderItemRepo;
    private final OrderRepo orderRepo;

    @Override
    public int deleteAllItemsByAlbumId(UUID albumId) {

        List<OrderItem> items = orderItemRepo.findByAlbumId(albumId);

        if (!items.isEmpty()) {
            orderItemRepo.deleteAll(items);
            log.info(AnsiOutput.toString(
                    AnsiColor.BRIGHT_GREEN, ORDER_ITEMS_DELETED), items.size(), albumId);
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
            log.info(AnsiOutput.toString(
                    AnsiColor.BRIGHT_GREEN, ORDERS_EMPTY_DELETED), emptyOrders.size());
        }

        return emptyOrders.size();
    }
}
