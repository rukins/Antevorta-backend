package com.antevorta.service;

import com.antevorta.exception.serverexception.EntityNotFoundException;
import com.antevorta.exception.serverexception.ServerException;
import com.antevorta.model.User;
import com.antevorta.repository.UserRepository;
import com.antevorta.security.encryptor.Encryptor;
import com.antevorta.security.service.AuthService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CurrentUserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final Encryptor encryptor;

    @Autowired
    public CurrentUserService(UserRepository userRepository, AuthService authService, Encryptor encryptor) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.encryptor = encryptor;
    }

    @SneakyThrows
    public User getAuthorizedUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> user = userRepository.findByEmail(email);

        return user.orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User updateUser(User user, String field, HttpServletRequest request) throws ServerException {
        User authorizedUser = getAuthorizedUser();

        Set<String> fields = Arrays.stream(field.split(",")).map(String::trim).collect(Collectors.toSet());

        if (fields.containsAll(List.of("firstname", "lastname"))) {
            authorizedUser.setFirstname(user.getFirstname());
            authorizedUser.setLastname(user.getLastname());

            return userRepository.save(authorizedUser);
        }

        return switch (field) {
            case "firstname" -> {
                authorizedUser.setFirstname(user.getFirstname());

                yield userRepository.save(authorizedUser);
            }
            case "lastname" -> {
                authorizedUser.setLastname(user.getLastname());

                yield userRepository.save(authorizedUser);
            }
            case "password" -> {
                authorizedUser.setPassword(encryptor.encrypt(user.getPassword()));

                authService.logout(request);

                yield userRepository.save(authorizedUser);
            }
            case "email" -> {
                authorizedUser.setEmail(user.getEmail());

                authService.logout(request);

                yield  userRepository.save(authorizedUser);
            }
            default -> throw new ServerException(HttpStatus.BAD_REQUEST.toString());
        };
    }

    public void verifyUser() {
        User authorizedUser = getAuthorizedUser();

        authorizedUser.setVerified(true);

        userRepository.save(authorizedUser);
    }

    public boolean isUserVerified() {
        return getAuthorizedUser().isVerified();
    }
}
