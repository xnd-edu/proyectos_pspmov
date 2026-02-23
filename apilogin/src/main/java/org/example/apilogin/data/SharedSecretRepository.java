package org.example.apilogin.data;

import org.example.apilogin.data.entities.SharedSecretEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SharedSecretRepository extends JpaRepository<SharedSecretEntity, Long> {
    Optional<SharedSecretEntity> findBySecretId(Long secretId);
    Optional<SharedSecretEntity> findBySharedWithId(Long sharedWithId);
    Optional<SharedSecretEntity> findByOwnerIdAndSharedWithId(Long ownerId, Long recipientId);
}
