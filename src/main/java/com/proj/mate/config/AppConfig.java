package com.proj.mate.config;


import com.proj.mate.dto.UserRequestDto;
import com.proj.mate.entity.UserInfo;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();


        modelMapper.typeMap(UserRequestDto.class, UserInfo.class)
                .addMappings(mapper -> mapper.skip(UserInfo::setId));

        return modelMapper;
    }
}

