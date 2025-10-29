package app.repository;

import app.model.entity.Album;
import app.model.entity.Order;
import app.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, UUID> {

    List<OrderItem> findByAlbumId(UUID albumId);

    Optional<OrderItem> findByOrderAndAlbum(Order order, Album album);
}
