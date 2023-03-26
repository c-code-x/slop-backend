package com.slop.slopbackend.service;


import com.slop.slopbackend.dto.request.user.UpdateEmailIdReqDTO;
import com.slop.slopbackend.dto.request.user.UpdateUserReqDTO;
import com.slop.slopbackend.dto.response.event.EventResDTO;
import com.slop.slopbackend.dto.response.user.UserFeedResDTO;
import com.slop.slopbackend.entity.*;
import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.repository.ClubFollowerRepository;
import com.slop.slopbackend.repository.EventRepository;
import com.slop.slopbackend.repository.UserEventRepository;
import com.slop.slopbackend.repository.UserRepository;
import com.slop.slopbackend.utility.FileUploadUtil;
import com.slop.slopbackend.utility.ModelMapperUtil;
import com.slop.slopbackend.utility.UserEventAction;
import com.slop.slopbackend.utility.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final ClubFollowerRepository clubFollowerRepository;
    private final UserEventRepository userEventRepository;
    private final EventRepository eventRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ImageService imageService, ClubFollowerRepository clubFollowerRepository, UserEventRepository userEventRepository,
                       EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.clubFollowerRepository = clubFollowerRepository;
        this.userEventRepository = userEventRepository;
        this.eventRepository = eventRepository;
    }

    public UserEntity getUserById(UUID id) {
        Optional<UserEntity> optionalUserEntity=userRepository.findById(id);
        if(optionalUserEntity.isEmpty())
            throw new ApiRuntimeException("User does not exist",HttpStatus.NOT_FOUND);
        return optionalUserEntity.get();
    }
    public UserEntity getUserByEmailId(String emailId) {
        Optional<UserEntity> optionalUserEntity=userRepository.findByEmailId(emailId);
        if(optionalUserEntity.isEmpty())
            throw new ApiRuntimeException("User with email:\""+emailId+"\" does not exist",HttpStatus.NOT_FOUND);
        return optionalUserEntity.get();
    }

    public UserEntity saveUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        if(userRepository.existsByEmailId(userEntity.getEmailId()))
            throw new ApiRuntimeException("EmailId is already taken!",HttpStatus.ALREADY_REPORTED);
        if(userRepository.existsByRegistrationId(userEntity.getRegistrationId()))
            throw new ApiRuntimeException("Registration id is already taken!",HttpStatus.ALREADY_REPORTED);
        if(userRepository.existsByPhoneNumber(userEntity.getPhoneNumber()))
            throw new ApiRuntimeException("Phone number is already taken!",HttpStatus.ALREADY_REPORTED);
        return userRepository.save(userEntity);
    }
    public UserEntity updateUserById(UpdateUserReqDTO updateUserReqDTO, UUID id) {
        Optional<UserEntity> optionalUserEntity=userRepository.findById(id);
        if(optionalUserEntity.isEmpty())
            throw new ApiRuntimeException("User does not exist",HttpStatus.NOT_FOUND);

        UserEntity userEntity=optionalUserEntity.get();
        userEntity.setFullName(updateUserReqDTO.getFullName());
        userEntity.setBio(updateUserReqDTO.getBio());

        return userRepository.save(userEntity);
    }

    public UserEntity updateEmailIdById(UpdateEmailIdReqDTO updateEmailIdReqDTO, UUID id) {
        Optional<UserEntity> optionalUserEntity=userRepository.findById(id);
        if(optionalUserEntity.isEmpty())
            throw new ApiRuntimeException("User does not exist",HttpStatus.NOT_FOUND);
        if(userRepository.existsByEmailId(updateEmailIdReqDTO.getEmailId()))
            throw new ApiRuntimeException("Email is already taken.",HttpStatus.ALREADY_REPORTED);

        UserEntity userEntity=optionalUserEntity.get();
        userEntity.setEmailId(updateEmailIdReqDTO.getEmailId());
        return userRepository.save(userEntity);
    }

    public UserEntity updatePasswordById(String password, UUID id) {

        Optional<UserEntity> optionalUserEntity=userRepository.findById(id);
        if(optionalUserEntity.isEmpty())
            throw new ApiRuntimeException("user does not exist", HttpStatus.NOT_FOUND);

        UserEntity userEntity=optionalUserEntity.get();
        userEntity.setPassword(passwordEncoder.encode(password));
        return userRepository.save(userEntity);
    }
    public UserEntity promoteUserToClub(UserEntity userEntity) {
        userEntity.setUserRole(UserRole.CLUB);
        return userRepository.save(userEntity);
    }
    public byte[] getUserProfilePicture(UUID id) throws IOException {
        UserEntity userEntity=getUserById(id);
        if(userEntity.getProfilePicture()==null)
            throw new ApiRuntimeException("User does not have a profile picture",HttpStatus.NO_CONTENT);
        String filePath=userEntity.getProfilePicture();
        if(filePath.isEmpty())
            throw new ApiRuntimeException("User does not have a profile picture",HttpStatus.NOT_FOUND);
        return FileUploadUtil.readFileToByteArray(filePath);
    }

    public UserEntity updateUserProfilePictureById(UUID id, MultipartFile file) throws IOException{
        UserEntity userEntity=getUserById(id);
        String fileName=imageService.saveImage(file);
        userEntity.setProfilePicture(fileName);
        return userRepository.save(userEntity);
    }

    public void followClub(UserEntity userEntity, ClubEntity clubEntity) {
        if(clubFollowerRepository.existsByClubAndUser(clubEntity,userEntity))
            throw new ApiRuntimeException("User is already following the club",HttpStatus.ALREADY_REPORTED);
        ClubFollowerEntity clubFollowerEntity=new ClubFollowerEntity();
        clubFollowerEntity.setClub(clubEntity);
        clubFollowerEntity.setUser(userEntity);
        clubFollowerRepository.save(clubFollowerEntity);
    }

    public void unfollowClub(UserEntity userEntity, ClubEntity clubEntity) {
        Optional<ClubFollowerEntity> optionalClubFollowerEntity=clubFollowerRepository.findByClubAndUser(clubEntity,userEntity);
        if(optionalClubFollowerEntity.isEmpty())
            throw new ApiRuntimeException("User is not following the club",HttpStatus.NOT_FOUND);
        clubFollowerRepository.delete(optionalClubFollowerEntity.get());
    }

    public UserFeedResDTO getUserFeed(UserEntity userEntity) {
        List<EventEntity> eventEntities=eventRepository.findAllEventsByClubOwnerId(userEntity.getId());
        List<EventResDTO> eventResDTOS=eventEntities.stream().map((eventEntity)->{
            EventResDTO eventResDTO= ModelMapperUtil.toObject(eventEntity,EventResDTO.class);
            long numberOfLikes=userEventRepository.countByEventAndAction(eventEntity, UserEventAction.LIKED);
            long numberOfShares=userEventRepository.countByEventAndAction(eventEntity, UserEventAction.SHARED);
            long numberOfRegistrations=userEventRepository.countByEventAndAction(eventEntity, UserEventAction.REGISTERED);
            long numberOfAttendees=userEventRepository.countByEventAndAction(eventEntity, UserEventAction.ATTENDED);
            eventResDTO.setNumberOfLikes(numberOfLikes);
            eventResDTO.setNumberOfShares(numberOfShares);
            eventResDTO.setNumberOfRegistrations(numberOfRegistrations);
            eventResDTO.setNumberOfAttendees(numberOfAttendees);
            eventResDTO.setLiked(userEventRepository.existsByUserAndEventAndAction(userEntity,eventEntity,UserEventAction.LIKED));
            eventResDTO.setClubName(eventEntity.getClub().getClubName());
            eventResDTO.setClubProfilePicture(eventEntity.getClub().getOwner().getProfilePicture());
            eventResDTO.setClubSlug(eventEntity.getClub().getClubSlug());
            return eventResDTO;
        }).toList();
        return UserFeedResDTO.builder()
                .events(eventResDTOS)
                .build();
    }
}
