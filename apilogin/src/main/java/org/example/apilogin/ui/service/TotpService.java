package org.example.apilogin.ui.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class TotpService {

    public String generateSecret() {
        return Base32.random();
    }

    public String generateQrCodeImageUri(String secret, String username, String issuer) throws WriterException, IOException {
        String qrCodeText = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s",
                issuer, username, secret, issuer);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 250, 250);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] imageData = pngOutputStream.toByteArray();

        String base64Image = Base64.getEncoder().encodeToString(imageData);
        return "data:image/png;base64," + base64Image;
    }

    public boolean verifyCode(String secret, String code) {
        try {
            Totp totp = new Totp(secret);
            return totp.verify(code);
        } catch (Exception _) {
            return false;
        }
    }
}