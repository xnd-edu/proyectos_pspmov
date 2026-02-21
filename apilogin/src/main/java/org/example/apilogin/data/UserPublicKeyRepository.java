package org.example.apilogin.data;

import org.example.apilogin.data.entities.UserPublicKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPublicKeyRepository extends JpaRepository<UserPublicKeyEntity, Long> {
    Optional<UserPublicKeyEntity> findByUserId(Long userId);
}
