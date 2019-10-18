package com.iphayao.accountservice.account;

import com.iphayao.accountservice.ApiResponse;
import com.iphayao.accountservice.account.exception.AccountNotFoundException;
import com.iphayao.accountservice.account.exception.SalaryLowerLimitException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper mapper;

    @GetMapping("/{reference_code}")
    public ResponseEntity<ApiResponse> getAccount(@PathVariable("reference_code") String referenceCode) throws AccountNotFoundException {
        Account account = accountService.findAccount(referenceCode);
        return ResponseEntity.ok(ApiResponse.ok(mapper.accountToAccountRespDto(account)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> registerAccount(@RequestBody AccountDto accountDto) throws SalaryLowerLimitException {
        Account account = accountService.createNewAccount(accountDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(mapper.accountToAccountRespDto(account)));
    }
}
