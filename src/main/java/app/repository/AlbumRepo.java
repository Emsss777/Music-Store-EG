package app.repository;

import app.model.entity.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlbumRepo extends JpaRepository<AlbumEntity, UUID> {

    Optional<AlbumEntity> findByTitle(String albumTitle);
}
