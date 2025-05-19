package com.project.projectLoginInExtensionsApi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.projectLoginInExtensionsApi.enums.StatusExtension;
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

    Page<Extension> findByLoggedUserIsNullAndExtensionNumberBetween(Integer start, Integer end, Pageable pageable);

    Page<Extension> findByStatusIn(List<StatusExtension> statuses, Pageable pageable);

    Extension findFirstByStatusInOrderByExtensionNumberAsc(List<StatusExtension> status);

    Extension findFirstByStatusInOrderByExtensionNumberDesc(List<StatusExtension> status);

    // Novo método para verificar ramais OCUPADOS fora do range
    @Query("SELECT COUNT(e) > 0 FROM Extension e WHERE e.status = :status AND (e.extensionNumber < :start OR e.extensionNumber > :end)")
    boolean existsByStatusAndExtensionNumberNotBetween(
            @Param("status") StatusExtension status,
            @Param("start") int start,
            @Param("end") int end);

}
