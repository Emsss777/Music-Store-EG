package app.model.dto;

import app.model.enums.PrimaryGenre;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDTO {

    private UUID id;
    private String artistName;
    private String stageName;
    private String firstName;
    private String lastName;
    private String artistBio;
    private PrimaryGenre primaryGenre;
    private LocalDateTime createdOn;
}
