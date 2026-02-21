package org.example.apilogin.domain.mapper;

import org.example.apilogin.data.entities.SharedSecretEntity;
import org.example.apilogin.domain.model.SharedSecret;
import org.springframework.stereotype.Component;

@Component
public class SharedSecretMapper {
    public SharedSecret toDomain(SharedSecretEntity entity) {
        return new SharedSecret(
                entity.getId(),
                entity.getSecretId(),
                entity.getOwnerId(),
                entity.getSharedWithId(),
                entity.getEncryptedData(),
                entity.getEncryptedKey(),
                entity.getIv(),
                entity.getCreatedAt()
        );
    }

    public SharedSecretEntity toEntity(SharedSecret domain) {
        SharedSecretEntity entity = new SharedSecretEntity();
        entity.setId(domain.id());
        entity.setSecretId(domain.secretId());
        entity.setOwnerId(domain.ownerId());
        entity.setSharedWithId(domain.sharedWithId());
        entity.setEncryptedData(domain.encryptedData());
        entity.setEncryptedKey(domain.encryptedKey());
        entity.setIv(domain.iv());
        entity.setCreatedAt(domain.createdAt());
        return entity;
    }
}

