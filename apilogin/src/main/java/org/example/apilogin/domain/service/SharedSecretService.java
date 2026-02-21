package org.example.apilogin.domain.service;

import org.example.apilogin.data.SharedSecretRepository;
import org.example.apilogin.domain.errores.ResourceNotFoundException;
import org.example.apilogin.domain.mapper.SharedSecretMapper;
import org.example.apilogin.domain.model.SharedSecret;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SharedSecretService {
    private final SharedSecretRepository repository;
    private final SharedSecretMapper mapper;

    public SharedSecretService(SharedSecretRepository repository, SharedSecretMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public SharedSecret findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("SharedSecret not found"));
    }

    public SharedSecret save(SharedSecret sharedSecret) {
        return mapper.toDomain(repository.save(mapper.toEntity(sharedSecret)));
    }

    public SharedSecret update(Long id, SharedSecret sharedSecret) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SharedSecret not found"));
        return mapper.toDomain(repository.save(mapper.toEntity(sharedSecret)));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("SharedSecret not found");
        }
        repository.deleteById(id);
    }

    public List<SharedSecret> findByOwnerId(Long ownerId) {
        return repository.findByOwnerId(ownerId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    public List<SharedSecret> findBySharedWithId(Long sharedWithId) {
        return repository.findBySharedWithId(sharedWithId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    public SharedSecret findByOwnerIdAndRecipientId(Long ownerId, Long recipientId) {
        return repository.findByOwnerIdAndSharedWithId(ownerId, recipientId)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("SharedSecret not found"));
    }
}
