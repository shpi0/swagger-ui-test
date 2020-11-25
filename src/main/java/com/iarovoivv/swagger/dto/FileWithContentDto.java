package com.iarovoivv.swagger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class FileWithContentDto extends FileMetaDataDto {

    @Schema(
            description = "Base64 encoded file",
            example = "VEhJUyBJUyBBIFRFU1QgRklMRQ0KDQpURVNUIFRFU1Q=",
            required = true)
    String data;
}
