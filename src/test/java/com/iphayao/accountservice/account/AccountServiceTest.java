package com.iphayao.accountservice.account;

import com.iphayao.accountservice.account.exception.SalaryLowerLimitException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    void test_create_new_account_expect_return_account_not_null() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();
        when(accountRepository.save(any(Account.class))).thenAnswer(value -> value.getArgument(0));

        // act
        Account account = accountService.createNewAccount(accountDto);

        // assert
        assertNotNull(account);
    }

    @Test
    void test_create_new_account_expect_save_account() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();

        // act
        accountService.createNewAccount(accountDto);

        // assert
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void test_create_new_account_expect_encrypted_password() throws Exception {
        // arrange;
        AccountDto accountDto = mockAccountDto();
        when(accountRepository.save(any(Account.class))).thenAnswer(value -> value.getArgument(0));

        // act
        Account account = accountService.createNewAccount(accountDto);

        // assert
        assertNotEquals(accountDto.getPassword(), account.getPassword());
        assertTrue(passwordEncoder.matches(accountDto.getPassword(), account.getPassword()));
    }

    @Test
    void test_create_new_account_expect_generate_reference_code() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();
        String expectReferenceCode = generateExpectReferenceCode(accountDto);
        when(accountRepository.save(any(Account.class))).thenAnswer(value -> value.getArgument(0));

        // act
        Account account = accountService.createNewAccount(accountDto);

        // assert
        assertEquals(expectReferenceCode, account.getReferenceCode());
    }

    @Test
    void test_create_new_account_expect_member_type_silver_when_salary_15000() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();
        accountDto.setSalary(15000);
        when(accountRepository.save(any(Account.class))).thenAnswer(value -> value.getArgument(0));

        // act
        Account account = accountService.createNewAccount(accountDto);

        // assert
        assertEquals(MemberType.SILVER, account.getMemberType());
    }

    @Test
    void test_create_new_account_expect_member_type_silver_when_salary_29999() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();
        accountDto.setSalary(29999);
        when(accountRepository.save(any(Account.class))).thenAnswer(value -> value.getArgument(0));

        // act
        Account account = accountService.createNewAccount(accountDto);

        // assert
        assertEquals(MemberType.SILVER, account.getMemberType());
    }

    @Test
    void test_create_new_account_expect_member_type_gold_when_salary_30000() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();
        accountDto.setSalary(30000);
        when(accountRepository.save(any(Account.class))).thenAnswer(value -> value.getArgument(0));

        // act
        Account account = accountService.createNewAccount(accountDto);

        // assert
        assertEquals(MemberType.GOLD, account.getMemberType());
    }

    @Test
    void test_create_new_account_expect_member_type_gold_when_salary_50000() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();
        accountDto.setSalary(50000);
        when(accountRepository.save(any(Account.class))).thenAnswer(value -> value.getArgument(0));

        // act
        Account account = accountService.createNewAccount(accountDto);

        // assert
        assertEquals(MemberType.GOLD, account.getMemberType());
    }

    @Test
    void test_create_new_account_expect_member_type_gold_when_salary_50001() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();
        accountDto.setSalary(50001);
        when(accountRepository.save(any(Account.class))).thenAnswer(value -> value.getArgument(0));

        // act
        Account account = accountService.createNewAccount(accountDto);

        // assert
        assertEquals(MemberType.PLATINUM, account.getMemberType());
    }

    @Test
    void test_create_new_account_expect_member_type_gold_when_salary_100000() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();
        accountDto.setSalary(100000);
        when(accountRepository.save(any(Account.class))).thenAnswer(value -> value.getArgument(0));

        // act
        Account account = accountService.createNewAccount(accountDto);

        // assert
        assertEquals(MemberType.PLATINUM, account.getMemberType());
    }

    @Test
    void test_create_new_account_expect_salary_lower_limit_exception_when_salary_14999() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();
        accountDto.setSalary(14999);

        // act
        // assert
        assertThrows(SalaryLowerLimitException.class, () -> accountService.createNewAccount(accountDto));

    }

    private String generateExpectReferenceCode(AccountDto accountDto) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "6789";
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