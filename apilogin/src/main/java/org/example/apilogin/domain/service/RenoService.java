package org.example.apilogin.domain.service;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.data.RenoRepository;
import org.example.apilogin.domain.errores.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constantes.MSG_RENO_NO_ENCONTRADO, id)));
    }

    public Reno save(Reno reno) {
        return renoMapper.toDomain(renoRepository.save(renoMapper.toEntity(reno)));
    }

    public Reno update(int id, Reno reno) {
        renoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constantes.MSG_RENO_NO_ENCONTRADO, id)));

        return renoMapper.toDomain(renoRepository.save(renoMapper.toEntity(reno)));
    }

    public void delete(int id) {
        if (!renoRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(Constantes.MSG_RENO_NO_ENCONTRADO, id));
        }
        renoRepository.deleteById(id);
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
