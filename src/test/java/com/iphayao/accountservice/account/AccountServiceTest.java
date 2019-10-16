package com.iphayao.accountservice.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Spy
    private AccountMapper accountMapper = new AccountMapperImpl();

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private AccountService accountService;

    @Test
    void test_create_new_account_expect_return_account_not_null() {
        // arrange
        AccountDto accountDto = mockAccountDto();
        when(accountRepository.save(any(Account.class))).thenAnswer(value -> value.getArgument(0));

        // act
        Account account = accountService.createNewAccount(accountDto);

        // assert
        assertNotNull(account);
    }

    @Test
    void test_create_new_account_expect_save_account() {
        // arrange
        AccountDto accountDto = mockAccountDto();

        // act
        accountService.createNewAccount(accountDto);

        // assert
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void test_create_new_account_expect_encrypted_password() {
        // arrange;
        AccountDto accountDto = mockAccountDto();

        when(accountRepository.save(any(Account.class))).thenAnswer(value -> value.getArgument(0));

        // act
        Account account = accountService.createNewAccount(accountDto);

        // assert
        assertNotEquals(accountDto.getPassword(), account.getPassword());
        assertTrue(passwordEncoder.matches(accountDto.getPassword(), account.getPassword()));
    }

    private AccountDto mockAccountDto() {
        return AccountDto.builder()
                .username("test_user")
                .password("P@ssw0rd")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("08123456789")
                .address("123, Bangkok")
                .email("john.doe@gmail.com")
                .salary(15000)
                .build();
    }

}