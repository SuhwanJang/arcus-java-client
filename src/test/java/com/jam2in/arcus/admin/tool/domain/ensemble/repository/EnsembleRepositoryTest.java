package com.jam2in.arcus.admin.tool.domain.ensemble.repository;

import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EnsembleRepositoryTest {

  @Autowired
  private EnsembleRepository ensembleRepository;

  @Test(expected = DataIntegrityViolationException.class)
  public void save_nameEmpty() {
    // given
    EnsembleEntity ensembleEntity = EnsembleEntity.builder()
        .name(null)
        .build();

    // when
    ensembleRepository.save(ensembleEntity);
  }

}
