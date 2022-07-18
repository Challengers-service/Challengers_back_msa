package com.challengers.userservice.domain.user.service;

import com.challengers.userservice.common.exception.UserException;
import com.challengers.userservice.domain.user.dto.UserMeResponse;
import com.challengers.userservice.domain.user.dto.UserUpdateRequest;
import com.challengers.userservice.domain.user.entity.User;
import com.challengers.userservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserMeResponse getCurrentUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException());
        return UserMeResponse.builder()
                .user(user)
                .build();
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateRequest userUpdateRequest){
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        String changeName = userUpdateRequest.getName();
        String changeBio = userUpdateRequest.getBio();

        user.update(changeName, changeBio, User.DEFAULT_IMAGE_URL);
    }
}
