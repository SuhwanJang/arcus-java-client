package com.jam2in.arcus.admin.tool.domain.ensemble.entity;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "ensembles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EnsembleEntity {

  @Builder
  public EnsembleEntity(String name,
                        Collection<ZooKeeperEntity> zookeepers) {
    this.name = name;
    this.zookeepers = zookeepers;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "ensemble_id")
  private Collection<ZooKeeperEntity> zookeepers;

  public void updateName(String name) {
    this.name = name;
  }

  public void updateZookeepers(Collection<ZooKeeperEntity> zookeepers) {
    this.zookeepers.clear();
    this.zookeepers.addAll(zookeepers);
  }

  public static EnsembleEntity of(EnsembleDto ensembleDto) {
    return ModelMapperUtils.map(ensembleDto, EnsembleEntity.class);
  }

}
