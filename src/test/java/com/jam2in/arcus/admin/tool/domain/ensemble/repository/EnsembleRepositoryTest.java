package com.jam2in.arcus.admin.tool.domain.ensemble.repository;

import com.jam2in.arcus.admin.tool.domain.ensemble.entity.EnsembleEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EnsembleRepositoryTest {

  /*
  @Autowired
  private EnsembleRepository ensembleRepository;

  private EnsembleEntity.EnsembleEntityBuilder ensembleEntityBuilder;

  @Before
  public void before() {
    ensembleEntityBuilder =
        EnsembleEntity.builder()
            .name("foo")
            .zookeepers(List.of(
                "192.168.0.1:2181",
                "192.168.0.2:2181",
                "192.168.0.3:2181"
            ));
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void save_nameEmpty() {
    // given
    EnsembleEntity ensembleEntity = EnsembleEntity.builder()
        .name(null)
        .build();

    // when
    ensembleRepository.save(ensembleEntity);
  }

  @Test
  public void existsByName() {
    // given
    EnsembleEntity ensembleEntity = ensembleEntityBuilder.build();
    ensembleRepository.save(ensembleEntity);

    // when
    boolean exists = ensembleRepository.existsByName(ensembleEntity.getName());

    // then
    assertThat(exists, is(true));
  }

  @Test
  public void existsByName_noName() {
    // given
    EnsembleEntity ensembleEntity = ensembleEntityBuilder.build();

    // when
    boolean exists = ensembleRepository.existsByName(ensembleEntity.getName());

    // then
    assertThat(exists, is(false));
  }
   */

}
