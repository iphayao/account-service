package com.iphayao.accountservice.account;

import com.iphayao.accountservice.account.exception.SalaryLowerLimitException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public Account createNewAccount(AccountDto accountDto) throws Exception {
        Account newAccount = accountMapper.accountDtoToAccount(accountDto);

        newAccount.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        newAccount.setReferenceCode(generateReferenceCode(accountDto.getPhoneNumber()));
        newAccount.setMemberType(validateMemberType(accountDto.getSalary()));

        return accountRepository.save(newAccount);
    }

    private String generateReferenceCode(String phoneNumber) {
        String last4DigitPhoneNumber = phoneNumber.substring(phoneNumber.length() - 4);
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + last4DigitPhoneNumber;
    }

    private MemberType validateMemberType(double salary) throws Exception {
        if(salary > 50000) {
            return MemberType.PLATINUM;
        } else if(salary <= 50000 && salary >= 30000) {
            return MemberType.GOLD;
        } else if(salary < 30000 && salary >= 15000) {
            return MemberType.SILVER;
        } else {
            throw new SalaryLowerLimitException();
        }
    }
}
