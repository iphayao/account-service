package com.iphayao.accountservice.account.exception;

import com.iphayao.accountservice.common.ApiException;

import static java.lang.String.format;

public class SalaryLowerLimitException extends ApiException {
    public SalaryLowerLimitException(Double message) {
        super(format("Salary %s lower than limit", message));
    }
}
