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
                entity.getServerSignature(),
                entity.getCreatedAt()
        );
    }

    public UserPublicKeyEntity toEntity(UserPublicKey domain) {
        UserPublicKeyEntity entity = new UserPublicKeyEntity();
        entity.setId(domain.id());
        entity.setUserId(domain.userId());
        entity.setPublicKey(domain.publicKey());
        entity.setServerSignature(domain.serverSignature());
        entity.setCreatedAt(domain.createdAt());
        return entity;
    }
}

