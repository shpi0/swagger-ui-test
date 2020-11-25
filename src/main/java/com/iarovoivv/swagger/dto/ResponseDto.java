package com.iarovoivv.swagger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseDto {

    @Schema(
            description = "Response data",
            example = "Some response information",
            required = true)
    String response;

    @Schema(
            description = "Response extra data",
            example = "Some extra information",
            required = true)
    String extra;
}
