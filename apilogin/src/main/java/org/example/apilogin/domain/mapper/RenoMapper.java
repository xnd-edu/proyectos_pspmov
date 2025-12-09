package org.example.apilogin.domain.mapper;

import org.example.apilogin.data.entities.RenoEntity;
import org.example.apilogin.domain.model.Reno;
import org.springframework.stereotype.Component;

@Component
public class RenoMapper {

    public Reno toDomain(RenoEntity e) {
        if (e == null) return null;
        return new Reno(
                e.getId(),
                e.getUserId(),
                e.getNombre(),
                e.getColor(),
                e.getCuernos()
        );
    }

    public RenoEntity toEntity(Reno r) {
        if (r == null) return null;
        RenoEntity entity = new RenoEntity();
        entity.setId(r.id());
        entity.setUserId(r.userId());
        entity.setNombre(r.nombre());
        entity.setColor(r.color());
        entity.setCuernos(r.cuernos());
        return entity;
    }
}
