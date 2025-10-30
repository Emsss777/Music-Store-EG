package app.repository;

import app.model.entity.Order;
import app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByOwner(User owner);

    @Query("SELECT coalesce(sum(o.totalAmount), 0) FROM Order o")
    java.math.BigDecimal findTotalRevenue();

    long countByCreatedOnBetween(LocalDateTime start, LocalDateTime end);
}
