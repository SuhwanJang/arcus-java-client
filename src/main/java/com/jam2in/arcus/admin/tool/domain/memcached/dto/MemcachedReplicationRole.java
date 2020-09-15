package com.jam2in.arcus.admin.tool.domain.memcached.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

public enum MemcachedReplicationRole {

  @JsonProperty("master")
  MASTER,
  @JsonProperty("slave")
  SLAVE
  ;

  public static MemcachedReplicationRole of(String role) {
    if (StringUtils.equals(role, "M")) {
      return MASTER;
    } else if (StringUtils.equals(role, "S")) {
      return SLAVE;
    }
    throw new IllegalArgumentException();
  }

}
