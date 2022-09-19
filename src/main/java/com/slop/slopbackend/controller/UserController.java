package com.slop.slopbackend.controller;

import com.slop.slopbackend.dto.request.user.UpdateEmailIdReqDTO;
import com.slop.slopbackend.dto.request.user.UpdatePasswordReqDTO;
import com.slop.slopbackend.dto.request.user.UpdateUserReqDTO;
import com.slop.slopbackend.dto.response.user.UserResDTO;
import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.service.UserService;
import com.slop.slopbackend.utility.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController()
@RequestMapping("users")
@CrossOrigin("*")
public class UserController {
    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
// TODO: check if user is authorized to update his document
    @PatchMapping("{id}")
    public Object updateUser(@RequestBody @Valid UpdateUserReqDTO updateUserReqDTO, @PathVariable UUID id){
        UserEntity userEntity=userService.updateUserById(updateUserReqDTO,id);
        return ModelMapperUtil.toUserResDTO(userEntity);
    }

   @PatchMapping("{id}/emailid")
   public UserResDTO updateUserEmailId(@RequestBody @Valid UpdateEmailIdReqDTO updateEmailIdReqDTO, @PathVariable UUID id){
        UserEntity userEntity=userService.updateEmailIdById(updateEmailIdReqDTO,id);
        return ModelMapperUtil.toUserResDTO(userEntity);
   }

    @PatchMapping("{id}/password")
    public UserResDTO updateUserPassword(@RequestBody @Valid UpdatePasswordReqDTO updatePasswordReqDTO, @PathVariable UUID id){
        UserEntity userEntity=userService.updatePasswordById(updatePasswordReqDTO,id);
        return ModelMapperUtil.toUserResDTO(userEntity);
    }
}
