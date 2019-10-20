package com.iphayao.accountservice.account;

import com.iphayao.accountservice.common.ApiException;

import static java.lang.String.format;

public class AccountRegisteredException extends ApiException {
    public AccountRegisteredException(String message) {
        super(format("Account with phone number %s already registered", message));
    }
}
