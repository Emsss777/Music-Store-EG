package app.model.entity;

import app.model.enums.Country;
import app.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "country", nullable = false)
    private Country country;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "updated_on", nullable = false)
    private LocalDateTime updatedOn;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @OrderBy("createdOn DESC")
    private List<OrderEntity> orders = new ArrayList<>();
}
