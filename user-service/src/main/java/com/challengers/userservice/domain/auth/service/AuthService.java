package com.challengers.userservice.domain.auth.service;

import com.challengers.userservice.common.exception.BadRequestException;
import com.challengers.userservice.common.exception.UserException;
import com.challengers.userservice.domain.user.entity.AuthProvider;
import com.challengers.userservice.domain.user.entity.Role;
import com.challengers.userservice.domain.user.entity.User;
import com.challengers.userservice.common.security.TokenProvider;
import com.challengers.userservice.domain.auth.dto.AuthDto;
import com.challengers.userservice.domain.auth.dto.LogInRequest;
import com.challengers.userservice.domain.auth.dto.TokenDto;
import com.challengers.userservice.domain.user.repository.UserRepository;
import com.challengers.userservice.global.client.PointClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    private final PointClient pointClient;

    @Transactional
    public ResponseEntity<String> signUp(@Valid @RequestBody AuthDto authDto) {
        if(userRepository.existsByEmail(authDto.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }


        User newUser = userRepository.save(User.builder()
                .name(authDto.getName())
                .email(authDto.getEmail())
                .provider(AuthProvider.local)
                .password(passwordEncoder.encode(authDto.getPassword()))
                .image(User.DEFAULT_IMAGE_URL)
                .role(Role.USER)
                .build()
        );

        pointClient.createPointInfo(newUser.getId().toString());

        return new ResponseEntity<String>("회원 가입이 성공적으로 완료되었습니다!", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody LogInRequest logInRequest) {
        User user = userRepository.findByEmail(logInRequest.getEmail()).orElseThrow(UserException::new);

        String jwt = tokenProvider.createTokenByUserEntity(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto("Bearer " + jwt), httpHeaders, HttpStatus.OK);
    }
}