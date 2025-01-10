package com.dofuspulse.api.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

  ADMIN_READ("admin:read"),
  ADMIN_UPDATE("admin:update"),
  ADMIN_DELETE("admin:delete"),
  ADMIN_CREATE("admin:create"),
  USER_READ("user:read"),
  USER_UPDATE("user:update"),
  USER_DELETE("user:delete"),
  USER_CREATE("user:create");

  private final String permission;
}
