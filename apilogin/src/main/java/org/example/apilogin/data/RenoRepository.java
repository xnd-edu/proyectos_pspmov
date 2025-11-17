package org.example.apilogin.data;

import org.example.apilogin.data.entities.RenoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RenoRepository extends JpaRepository<RenoEntity, Integer> {
    List<RenoEntity> findByUserId(Integer userId);
    List<RenoEntity> findByNombreContaining(String nombre);
}

