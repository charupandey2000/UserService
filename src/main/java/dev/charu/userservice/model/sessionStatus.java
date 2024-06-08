package dev.charu.userservice.model;

import org.springframework.stereotype.Component;


public enum sessionStatus {
    ACTIVE,
    EXPIRED,
    LOGGED_OUT,
    INVALID,
}
