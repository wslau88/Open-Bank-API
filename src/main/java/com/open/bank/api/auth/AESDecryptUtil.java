package com.open.bank.api.auth;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AESDecryptUtil {
	@Value("${login.decrypt.secret}")
	private String LOGIN_DECRYPT_SECRET;
	
	@Value("${login.decrypt.standard}")
	private String LOGIN_DECRYPT_STANDARD;
	
	@Value("${login.decrypt.standard.spec}")
	private String LOGIN_DECRYPT_STANDARD_SPEC;
	
    public String decrypt(String ciphertext) throws Exception {
    	SecretKey secretKey = getSecretKey(LOGIN_DECRYPT_SECRET);
    	Cipher cipher = Cipher.getInstance(LOGIN_DECRYPT_STANDARD_SPEC);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)));
    }

    public SecretKey getSecretKey(String secretKey) throws Exception {
        byte[] decodeSecretKey = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(decodeSecretKey, 0, decodeSecretKey.length, LOGIN_DECRYPT_STANDARD);
    }
}
