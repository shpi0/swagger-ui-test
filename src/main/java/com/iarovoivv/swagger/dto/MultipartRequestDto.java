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
public class MultipartRequestDto {

    @Schema(
            type = "string",
            format = "binary",
            description = "Binary file data",
            required = true)
    String file;

    @Schema(
            description = "File information",
            implementation = FileMetaDataDto.class,
            required = true)
    FileMetaDataDto document;
}
