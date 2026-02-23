package org.example.apilogin.domain.service;

import org.example.apilogin.common.Constantes;
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
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.MSG_ERROR_COMPARTIDO_NO_ENCONTRADO));
    }

    public SharedSecret save(SharedSecret sharedSecret) {
        return mapper.toDomain(repository.save(mapper.toEntity(sharedSecret)));
    }

    public SharedSecret update(Long id, SharedSecret sharedSecret) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.MSG_ERROR_COMPARTIDO_NO_ENCONTRADO));
        return mapper.toDomain(repository.save(mapper.toEntity(sharedSecret)));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(Constantes.MSG_ERROR_COMPARTIDO_NO_ENCONTRADO);
        }
        repository.deleteById(id);
    }

    public List<SharedSecret> findBySecretId(Long secretId) {
        return repository.findBySecretId(secretId)
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
}
