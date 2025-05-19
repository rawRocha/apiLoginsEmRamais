package com.project.projectLoginInExtensionsApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.projectLoginInExtensionsApi.model.ExtensionRange;

public interface ExtensionRangeRepository extends JpaRepository<ExtensionRange, Long> {
    ExtensionRange findTopByOrderByCreatedAtDesc();
}
