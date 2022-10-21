package com.uet.iot.res;

public class AuthenticationRes {
    private final String jwt;

    public AuthenticationRes(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
