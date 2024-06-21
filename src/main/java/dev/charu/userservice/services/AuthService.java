package dev.charu.userservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.charu.userservice.Dtos.SendEmailDto;
import dev.charu.userservice.Dtos.UserDto;
import dev.charu.userservice.KafkaConfig.KafkaProducerClient;
import dev.charu.userservice.exceptions.UserAlreadyExistsException;
import dev.charu.userservice.exceptions.UserDoesNotExistException;
import dev.charu.userservice.model.Session;
import dev.charu.userservice.model.User;
import dev.charu.userservice.model.sessionStatus;
import dev.charu.userservice.repositories.SessionRepository;
import dev.charu.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private KafkaProducerClient kafkaProducerClient;
    private ObjectMapper objectMapper;

    public AuthService(UserRepository userRepository,SessionRepository sessionRepository,KafkaProducerClient kafkaProducerClient,ObjectMapper objectMapper) {
          this.userRepository=userRepository;
          this.sessionRepository=sessionRepository;
          this.bCryptPasswordEncoder=new BCryptPasswordEncoder();
          this.kafkaProducerClient=kafkaProducerClient;
          this.objectMapper=objectMapper;
    }

    public ResponseEntity<UserDto> login(String email, String password) throws UserDoesNotExistException {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UserDoesNotExistException("User with email: " + email + " doesn't exist.");
        }

        User user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }




        String token = RandomStringUtils.randomAscii(20);
        //String token= String.valueOf(System.currentTimeMillis());
        MultiValueMapAdapter<String, String > headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add("AUTH_TOKEN", token);

        Session session = new Session();
        session.setSessionStatus(sessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);


        UserDto userDto = UserDto.from(user);
        ResponseEntity<UserDto> response = new ResponseEntity<>(
                userDto,
                headers,
                HttpStatus.OK
        );

        return response;
    }

    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }

        Session session = sessionOptional.get();

        session.setSessionStatus(sessionStatus.LOGGED_OUT);

        sessionRepository.save(session);

        return ResponseEntity.ok().build();
    }

    public UserDto signUp(String email, String password) throws UserAlreadyExistsException, JsonProcessingException {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (!userOptional.isEmpty()) {
            throw new UserAlreadyExistsException("User with " + email + " already exists.");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        User savedUser = userRepository.save(user);
        SendEmailDto sendEmailDto=new SendEmailDto();
        sendEmailDto.setTo(email);
        sendEmailDto.setFrom("charupandey2");
        sendEmailDto.setBody("welcome to scaler");
        sendEmailDto.setSubject("hello Thanks for joining");

        kafkaProducerClient.sendEvent("sendEmail",objectMapper.writeValueAsString(sendEmailDto));
        return UserDto.from(savedUser);

    }

    public Optional<UserDto> validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return Optional.empty();
        }

        Session session = sessionOptional.get();

        if (!session.getSessionStatus().equals(sessionStatus.ACTIVE)) {
            return Optional.empty();
        }

        User user = userRepository.findById(userId).get();

        UserDto userDto = UserDto.from(user);


        return Optional.of(userDto);
    }

}



