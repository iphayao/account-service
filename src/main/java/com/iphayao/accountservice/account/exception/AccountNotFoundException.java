package com.iphayao.accountservice.account.exception;

import com.iphayao.accountservice.common.ApiException;

import static java.lang.String.format;

public class AccountNotFoundException extends ApiException {
    public AccountNotFoundException(String message) {
        super(format("Account ID %s not found", message));
    }
}
