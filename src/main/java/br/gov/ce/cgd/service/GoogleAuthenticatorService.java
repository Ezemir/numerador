package br.gov.ce.cgd.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class GoogleAuthenticatorService {

    private final GoogleAuthenticator googleAuthenticator;

    public GoogleAuthenticatorService() {
        this.googleAuthenticator = new GoogleAuthenticator();
    }

    public GoogleAuthenticatorKey generateSecretKey() {
        return googleAuthenticator.createCredentials();
    }

    public String getQRCodeUrl(String issuer, String accountName, GoogleAuthenticatorKey key) {
        String encodedIssuer = encodeValue(issuer);
        String encodedAccountName = encodeValue(accountName);

        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(encodedIssuer, encodedAccountName, key);
    }

    public byte[] generateQRCode(String qrCodeUrl) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    public boolean validateCode(String secretKey, int codigoValidador) {
        return googleAuthenticator.authorize(secretKey, codigoValidador);
    }

    private String encodeValue(String value) {
        return value.replace(" ", "%20")
                   .replace("@", "%40")
                   .replace(":", "%3A");
    }
}