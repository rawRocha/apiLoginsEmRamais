package com.project.projectLoginInExtensionsApi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

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
            extensionRepository.save(extension);
            created.add(extension);
        }

        return created;
    }
}
