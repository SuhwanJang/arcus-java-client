package com.jam2in.arcus.admin.tool.domain.ensemble.entity;

import com.jam2in.arcus.admin.tool.domain.common.entity.DateEntity;
import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.domain.zookeeper.entity.ZooKeeperEntity;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ensembles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EnsembleEntity extends DateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  @Setter
  private String name;

  private String address;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "ensembleId")
  private List<ZooKeeperEntity> zkservers = new ArrayList<>();

  @Builder
  public EnsembleEntity(String name,
                        List<ZooKeeperEntity> zkservers,
                        String address) {
    this.name = name;
    this.zkservers = zkservers;
    this.address = address;
  }

  public void setZkservers(List<ZooKeeperEntity> zkservers) {
    this.zkservers.clear();
    if (CollectionUtils.isNotEmpty(zkservers)) {
      this.zkservers.addAll(zkservers);
    }
  }

  public static EnsembleEntity of(EnsembleDto ensembleDto) {
    return ModelMapperUtils.map(ensembleDto, EnsembleEntity.class);
  }

}
