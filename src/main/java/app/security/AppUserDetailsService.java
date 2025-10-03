package app.security;

import app.model.entity.UserEntity;
import app.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public AppUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User [{}] does NOT Exist!", username); // лог в терминала
                    return new UsernameNotFoundException("Bad Credentials!");
                });

        return AuthenticationMetadata.builder()
                .username(user.getUsername())
                .userId(user.getId())
                .password(user.getPassword())
                .role(user.getRole())
                .isActive(user.isActive())
                .build();
    }
}
