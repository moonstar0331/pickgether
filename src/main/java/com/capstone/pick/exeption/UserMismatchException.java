package com.capstone.pick.exeption;

import lombok.Getter;

@Getter
public class UserMismatchException extends Exception {
    private Long voteId;
    public UserMismatchException(Long voteId){
        this.voteId = voteId;
    }

}
