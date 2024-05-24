package com.kltn.gateway.jwt;

import javax.naming.AuthenticationException;

public final class JwtTokenMalformedException extends AuthenticationException {
   public JwtTokenMalformedException(String message) {
      super(message);
   }
}