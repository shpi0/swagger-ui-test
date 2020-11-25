package com.iarovoivv.swagger.controller;

import com.iarovoivv.swagger.dto.FileWithContentDto;
import com.iarovoivv.swagger.dto.ResponseDto;
import com.iarovoivv.swagger.util.CustomJackson2JsonDecoder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {

    private static final String ID_PARAM = "id";

    public Mono<ServerResponse> testJson(ServerRequest request) {
        var id = request.pathVariable(ID_PARAM);
        return Mono.just(new ResponseDto().setResponse(id).setExtra("JSON"))
                .flatMap(result -> ServerResponse.ok().bodyValue(result))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> testMultipart(ServerRequest request) {
        var id = request.pathVariable(ID_PARAM);
        return Mono.just(new ResponseDto().setResponse(id).setExtra("MULTIPART"))
                .flatMap(result -> ServerResponse.ok().bodyValue(result))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> testMixed(ServerRequest request) {
        var id = request.pathVariable(ID_PARAM);
        var mediaType = request.headers().contentType();
        if (mediaType.isPresent()) {
            if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType.get())) {
                return request
                        .bodyToMono(FileWithContentDto.class)
                        .map(dto -> new ResponseDto().setResponse(id).setExtra("MIXED/JSON " + dto.toString()))
                        .flatMap(result -> ServerResponse.status(HttpStatus.CREATED).bodyValue(result));
            } else if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(mediaType.get())) {
                return request
                        .multipartData()
                        .flatMap(parts -> {
                            var partMap = parts.toSingleValueMap();
                            var part = partMap.get("document");
                            return CustomJackson2JsonDecoder.createJackson2JsonDecoder()
                                    .decode(part.content(),
                                            ResolvableType.forClass(FileWithContentDto.class), null, null)
                                    .last()
                                    .cast(FileWithContentDto.class)
                                    .flatMap(dto -> {
                                        var filePart = partMap.get("file");
                                        return filePart.content().map(data -> {
                                            byte[] bytes = new byte[data.readableByteCount()];
                                            data.read(bytes);
                                            return new String(Base64Utils.encode(bytes), StandardCharsets.UTF_8);
                                        })
                                                .collect(Collectors.joining())
                                                .map(dto::setData);
                                    })
                                    .map(dto -> new ResponseDto().setResponse(id).setExtra("MIXED/MULTIPART " + dto.toString()));
                        })
                        .flatMap(result -> ServerResponse.status(HttpStatus.CREATED).bodyValue(result));
            }
        }
        return ServerResponse.badRequest().build();
    }
}
