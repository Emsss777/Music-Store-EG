package app.model.dto;

import app.model.enums.PrimaryGenre;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDTO {

    private UUID id;
    private String title;
    private PrimaryGenre genre;
    private int year;
    private String description;
    private String coverUrl;
    private BigDecimal price;
    private ArtistDTO artist;
    private LocalDateTime createdOn;
}
