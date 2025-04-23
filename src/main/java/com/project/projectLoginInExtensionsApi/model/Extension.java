package com.project.projectLoginInExtensionsApi.model;

import com.project.projectLoginInExtensionsApi.enums.StatusExtension;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Extension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Integer extensionNumber;

    @OneToOne
    @JoinColumn(name = "logged_user_id", nullable = true)
    private User loggedUser;

    @Enumerated(EnumType.STRING)
    private StatusExtension status;
}
