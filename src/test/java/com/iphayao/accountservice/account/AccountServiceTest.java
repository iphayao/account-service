package com.iphayao.accountservice.account;

import com.iphayao.accountservice.account.exception.AccountNotFoundException;
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
import java.util.Optional;

import static com.iphayao.accountservice.account.TestHelper.mockAccountDto;
import static com.iphayao.accountservice.account.TestHelper.mockAccountEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        when(accountRepository.findByPhoneNumber(eq(accountDto.getPhoneNumber()))).thenReturn(Optional.empty());
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
        when(accountRepository.findByPhoneNumber(eq(accountDto.getPhoneNumber()))).thenReturn(Optional.empty());
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
        when(accountRepository.findByPhoneNumber(eq(accountDto.getPhoneNumber()))).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenAnswer(value -> value.getArgument(0));

        // act
        Account account = accountService.createNewAccount(accountDto);

        // assert
        assertEquals(expectReferenceCode, account.getReferenceCode());
    }

    @Test
    void test_create_new_account_expect_reference_code_size_12_digit() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();
        when(accountRepository.findByPhoneNumber(eq(accountDto.getPhoneNumber()))).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenAnswer(value -> value.getArgument(0));

        // act
        Account account = accountService.createNewAccount(accountDto);

        // assert
        assertEquals(12, account.getReferenceCode().length());
    }

    @Test
    void test_create_new_account_expect_member_type_silver_when_salary_15000() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();
        accountDto.setSalary(15000);
        when(accountRepository.findByPhoneNumber(eq(accountDto.getPhoneNumber()))).thenReturn(Optional.empty());
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
        when(accountRepository.findByPhoneNumber(eq(accountDto.getPhoneNumber()))).thenReturn(Optional.empty());
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
        when(accountRepository.findByPhoneNumber(eq(accountDto.getPhoneNumber()))).thenReturn(Optional.empty());
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
        when(accountRepository.findByPhoneNumber(eq(accountDto.getPhoneNumber()))).thenReturn(Optional.empty());
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
        when(accountRepository.findByPhoneNumber(eq(accountDto.getPhoneNumber()))).thenReturn(Optional.empty());
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
        when(accountRepository.findByPhoneNumber(eq(accountDto.getPhoneNumber()))).thenReturn(Optional.empty());
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

    @Test
    void test_create_new_account_expect_account_registered_exception_when_register_same_mobile_number() {
        // arrange
        AccountDto accountDto = mockAccountDto();
        when(accountRepository.findByPhoneNumber(eq(accountDto.getPhoneNumber()))).thenReturn(mockAccountEntity());

        // act
        // assert
        assertThrows(AccountRegisteredException.class, () -> accountService.createNewAccount(accountDto));
    }

    @Test
    void test_find_account_by_reference_code_expect_account_not_null() throws Exception {
        // arrange
        String referenceCode = "201910131234";

        when(accountRepository.findByReferenceCode(eq(referenceCode))).thenReturn(mockAccountEntity());

        // act
        Account account = accountService.findAccount(referenceCode);

        // assert
        assertNotNull(account);
    }

    @Test
    void test_find_account_by_reference_code_expect_account_not_found_exception_when_not_found_account() {
        // arrange
        String referenceCode = "201910131234";

        when(accountRepository.findByReferenceCode(eq(referenceCode))).thenReturn(Optional.empty());

        // act
        // assert
        assertThrows(AccountNotFoundException.class, () -> accountService.findAccount(referenceCode));
    }

    private String generateExpectReferenceCode(AccountDto accountDto) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "6789";
    }

}