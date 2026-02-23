package org.example.apilogin.domain.service;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.data.UserPublicKeyRepository;
import org.example.apilogin.data.entities.UserPublicKeyEntity;
import org.example.apilogin.domain.errores.ResourceNotFoundException;
import org.example.apilogin.domain.mapper.UserPublicKeyMapper;
import org.example.apilogin.domain.model.UserPublicKey;
import org.example.apilogin.ui.security.crypto.AsymmetricEncryptionService;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
public class UserPublicKeyService {
    private final UserPublicKeyRepository repository;
    private final UserPublicKeyMapper mapper;
    private final AsymmetricEncryptionService asymmetricEncryptionService;

    public UserPublicKeyService(UserPublicKeyRepository repository, UserPublicKeyMapper mapper, AsymmetricEncryptionService asymmetricEncryptionService) {
        this.repository = repository;
        this.mapper = mapper;
        this.asymmetricEncryptionService = asymmetricEncryptionService;
    }

    public UserPublicKey findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.MSG_CLAVE_PUBLICA_NO_ENCONTRADA));
    }

    public UserPublicKey save(UserPublicKey userPublicKey) {
        try {
            UserPublicKeyEntity entity = mapper.toEntity(userPublicKey);

            // Truncar a microsegundos porque H2 trunca los nanosegundos al persistir.
            // Si firmamos con nanosegundos pero BD guarda microsegundos, el mensaje
            // a verificar nunca coincidirá con el firmado.
            LocalDateTime now = LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MICROS);
            entity.setCreatedAt(now);

            String messageToSign = String.format(Constantes.SERVER_SIGNATURE_MESSAGE,
                    entity.getUserId(),
                    Base64.getEncoder().encodeToString(entity.getPublicKey()),
                    now);

            // Usar la clave privada del servidor
            PrivateKey serverPrivateKey = getServerPrivateKey();
            byte[] signature = asymmetricEncryptionService.sign(messageToSign, serverPrivateKey);
            entity.setServerSignature(signature);

            return mapper.toDomain(repository.save(entity));

        } catch (Exception e) {
            throw new SecurityException(Constantes.MSG_CERTIFICAR_CLAVE_PUBLICA_ERROR, e);
        }
    }

    public UserPublicKey update(Long id, UserPublicKey userPublicKey) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.MSG_CLAVE_PUBLICA_NO_ENCONTRADA));
        return mapper.toDomain(repository.save(mapper.toEntity(userPublicKey)));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(Constantes.MSG_CLAVE_PUBLICA_NO_ENCONTRADA);
        }
        repository.deleteById(id);
    }

    public UserPublicKey findByUserId(Long userId) {
        return repository.findByUserId(userId)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.MSG_CLAVE_PUBLICA_NO_ENCONTRADA));
    }

    public List<UserPublicKey> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    private PrivateKey getServerPrivateKey() {
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(Constantes.PRIVATE_KEY_FILE)) {
            if (is == null) {
                throw new SecurityException(Constantes.PRIVATE_KEY_FILE_NOT_FOUND);
            }
            return asymmetricEncryptionService.binaryToPrivateKey(is.readAllBytes());
        } catch (Exception e) {
            throw new SecurityException(Constantes.ERROR_LEER_CLAVE_PRIVADA, e);
        }
    }
}
