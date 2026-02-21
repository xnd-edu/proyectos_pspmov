package org.example.apilogin.domain.mapper;

import org.example.apilogin.data.entities.SecretEntity;
import org.example.apilogin.domain.model.Secret;
import org.springframework.stereotype.Component;

@Component
public class SecretMapper {

    public Secret toDomain(SecretEntity e) {
        if (e == null) return null;
        return new Secret(
                e.getId(),
                e.getUserId(),
                e.getEncryptedData(),
                e.getIv(),
                e.getSalt(),
                e.getCreatedAt()
        );
    }

    public SecretEntity toEntity(Secret s) {
        if (s == null) return null;
        SecretEntity entity = new SecretEntity();
        entity.setId(s.id());
        entity.setUserId(s.userId());
        entity.setEncryptedData(s.encryptedData());
        entity.setIv(s.iv());
        entity.setSalt(s.salt());
        // createdAt se maneja automáticamente con @PrePersist
        return entity;
    }
}
