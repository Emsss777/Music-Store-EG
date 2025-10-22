package app.repository;

import app.model.entity.Album;
import app.model.enums.PrimaryGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlbumRepo extends JpaRepository<Album, UUID> {

    Optional<Album> findByTitle(String albumTitle);

    List<Album> findAllByOrderByYearDesc();

    List<Album> findByGenreOrderByYearDesc(PrimaryGenre genre);
}
