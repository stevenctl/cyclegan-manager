package com.slandow.cycleganmanager.security

import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component

class SecurityConstants {

    public static String SECRET

    public static long EXPIRATION_TIME

    public static String LOGIN_URL = "/login"

    public static List<String> UNSECURED_URLS = [LOGIN_URL, "/", "/index.html", "/dist/*", "/public/*", "favicon-32x32.png"]

    public static final String TOKEN_PREFIX = "Bearer "
    public static final String HEADER_STRING = "Authorization"

    @Value('${token-expiration}')
    void setExpirationTime(long expirationTime){
        EXPIRATION_TIME = expirationTime
    }

    @Value('${token-gen-secret}')
    void setSecret(String secret){
        SECRET = secret
    }
}

