package app.repository;

import app.model.entity.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistRepo extends JpaRepository<ArtistEntity, UUID> {

    Optional<ArtistEntity> findByArtistName(String artistName);
}
