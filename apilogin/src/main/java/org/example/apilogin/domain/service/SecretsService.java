package org.example.apilogin.domain.service;

import org.example.apilogin.data.SecretRepository;
import org.example.apilogin.domain.errores.ResourceNotFoundException;
import org.example.apilogin.domain.mapper.SecretMapper;
import org.example.apilogin.domain.model.Secret;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecretsService {
    private final SecretRepository repository;
    private final SecretMapper mapper;

    public SecretsService(SecretRepository repository, SecretMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Secret findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Secret not found"));
    }

    public Secret save(Secret secret) {
        return mapper.toDomain(repository.save(mapper.toEntity(secret)));
    }

    public Secret update(Long id, Secret secret) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Secret not found"));

        return mapper.toDomain(repository.save(mapper.toEntity(secret)));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Secret not found");
        }
        repository.deleteById(id);
    }

    public List<Secret> findByUserId(Long userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
