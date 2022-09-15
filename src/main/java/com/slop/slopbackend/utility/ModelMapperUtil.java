package com.slop.slopbackend.utility;


import com.slop.slopbackend.dto.response.user.UserResDTO;
import com.slop.slopbackend.entity.UserEntity;
import org.modelmapper.ModelMapper;

public class ModelMapperUtil {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserEntity toUserEntity(Object object) {
        return modelMapper.map(object, UserEntity.class);
    }

    public static UserResDTO toUserResDTO(Object object) {
        return modelMapper.map(object, UserResDTO.class);
    }

}

