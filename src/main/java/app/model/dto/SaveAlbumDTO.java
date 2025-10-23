package app.model.dto;

import app.model.enums.PrimaryGenre;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

import static app.util.ExceptionMessages.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveAlbumDTO {

    @NotBlank
    @Size(max = 100, message = ALBUM_TITLE_TOO_LONG)
    private String title;

    @NotNull
    private PrimaryGenre genre;

    @NotNull
    @Max(value = 2025, message = ALBUM_YEAR_TOO_HIGH)
    private Integer year;

    @Size(max = 1000, message = ALBUM_DESCRIPTION_TOO_LONG)
    private String description;

    @NotBlank
    @Pattern(regexp = "^(https?://).*", message = ALBUM_COVER_URL_INVALID)
    private String coverUrl;

    @NotNull
    private BigDecimal price;

    @NotNull
    private UUID artistId;
}
