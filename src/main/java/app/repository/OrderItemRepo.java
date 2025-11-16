package app.repository;

import app.model.entity.OrderItem;
import app.model.projection.TopSellingAlbumProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, UUID> {

    List<OrderItem> findByAlbumId(UUID albumId);

    @Query("""
            SELECT
                oi.album.id AS albumId,
                oi.album.title AS title,
                concat(oi.album.artist.firstName, ' ', oi.album.artist.lastName) AS artist,
                sum(oi.quantity) AS unitsSold,
                sum(oi.unitPrice * oi.quantity) AS revenue,
                oi.album.coverUrl AS coverUrl
            FROM OrderItem oi
            GROUP BY
                oi.album.id,
                oi.album.title,
                oi.album.artist.firstName,
                oi.album.artist.lastName,
                oi.album.coverUrl
            ORDER BY unitsSold DESC
            """)
    List<TopSellingAlbumProjection> findTopSellingAlbums();
}
