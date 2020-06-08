package com.jam2in.arcus.admin.tool.domain.ensemble.entity;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.util.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "ensembles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EnsembleEntity {

  @Builder
  public EnsembleEntity(String name,
                        Collection<String> zookeepers) {
    this.name = name;
    this.zookeepers = zookeepers;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @ElementCollection
  @JoinTable(
      name = "zookeepers",
      joinColumns = {
        @JoinColumn(name = "id")
      }
  )
  @Column(name = "zookeeper")
  private Collection<String> zookeepers;

  public void updateName(String name) {
    this.name = name;
  }

  public void updateZookeepers(Collection<String> zookeepers) {
    this.zookeepers = zookeepers;
  }

  public static EnsembleEntity of(EnsembleDto ensembleDto) {
    return ModelMapperUtils.map(ensembleDto, EnsembleEntity.class);
  }

}
