package org.example.apilogin.domain.mapper;

import org.example.apilogin.data.entities.UserPublicKeyEntity;
import org.example.apilogin.domain.model.UserPublicKey;
import org.springframework.stereotype.Component;

@Component
public class UserPublicKeyMapper {
    public UserPublicKey toDomain(UserPublicKeyEntity entity) {
        return new UserPublicKey(
                entity.getId(),
                entity.getUserId(),
                entity.getPublicKey(),
                entity.getKeySize(),
                entity.getServerSignature(),
                entity.getSignedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public UserPublicKeyEntity toEntity(UserPublicKey domain) {
        UserPublicKeyEntity entity = new UserPublicKeyEntity();
        entity.setId(domain.id());
        entity.setUserId(domain.userId());
        entity.setPublicKey(domain.publicKey());
        entity.setKeySize(domain.keySize());
        entity.setServerSignature(domain.serverSignature());
        entity.setSignedAt(domain.signedAt());
        entity.setCreatedAt(domain.createdAt());
        entity.setUpdatedAt(domain.updatedAt());
        return entity;
    }
}

