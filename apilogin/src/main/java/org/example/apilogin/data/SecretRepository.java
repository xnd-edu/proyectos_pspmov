package org.example.apilogin.data;

import org.example.apilogin.data.entities.SecretEntity;
import org.example.apilogin.data.entities.SharedSecretEntity;
import org.example.apilogin.data.entities.UserPublicKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecretRepository extends JpaRepository<SecretEntity, Long> {
    Optional<SecretEntity> findByUserId(Long userId);
}
