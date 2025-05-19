package com.project.projectLoginInExtensionsApi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.projectLoginInExtensionsApi.enums.StatusExtension;
import com.project.projectLoginInExtensionsApi.model.Extension;
import com.project.projectLoginInExtensionsApi.model.ExtensionRange;
import com.project.projectLoginInExtensionsApi.repository.ExtensionRangeRepository;
import com.project.projectLoginInExtensionsApi.repository.ExtensionRepository;

@Service
public class ExtensionService {
    private final ExtensionRepository extensionRepository;
    private final ExtensionRangeRepository extensionRangeRepository;

    public ExtensionService(ExtensionRepository extensionRepository,
            ExtensionRangeRepository extensionRangeRepository) {
        this.extensionRepository = extensionRepository;
        this.extensionRangeRepository = extensionRangeRepository;
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

        ExtensionRange newRange = new ExtensionRange();
        newRange.setStart(startRange);
        newRange.setEnd(endRange);

        extensionRangeRepository.save(newRange);
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

    public boolean isExtensionInRange(int extensionNumber, int startRange, int endRange) {
        if (extensionNumber >= startRange && extensionNumber <= endRange) {
            return true;
        }
        return false;
    }
}
