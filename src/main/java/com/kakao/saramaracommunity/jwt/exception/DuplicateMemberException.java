package com.kakao.saramaracommunity.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*@NoArgsConstructor
@AllArgsConstructor*/
@Builder
@Getter
public class DuplicateMemberException extends RuntimeException {

    public String message;
    public Throwable cause;

    public DuplicateMemberException() {
        super();
    }
    public DuplicateMemberException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateMemberException(String message) {
        super(message);
    }
    public DuplicateMemberException(Throwable cause) {
        super(cause);
    }
}
