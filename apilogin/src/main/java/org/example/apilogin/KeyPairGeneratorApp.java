package org.example.apilogin;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.ui.security.crypto.AsymmetricEncryptionService;

import java.io.FileOutputStream;
import java.security.KeyPair;

/**
 * Esta clase es una aplicación de consola que genera un par de claves (pública y privada) utilizando el servicio de cifrado asimétrico.
 * La clave privada se guarda en un archivo llamado "server_private.key" y la clave pública se guarda en "server_public.key".
 * NOTA PARA EL PROFESOR: SOLO SE EJECUTA UNA VEZ PARA GENERAR LAS CLAVES INICIALES DEL SERVIDOR. NO DEBE EJECUTARSE EN PRODUCCIÓN NI REGENERAR CLAVES EXISTENTES.
 */
public class KeyPairGeneratorApp {
    static void main() throws Exception {
        AsymmetricEncryptionService service = new AsymmetricEncryptionService();
        KeyPair keyPair = service.generateKeyPair();

        // Guardar clave privada
        try (FileOutputStream fos = new FileOutputStream(Constantes.PRIVATE_KEY_FILE)) {
            fos.write(keyPair.getPrivate().getEncoded());
        }

        // Guardar clave pública
        try (FileOutputStream fos = new FileOutputStream(Constantes.PUBLIC_KEY_FILE)) {
            fos.write(keyPair.getPublic().getEncoded());
        }

        System.out.println("Claves generadas y guardadas.");
        System.out.println("Clave pública en Base64: " + service.publicKeyToBase64(keyPair.getPublic()));
    }
}

