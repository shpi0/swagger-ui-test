package com.iarovoivv.swagger.route;

import com.iarovoivv.swagger.controller.TestController;
import com.iarovoivv.swagger.dto.FileWithContentDto;
import com.iarovoivv.swagger.dto.MultipartRequestDto;
import com.iarovoivv.swagger.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TestRoute {

    private static final String PATH_MULTIPART = "/test/multipart/{id}";
    private static final String PATH_JSON = "/test/json/{id}";
    private static final String PATH_MIXED = "/test/mixed/{id}";

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = PATH_JSON,
                    method = RequestMethod.POST,
                    beanClass = TestController.class,
                    beanMethod = "testJson",
                    operation =
                    @Operation(
                            operationId = "testJson",
                            parameters = {@Parameter(
                                    name = "id",
                                    required = true,
                                    description = "ID of something",
                                    in = ParameterIn.PATH)},
                            requestBody = @RequestBody(
                                    description = "Create file",
                                    required = true,
                                    content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = FileWithContentDto.class))),
                            security = {@SecurityRequirement(name = "bearer-key")},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful Operation",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ResponseDto.class)))
                            })),
            @RouterOperation(
                    path = PATH_MULTIPART,
                    method = RequestMethod.POST,
                    beanClass = TestController.class,
                    beanMethod = "testMultipart",
                    operation =
                    @Operation(
                            operationId = "testMultipart",
                            parameters = {@Parameter(
                                    name = "id",
                                    required = true,
                                    description = "ID of something",
                                    in = ParameterIn.PATH)},
                            requestBody = @RequestBody(
                                    description = "Create file",
                                    required = true,
                                    content = @Content(
                                            mediaType = "multipart/form-data",
                                            encoding = {
                                                    @Encoding(name = "document", contentType = "application/json")
                                            },
                                            schema = @Schema(type = "object", implementation = MultipartRequestDto.class))),
                            security = {@SecurityRequirement(name = "bearer-key")},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful Operation",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ResponseDto.class)))
                            })),
            @RouterOperation(
                    path = PATH_MIXED,
                    method = RequestMethod.POST,
                    beanClass = TestController.class,
                    beanMethod = "testMixed",
                    operation =
                    @Operation(
                            operationId = "testMixed",
                            parameters = {@Parameter(
                                    name = "id",
                                    required = true,
                                    description = "ID of something",
                                    in = ParameterIn.PATH)},
                            requestBody = @RequestBody(
                                    description = "Create file",
                                    required = true,
                                    content = {
                                            @Content(
                                                    mediaType = "multipart/form-data",
                                                    encoding = {
                                                            @Encoding(name = "document", contentType = "application/json")
                                                    },
                                                    schema = @Schema(type = "object", implementation = MultipartRequestDto.class)),
                                            @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = FileWithContentDto.class))
                                    }),
                            security = {@SecurityRequirement(name = "bearer-key")},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful Operation",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ResponseDto.class)))
                            }))
    })
    public RouterFunction<ServerResponse> questionnaireFolderRoutes(
            TestController controller) {
        return
                route(POST(PATH_MIXED).and(accept(MediaType.ALL)), controller::testMixed)
                .andRoute(POST(PATH_JSON).and(accept(MediaType.ALL)), controller::testJson)
                .andRoute(POST(PATH_MULTIPART).and(accept(MediaType.ALL)), controller::testMultipart);
    }
}
