package org.ost.investigate.springboot.demo.service.components;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
public class HardcodedKeyRetriever implements KeyRetriever {

    private static final String KEY = "-----BEGIN PRIVATE KEY-----" +
            "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDJ7LKt6DifRZNS" +
            "xDFQLLodwJf+U+8TrIOWiKw0S5M9J4BjXR2mxRU+ez8D67xAMJChLU9UumfYGpKJ" +
            "ka7zkn2JaF7bHK1nkIBYAY7RR7+UZ85Rjak4m5B1ZLsJiVlO1nK9lM0u4yP8DIcB" +
            "kcjoxCnAC6IL/KnE58eYXfZ7VioaQ6F8+rtSyplWwhZjolLm8ID0jNLyRG7ztL+B" +
            "jKyXxYmLbnSoB2ZIwNy/D9Q1/2qJogrcOlgHB43mr+VUuJuPKMcm4A47YX/3HPx3" +
            "JBF7FWVDengnQlaJRvWjAmsQ5BD1HNvYdYwYU3cN+ZhdzYWPSOL1POup9hEh7fpb" +
            "AjC11eRfAgMBAAECggEAWug+dT8EJuiK8As17FOGoxhvFWgyTVgwkdgMNysDKdgb" +
            "3CAy1j8KVmvkLd62jOuQYY/llE6rq8YizN6StkPwUqlzcxARHXwX3AcvhhbYMHcf" +
            "kuXFFqAqxD9KWXvFglHXrHROf9pw++uj6Ff67JhL95MFLIJe6DA1UuqSqmpbYCDf" +
            "XJxUHzNHDQRqCwDTncMNzi6aNI46ALtLNiN6tdTPiVJTKlOsYq/YHLPB/mAtSZ4v" +
            "3c4VDEloDS3WZztExx83lGBQpNjsXHZIERm3vft6zWfamZEJhIqE5gPc0RGmgHeq" +
            "TlCjySSTGdP8udToehCyE760EDjX8WOvb2ffrgNUiQKBgQDz55mVqb27QP+hCBD6" +
            "edfQhb7OQ6a+uYGOo56rpmuJfX9sGQRn6l2U0L9vh9NAXlwLHFg3DWykQ3k//Yur" +
            "flo1tMk5HTljwka+WFy9HqpYWQCtHe/dZyL+auXfSUH0fdshSlJe8sw9iAUpjQFZ" +
            "zbyANsvS6+nvyySAH1Q3M/kjZQKBgQDT8CfZkBoUY30Ap7stmAfVorZzFtEimezX" +
            "pIvJfRT+K6Sr8KiLLSoAIM/3aEyZstBDcZHYslUbeGMiRdWFoLB0YgeoOWVdS0t1" +
            "IDq+4ovSzT9kPgj3FcR6ICG6Mx0WPmgq5g6K8zgBlRgzhaLgdi7O+H+Exikkm6fU" +
            "4CyxsWImcwKBgDYF8mrH8hJrzzMnyjrwifDR7vVY3rIX/K5PZ3G1ptQJTBPxZpxt" +
            "uNSheAWIHC6Bt/luCcwH2Sx5FX4q9qjBj9GdfYR9CJ0kBu9aOxBYnnp7HD88BkkP" +
            "8xxPKSNP4p/40HSLNbt6I+rtGdeeN9JgU4RCntrVDjfi0u7eAZVImcJRAoGAP5tj" +
            "ClvCR/435PtyJNPs/gQQh+gAKkdSGsIqpfUhWMsWZ/gZTXPnmBRA9jraSoKtGKC8" +
            "KfITsuPltMKKu8knTDajn6xiLKbNZcCfgOyTFFg/P0GTBT0GM7TzCmQOxb9DGiTj" +
            "lnY6h9cMZWIlKQKvN7TC6MFkVbVVoIELSOU41NsCgYAcn1Au1SPXuazdrDxOyf4/" +
            "LYbIdgHkE58cJpLgh5jcEOX62ov8kVxEOqh8P2NPUPzikXkjfPDQDoBleI22xHdE" +
            "2HlchRDfcq0N9Mv7pGmTFXKZYb0Zr3FrC9g/S6HDm8Y6ccrjjDf+BJ+oLQNzzJZ2" +
            "/e1jKlqVFn7KZKZUTddcVQ==" +
            "-----END PRIVATE KEY-----";

    @Override
    public Key getKey() {
        String privateKey = KEY.replace("-----BEGIN PRIVATE KEY-----", "");
        privateKey = privateKey.replace("-----END PRIVATE KEY-----", "");
        privateKey = privateKey.replaceAll("\\s+", "");

        byte[] decode = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decode);
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Exception during key generation");
        }
    }
}
