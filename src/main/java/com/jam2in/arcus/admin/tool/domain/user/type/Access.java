package com.jam2in.arcus.admin.tool.domain.user.type;

import org.springframework.security.core.GrantedAuthority;

public enum Access implements GrantedAuthority {

  ACCESS_ZOOKEEPER_CLUSTER_MANAGEMENT,
  ACCESS_CACHE_CLUSTER_MANAGEMENT,
  ACCESS_CACHE_ITEM_MANAGEMENT,
  ACCESS_CACHE_DIAGNOSIS_MANAGEMENT,
  ;

  @Override
  public String getAuthority() {
    return name();
  }

}
