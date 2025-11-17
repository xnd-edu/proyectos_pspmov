package org.example.apilogin.domain.service;

import org.example.apilogin.data.RenoRepository;
import org.example.apilogin.data.entities.RenoEntity;
import org.example.apilogin.domain.mapper.RenoMapper;
import org.example.apilogin.domain.model.Reno;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RenoService {

    private final RenoRepository renoRepository;
    private final RenoMapper renoMapper;

    public RenoService(RenoRepository renoRepository, RenoMapper renoMapper) {
        this.renoRepository = renoRepository;
        this.renoMapper = renoMapper;
    }

    public List<Reno> findAll() {
        return renoRepository.findAll()
                .stream()
                .map(renoMapper::toDomain)
                .toList();
    }

    public Reno findById(int id) {
        return renoRepository.findById(id)
                .map(renoMapper::toDomain)
                .orElse(null);
    }

    public Reno save(Reno reno) {
        RenoEntity saved = renoRepository.save(renoMapper.toEntity(reno));
        return renoMapper.toDomain(saved);
    }

    public Reno update(int id, Reno reno) {
        return renoRepository.findById(id)
                .map(existing -> {
                    RenoEntity updated = renoMapper.toEntity(reno);
                    updated.setId(existing.getId());
                    return renoMapper.toDomain(renoRepository.save(updated));
                })
                .orElse(null);
    }

    public boolean delete(int id) {
        if (renoRepository.existsById(id)) {
            renoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Reno> findByUserId(int userId) {
        return renoRepository.findByUserId(userId)
                .stream()
                .map(renoMapper::toDomain)
                .toList();
    }

    public List<Reno> findNameLike(String nombre) {
        return renoRepository.findByNombreContaining(nombre)
                .stream()
                .map(renoMapper::toDomain)
                .toList();
    }
}
