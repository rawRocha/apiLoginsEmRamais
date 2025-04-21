package com.project.projectLoginInExtensionsApi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.projectLoginInExtensionsApi.model.Extension;
import com.project.projectLoginInExtensionsApi.model.User;

public interface ExtensionRepository extends JpaRepository<Extension, Long> {
    Page<Extension> findByLoggedUserIsNull(Pageable pageable);

    Optional<Extension> findByExtensionNumber(Integer extensionNumber);

    boolean existsByExtensionNumber(Integer extensionNumber);

    List<Extension> findByLoggedUser(User user);

    List<Extension> findByLoggedUserIsNotNull();

    Page<Extension> findByExtensionNumber(Integer extensionNumber, Pageable pageable);

    Page<Extension> findByLoggedUser_UsernameContainingIgnoreCase(String username, Pageable pageable);
}
