package dev.charu.userservice.controller;

import dev.charu.userservice.Dtos.*;
import dev.charu.userservice.exceptions.UserAlreadyExistsException;
import dev.charu.userservice.exceptions.UserDoesNotExistException;
import dev.charu.userservice.model.sessionStatus;
import dev.charu.userservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    AuthService authService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request) throws UserDoesNotExistException {
        return null;
        //return authService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request) {
        return null;
        //return authService.logout(request.getToken(), request.getUserId());
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request) throws UserAlreadyExistsException {
        //return null;
        UserDto userDto = authService.signUp(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidatetokenResponseDto> validateToken(@RequestBody ValidateTokenRequestDto request) {
//        Optional<UserDto> userDto = authService.validate(request.getToken(), request.getUserId());
//
//        if (userDto.isEmpty()) {
//            ValidatetokenResponseDto response = new ValidatetokenResponseDto();
//            response.setSessionStatus(sessionStatus.INVALID);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
//
//        ValidatetokenResponseDto response = new ValidatetokenResponseDto();
//        response.setSessionStatus(sessionStatus.ACTIVE);
//        response.setUserDto(userDto.get());
//        return new ResponseEntity<>(response, HttpStatus.OK);
        return null;
    }


}
