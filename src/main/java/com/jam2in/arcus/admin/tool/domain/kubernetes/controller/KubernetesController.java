package com.jam2in.arcus.admin.tool.domain.kubernetes.controller;

import com.jam2in.arcus.admin.tool.domain.kubernetes.dto.KubernetesDto;
import com.jam2in.arcus.admin.tool.domain.kubernetes.service.KubernetesService;
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
@RequestMapping("/api/v1/kubernetes")
public class KubernetesController {

  private final KubernetesService kubernetesService;

  public KubernetesController(KubernetesService kubernetesService) {
    this.kubernetesService = kubernetesService;
  }

  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public KubernetesDto create(@RequestBody @Valid KubernetesDto kubernetesDto) {
    return kubernetesService.create(kubernetesDto);
  }

  @PutMapping("/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public KubernetesDto update(@PathVariable long id,
                              @RequestBody @Valid KubernetesDto kubernetesDto) {
    return kubernetesService.update(id, kubernetesDto);
  }

  @GetMapping("/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public KubernetesDto get(@PathVariable long id) {
    return kubernetesService.get(id);
  }

  @GetMapping
  @ResponseStatus(code = HttpStatus.OK)
  public List<KubernetesDto> getAll() {
    return kubernetesService.getAll();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public void delete(@PathVariable long id) {
    kubernetesService.delete(id);
  }

}
