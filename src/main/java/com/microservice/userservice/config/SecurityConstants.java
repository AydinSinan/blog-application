package com.microservice.userservice.config;

public class SecurityConstants {

    private SecurityConstants() throws IllegalAccessException {
        throw new IllegalAccessException("Illegal access");
    }

    public static final String JWT_KEY ="zo0pEhFvdBaujVGDQlOo7wa4EefcYtUboYGOMhjXPuA=";
    public static final String HASHING_KEY = "b8F9k5pR3t2h6A7s";
    public static final String AUTH_HEADER = "Authorization";
    public static final String JWT_ISSUER = "myApp"; // Optional: Define an issuer for your JWTs

}