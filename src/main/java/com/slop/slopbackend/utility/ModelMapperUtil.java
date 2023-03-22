package com.slop.slopbackend.utility;


import com.slop.slopbackend.dto.response.event.EventCompleteResDTO;
import com.slop.slopbackend.dto.response.event.EventResDTO;
import com.slop.slopbackend.dto.response.user.UserResDTO;
import com.slop.slopbackend.dto.response.user.UserSignInResDTO;
import com.slop.slopbackend.entity.EventEntity;
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

    public static EventEntity toEventEntity(Object object) {
        return modelMapper.map(object, EventEntity.class);
    }

    public static EventResDTO toEventResDTO(Object object) {
        return modelMapper.map(object,EventResDTO.class);
    }

    public static EventCompleteResDTO toEventCompleteResDTO(Object object) {
        return modelMapper.map(object,EventCompleteResDTO.class);
    }

    public static <T> T toObject(Object object, Class<T> className) {
        return modelMapper.map(object, className);
    }
}

