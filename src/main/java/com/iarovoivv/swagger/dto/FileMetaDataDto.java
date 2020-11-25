package com.iarovoivv.swagger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileMetaDataDto {

    @Schema(
            description = "File name",
            example = "Document",
            required = true)
    String name;

    @Schema(
            description = "File description",
            example = "Extra important document",
            required = true)
    String description;

    @Schema(
            description = "File category id",
            example = "76fa4a8f-4253-4386-93a4-ebd6b3b16b68",
            required = true)
    UUID category;
}
