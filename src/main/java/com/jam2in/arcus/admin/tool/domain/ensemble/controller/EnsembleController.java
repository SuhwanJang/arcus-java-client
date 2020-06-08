package com.jam2in.arcus.admin.tool.domain.ensemble.controller;

import com.jam2in.arcus.admin.tool.domain.ensemble.dto.EnsembleDto;
import com.jam2in.arcus.admin.tool.domain.ensemble.service.EnsembleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ensembles")
public class EnsembleController {

  private final EnsembleService ensembleService;

  public EnsembleController(EnsembleService ensembleService) {
    this.ensembleService = ensembleService;
  }

  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public EnsembleDto create(@RequestBody @Valid EnsembleDto ensembleDto) {
    return ensembleService.create(ensembleDto);
  }

  @PutMapping("/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public EnsembleDto update(@PathVariable long id,
                            @RequestBody @Valid EnsembleDto ensembleDto) {
    return ensembleService.update(id, ensembleDto);
  }

  @GetMapping("/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public EnsembleDto get(@PathVariable long id) {
    return ensembleService.get(id);
  }

  @GetMapping("/{id}/servers")
  @ResponseStatus(code = HttpStatus.OK)
  public void getServers(@PathVariable long id) {
    // TODO: response list of zookeeper server info
  }

  @GetMapping
  @ResponseStatus(code = HttpStatus.OK)
  public List<EnsembleDto> getAll() {
    return ensembleService.getAll();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public void delete(@PathVariable long id) {
    ensembleService.delete(id);
  }

}
