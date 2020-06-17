package com.jam2in.arcus.admin.tool.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModelMapperUtils {

  private static final ModelMapper modelMapper = initializeModelMapper();

  public static <D> D map(Object object, Class<D> destinationType) {
    return modelMapper.map(object, destinationType);
  }

  public static <D> D map(Object object, Class<D> destinationType, String typeMapName) {
    return modelMapper.map(object, destinationType, typeMapName);
  }

  public static <S, D> TypeMap<S, D> createTypeMap(Class<S> sourceType, Class<D> destinationType,
                                                   String typeMapName) {
    return modelMapper.createTypeMap(sourceType, destinationType, typeMapName);
  }

  public static ModelMapper initializeModelMapper() {
    ModelMapper modelMapper = new ModelMapper();

    Configuration configuration = modelMapper.getConfiguration();
    configuration.setMatchingStrategy(MatchingStrategies.STRICT);
    configuration.setFieldMatchingEnabled(true);
    configuration.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

    return modelMapper;
  }

}
