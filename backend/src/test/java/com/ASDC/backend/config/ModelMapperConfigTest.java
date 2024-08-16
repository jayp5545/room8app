package com.ASDC.backend.config;

import com.ASDC.backend.config.ModelMapperConfig;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
public class ModelMapperConfigTest {

    @Test
    public void modelMapperBeanTest() {
        ModelMapperConfig modelMapperConfig = new ModelMapperConfig();
        ModelMapper modelMapper = modelMapperConfig.modelMapper();
        assertNotNull(modelMapper);
    }
}
