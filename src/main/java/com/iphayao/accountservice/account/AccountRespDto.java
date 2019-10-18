package com.iphayao.accountservice.account;

import lombok.Data;

@Data
public class AccountRespDto {
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String email;
    private double salary;
    private MemberType memberType;
    private String referenceCode;
}
