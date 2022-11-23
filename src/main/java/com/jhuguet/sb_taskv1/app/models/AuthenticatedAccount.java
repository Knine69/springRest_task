package com.jhuguet.sb_taskv1.app.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthenticatedAccount {
    private String token;
    private Date timestamp;
}
