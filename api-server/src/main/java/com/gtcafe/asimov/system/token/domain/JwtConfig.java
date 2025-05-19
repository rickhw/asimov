package com.gtcafe.asimov.system.token.domain;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class JwtConfig {

    // private static String PRIVATE_KEY_PATH = "src/main/resources/private_key.pem";
    // private static String PUBLIC_KEY_PATH = "src/main/resources/public_key.pem";
    private static String PRIVATE_KEY_PATH = "private_key.pem";
    private static String PUBLIC_KEY_PATH = "public_key.pem";

    @Bean
    public KeyPair keyPair() throws Exception {
        // 讀取 private_key.pem 和 public_key.pem
        String privateKeyPem = readKeyFromClasspath(PRIVATE_KEY_PATH)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        String publicKeyPem = readKeyFromClasspath(PUBLIC_KEY_PATH)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        // 解析 PEM 格式
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPem);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPem);

        // 建立 KeyPair
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        return new KeyPair(publicKey, privateKey);
    }

     private String readKeyFromClasspath(String path) throws Exception {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream inputStream = resource.getInputStream();
             Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            return scanner.useDelimiter("\\A").next();
        }
    }
}
