package com.iphayao.accountservice.account;

import com.iphayao.accountservice.account.exception.SalaryLowerLimitException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class AccountService {
    private static final double LOWER_BOUND_MEMBER_TYPE_PLATINUM = 50000.00;
    private static final double UPPER_BOUND_MEMBER_TYPE_GOLD = 50000.00;
    private static final double LOWER_BOUND_MEMBER_TYPE_GOLD = 30000.00;
    private static final double UPPER_BOUND_MEMBER_TYPE_SILVER = 30000.00;
    private static final double LOWER_BOUND_MEMBER_TYPE_SILVER = 15000.00;

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

    public Account createNewAccount(AccountDto accountDto) throws SalaryLowerLimitException {
        Account newAccount = accountMapper.accountDtoToAccount(accountDto);

        newAccount.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        newAccount.setReferenceCode(generateReferenceCode(accountDto.getPhoneNumber()));
        newAccount.setMemberType(validateMemberType(accountDto.getSalary()));

        return accountRepository.save(newAccount);
    }

    public Account findAccount(String referenceCode) throws AccountNotFoundException {
        Optional<Account> account = accountRepository.findByReferenceCode(referenceCode);
        return account.orElseThrow(AccountNotFoundException::new);
    }

    private String generateReferenceCode(String phoneNumber) {
        String last4DigitPhoneNumber = phoneNumber.substring(phoneNumber.length() - 4);
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + last4DigitPhoneNumber;
    }

    private MemberType validateMemberType(double salary) throws SalaryLowerLimitException {
        if(salary > LOWER_BOUND_MEMBER_TYPE_PLATINUM) {
            return MemberType.PLATINUM;
        } else if(salary <= UPPER_BOUND_MEMBER_TYPE_GOLD && salary >= LOWER_BOUND_MEMBER_TYPE_GOLD) {
            return MemberType.GOLD;
        } else if(salary < UPPER_BOUND_MEMBER_TYPE_SILVER && salary >= LOWER_BOUND_MEMBER_TYPE_SILVER) {
            return MemberType.SILVER;
        } else {
            throw new SalaryLowerLimitException();
        }
    }
}
