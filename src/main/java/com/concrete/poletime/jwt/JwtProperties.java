package com.concrete.poletime.jwt;

import org.springframework.beans.factory.annotation.Value;

public class JwtProperties {
    public static final String SECRET_KEY = "Secret_Key";
    public static final int EXPIRATION = 840_000_000;
}
