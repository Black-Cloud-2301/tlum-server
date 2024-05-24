package com.kltn.gateway.jwt;

import javax.naming.AuthenticationException;

public final class JwtTokenMissingException extends AuthenticationException {
   public JwtTokenMissingException(String message) {
      super(message);
   }
}