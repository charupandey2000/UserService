package dev.charu.userservice.Dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendEmailDto {
    private String to;
    private String from;
    private  String Subject;
    private String Body;

}
