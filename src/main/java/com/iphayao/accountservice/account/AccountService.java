package com.iphayao.accountservice.account;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository,
                          AccountMapper accountMapper,
                          PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Account createNewAccount(AccountDto accountDto) {
        Account newAccount = accountMapper.accountDtoToAccount(accountDto);
        newAccount.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        return accountRepository.save(newAccount);
    }
}
