package org.example.apilogin.data;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.data.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByToken(String token);

    List<TokenEntity> findByUserId(Long userId);

    @Modifying
    @Query(Constantes.QUERY_REVOKE_TOKEN)
    void revokeToken(String token);
}

