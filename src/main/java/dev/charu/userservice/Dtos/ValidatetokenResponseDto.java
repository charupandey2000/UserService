package dev.charu.userservice.Dtos;

import dev.charu.userservice.model.sessionStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.support.SessionStatus;

@Getter
@Setter
public class ValidatetokenResponseDto {
     private UserDto userDto;
    private sessionStatus sessionStatus;
}
