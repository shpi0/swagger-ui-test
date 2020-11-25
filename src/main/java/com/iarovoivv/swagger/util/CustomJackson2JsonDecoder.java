package com.iarovoivv.swagger.util;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class CustomJackson2JsonDecoder {

    private CustomJackson2JsonDecoder() {}

    public static Jackson2JsonDecoder createJackson2JsonDecoder() {
        var om = Jackson2ObjectMapperBuilder.json().modules(new Jdk8Module()).build();
        Jackson2JsonDecoder jackson2JsonDecoder = new Jackson2JsonDecoder(om);
        SimpleModule localDateTimeDeserializerModule = new SimpleModule();
        jackson2JsonDecoder.getObjectMapper().registerModule(localDateTimeDeserializerModule);
        return jackson2JsonDecoder;
    }
}
