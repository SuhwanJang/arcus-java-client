package com.jam2in.arcus.admin.tool.domain.cluster.dto;

import org.apache.commons.lang3.StringUtils;

public enum ReplicationRole {

  MASTER,
  SLAVE
  ;

  public static ReplicationRole of(String role) {
    if (StringUtils.equals(role, "M")) {
      return MASTER;
    } else if (StringUtils.equals(role, "S")) {
      return SLAVE;
    }
    throw new IllegalArgumentException();
  }

}
