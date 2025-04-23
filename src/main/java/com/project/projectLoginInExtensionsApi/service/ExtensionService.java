package com.project.projectLoginInExtensionsApi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.project.projectLoginInExtensionsApi.enums.StatusExtension;
import com.project.projectLoginInExtensionsApi.model.Extension;
import com.project.projectLoginInExtensionsApi.repository.ExtensionRepository;

@Service
public class ExtensionService {
    private final ExtensionRepository extensionRepository;

    public ExtensionService(ExtensionRepository extensionRepository) {
        this.extensionRepository = extensionRepository;
    }

    public List<Extension> createRangeExtensions(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("O número inicial deve ser menor ou igual ao número final.");
        }

        List<Extension> created = new ArrayList<>();

        for (int num = start; num <= end; num++) {
            if (extensionRepository.existsByExtensionNumber(num)) {
                throw new IllegalStateException("Ramal " + num + " já cadastrado.");
            }
            Extension extension = new Extension();
            extension.setExtensionNumber(num);
            extension.setStatus(StatusExtension.DISPONIVEL);
            extensionRepository.save(extension);
            created.add(extension);
        }

        return created;
    }

    // Novo método: Configura o range válido e atualiza os status
    public void configureRange(int startRange, int endRange) {
        if (startRange > endRange) {
            throw new IllegalArgumentException("O início do range deve ser menor ou igual ao fim.");
        }

        // Verifica se há ramais OCUPADOS fora do novo range
        boolean hasOccupiedOutsideRange = extensionRepository.existsByStatusAndExtensionNumberNotBetween(
                StatusExtension.OCUPADO,
                startRange,
                endRange);

        if (hasOccupiedOutsideRange) {
            throw new IllegalStateException("Existem ramais ocupados fora do range configurado. Libere-os primeiro.");
        }

        // Atualiza os status dos ramais existentes
        List<Extension> allExtensions = extensionRepository.findAll();

        for (Extension ext : allExtensions) {
            if (ext.getStatus() == StatusExtension.OCUPADO) {
                continue; // Mantém o status se já estiver ocupado
            }

            if (ext.getExtensionNumber() >= startRange && ext.getExtensionNumber() <= endRange) {
                ext.setStatus(StatusExtension.DISPONIVEL);
            } else {
                ext.setStatus(StatusExtension.INVALIDO);
            }
        }

        extensionRepository.saveAll(allExtensions);
    }

    public void resetRangeConfiguration() {
        List<Extension> allExtensions = extensionRepository.findAll();

        for (Extension ext : allExtensions) {
            if (ext.getLoggedUser() == null) {
                ext.setStatus(StatusExtension.DISPONIVEL);
            }
        }

        extensionRepository.saveAll(allExtensions);
    }
}
