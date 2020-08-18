package com.jam2in.arcus.admin.tool.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public final class ApiError {

  private int status;
  private String code;
  private String message;
  private List<Detail> details;

  private ApiError(int status,
                  String code,
                  String message,
                  List<Detail> details) {
    this.status = status;
    this.code = code;
    this.message = message;
    this.details = details;
  }

  private ApiError(ApiErrorCode apiErrorCode, List<Detail> details) {
    this(apiErrorCode.status().value(), apiErrorCode.code(), apiErrorCode.message(), details);
  }

  private ApiError(ApiErrorCode apiErrorCode, Detail detail) {
    this(apiErrorCode.status().value(), apiErrorCode.code(), apiErrorCode.message(), List.of(detail));
  }

  public static ApiError of(ApiErrorCode apiErrorCode) {
    return new ApiError(apiErrorCode.status().value(), apiErrorCode.code(), apiErrorCode.message(),
        Collections.emptyList());
  }

  public static ApiError of(ApiErrorCode apiErrorCode, List<Detail> details) {
    return new ApiError(apiErrorCode, details);
  }

  public static ApiError of(ApiErrorCode apiErrorCode, Detail detail) {
    return new ApiError(apiErrorCode, detail);
  }

  public static ApiError of(ApiErrorCode apiErrorCode, InvalidFormatException exception) {
    List<JsonMappingException.Reference> references = exception.getPath();

    if (CollectionUtils.size(references) > 0) {
      return new ApiError(apiErrorCode,
          List.of(
              Detail.of(
                  references.get(0).getFieldName(),
                  Objects.toString(exception.getValue(), StringUtils.EMPTY),
                  String.format(
                      ApiErrorCode.COMMON_INVALID_TYPE.message(),
                      exception.getTargetType().getSimpleName()))));
    } else {
      log.warn("references is empty");
    }

    return of(apiErrorCode);
  }

  public static ApiError of(ApiErrorCode apiErrorCode, BindingResult bindingResult) {
    List<org.springframework.validation.FieldError> fieldErrors =
        bindingResult.getFieldErrors();

    if (CollectionUtils.isEmpty(fieldErrors)) {
      return of(apiErrorCode);
    }

    return new ApiError(apiErrorCode, (List<Detail>) fieldErrors
        .stream()
        .collect(
            ArrayList<Detail>::new,
            (details, fieldError) -> {
              Object rejectedValue = fieldError.getRejectedValue();
              details.add(
                  Detail.of(
                      fieldError.getField(),
                      Objects.toString(rejectedValue, StringUtils.EMPTY),
                      fieldError.getDefaultMessage()));
            },
            List::addAll));
  }

  public static ApiError of(ApiErrorCode apiErrorCode, ConstraintViolationException e) {
    return new ApiError(apiErrorCode, (List<Detail>) e.getConstraintViolations()
        .stream()
        .collect(
            ArrayList<Detail>::new,
            (details, violation) ->
              details.add(Detail.of(
                  com.jam2in.arcus.admin.tool.util.StringUtils
                      .substringAfterLast(violation.getPropertyPath().toString(), "."),
                  violation.getInvalidValue() != null
                      ? violation.getInvalidValue().toString() : StringUtils.EMPTY,
                  violation.getMessage()
              )),
            List::addAll));
  }

  public static ApiError of(ApiErrorCode apiErrorCode,
                            MethodArgumentTypeMismatchException exception) {
    Optional<Class<?>> optionalClass = Optional.ofNullable(exception.getRequiredType());

    return new ApiError(apiErrorCode, Detail.of(
        exception.getName(),
        Objects.toString(exception.getValue(), StringUtils.EMPTY),
        optionalClass.map(c ->
            String.format(ApiErrorCode.COMMON_INVALID_TYPE.message(), c.getSimpleName()))
            .orElse(StringUtils.EMPTY)));
  }

  public static void response(HttpServletResponse response,
                              ApiError apiError,
                              ObjectMapper mapper)
      throws IOException {
    response.setStatus(apiError.getStatus());
    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(mapper.writeValueAsString(apiError));
    response.getWriter().flush();
    response.getWriter().close();
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class Detail {

    private String name;
    private String value;
    private String reason;

    private Detail(String name, String value, String reason) {
      this.name = name;
      this.value = value;
      this.reason = reason;
    }

    public static Detail of(String value) {
      return of(StringUtils.EMPTY, value, StringUtils.EMPTY);
    }

    public static Detail of(String name, String value) {
      return of(name, value, StringUtils.EMPTY);
    }

    public static Detail of(String name, String value, String reason) {
      return new Detail(name, value, reason);
    }

  }

}
