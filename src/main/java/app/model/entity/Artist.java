package app.model.entity;

import app.model.enums.PrimaryGenre;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Table(name = "artists")
public class Artist extends BaseEntity {

    @Column(name = "artist_name", nullable = false)
    private String artistName;

    @Column(name = "stage_name")
    private String stageName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "artist_bio", columnDefinition = "TEXT")
    private String artistBio;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_genre", nullable = false)
    private PrimaryGenre primaryGenre;

    @OneToMany(mappedBy = "artist", fetch = FetchType.EAGER)
    private List<Album> albums = new ArrayList<>();
}
