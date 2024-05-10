package com.fc8.aptner.common.utils;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptionAlgorithmSpec;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class KmsUtilsTest {

    final String KEY = "69ee4900-f7d9-4c99-9ac8-224f0210f16d";
    final Regions REGIONS = Regions.AP_NORTHEAST_2;
    final EncryptionAlgorithmSpec ALGORITHM = EncryptionAlgorithmSpec.SYMMETRIC_DEFAULT;

    @Test
    @DisplayName("암복호화 테스트")
    void encrypt() {

        // given
        final String plaintext = "Hello, World!";

        // when
        AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                .withRegion(REGIONS)
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();

        EncryptRequest encryptRequest = new EncryptRequest();
        encryptRequest.withKeyId(KEY);
        encryptRequest.withPlaintext(ByteBuffer.wrap(plaintext.getBytes(StandardCharsets.UTF_8)));
        encryptRequest.withEncryptionAlgorithm(ALGORITHM);

        byte[] cipherBytes = kmsClient.encrypt(encryptRequest).getCiphertextBlob().array();
        String encryptText = Base64.encodeBase64String(cipherBytes);

        DecryptRequest decryptRequest = new DecryptRequest();
        decryptRequest.withKeyId(KEY);
        decryptRequest.withCiphertextBlob(ByteBuffer.wrap(Base64.decodeBase64(encryptText)));
        decryptRequest.withEncryptionAlgorithm(ALGORITHM);

        byte[] textBytes = kmsClient.decrypt(decryptRequest).getPlaintext().array();
        String decryptText = new String(textBytes);

        System.out.println("plaint text : " + plaintext);
        System.out.println("encrypt text : " + encryptText);
        System.out.println("decrypt text : " + decryptText);

        // then
        assertEquals(plaintext, decryptText);

    }

}