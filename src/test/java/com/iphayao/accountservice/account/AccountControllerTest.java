package com.iphayao.accountservice.account;

import com.iphayao.accountservice.ApiResponse;
import com.iphayao.accountservice.account.exception.AccountNotFoundException;
import com.iphayao.accountservice.account.exception.SalaryLowerLimitException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.iphayao.accountservice.account.TestHelper.mockAccountDto;
import static com.iphayao.accountservice.account.TestHelper.mockAccountEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Mock
    private AccountService accountService;

    @Spy
    private AccountMapper accountMapper = new  AccountMapperImpl();

    @InjectMocks
    private AccountController accountController;

    @Test
    void test_create_new_account_expect_account_response() throws SalaryLowerLimitException {
        // arrange
        AccountDto accountDto = mockAccountDto();

        // act
        ResponseEntity<ApiResponse> response = accountController.registerAccount(accountDto);

        // assert
        assertNotNull(response);

    }

    @Test
    void test_create_new_account_expect_status_code_201() throws SalaryLowerLimitException {
        // arrange
        AccountDto accountDto = mockAccountDto();

        // act
        ResponseEntity<ApiResponse> response = accountController.registerAccount(accountDto);

        // assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    void test_create_new_account_expect_body_status_created() throws SalaryLowerLimitException {
        // arrange
        AccountDto accountDto = mockAccountDto();

        // act
        ResponseEntity<ApiResponse> response = accountController.registerAccount(accountDto);

        // assert
        assertEquals(HttpStatus.CREATED.getReasonPhrase(), response.getBody().getStatus());

    }

    @Test
    void test_create_new_account_expect_body_data_not_null() throws SalaryLowerLimitException {
        // arrange
        AccountDto accountDto = mockAccountDto();
        when(accountService.createNewAccount(eq(accountDto))).thenReturn(mockAccountEntity().get());

        // act
        ResponseEntity<ApiResponse> response = accountController.registerAccount(accountDto);

        // assert
        assertNotNull(response.getBody().getData());

    }

    @Test
    void test_create_new_account_expect_body_data_key_field_match_with_dto() throws SalaryLowerLimitException {
        // arrange
        AccountDto accountDto = mockAccountDto();
        when(accountService.createNewAccount(eq(accountDto))).thenReturn(mockAccountEntity().get());

        // act
        ResponseEntity<ApiResponse> response = accountController.registerAccount(accountDto);
        AccountRespDto respDto = (AccountRespDto) response.getBody().getData();

        // assert
        assertAll(() -> {
            assertEquals(accountDto.getUsername(), respDto.getUsername());
            assertEquals(accountDto.getFirstName(), respDto.getFirstName());
            assertEquals(accountDto.getLastName(), respDto.getLastName());
            assertEquals(accountDto.getPhoneNumber(), respDto.getPhoneNumber());
            assertEquals(accountDto.getAddress(), respDto.getAddress());
            assertEquals(accountDto.getEmail(), respDto.getEmail());
            assertEquals(accountDto.getSalary(), respDto.getSalary());
            assertNotNull(respDto.getMemberType());
            assertNotNull(respDto.getReferenceCode());
        });

    }

    @Test
    void test_get_account_expect_account_response() throws AccountNotFoundException {
        // arrange
        String referenceCode = "201910131234";

        // act
        ResponseEntity<ApiResponse> response = accountController.getAccount(referenceCode);

        // assert
        assertNotNull(response);
    }

    @Test
    void test_get_account_expect_status_code_200() throws AccountNotFoundException {
        // arrange
        String referenceCode = "201910131234";

        // act
        ResponseEntity<ApiResponse> response = accountController.getAccount(referenceCode);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void test_get_account_expect_body_status_ok() throws AccountNotFoundException {
        // arrange
        String referenceCode = "201910131234";

        // act
        ResponseEntity<ApiResponse> response = accountController.getAccount(referenceCode);

        // assert
        assertEquals(HttpStatus.OK.getReasonPhrase(), response.getBody().getStatus());
    }

    @Test
    void test_get_account_expect_body_data_not_null() throws AccountNotFoundException {
        // arrange
        String referenceCode = "201910131234";
        when(accountService.findAccount(eq(referenceCode))).thenReturn(mockAccountEntity().get());

        // act
        ResponseEntity<ApiResponse> response = accountController.getAccount(referenceCode);

        // assert
        assertNotNull(response.getBody().getData());
    }

    @Test
    void test_get_account_expect_body_data_key_fields_correct() throws AccountNotFoundException {
        // arrange
        String referenceCode = "201910131234";
        AccountDto accountDto = mockAccountDto();
        when(accountService.findAccount(eq(referenceCode))).thenReturn(mockAccountEntity().get());

        // act
        ResponseEntity<ApiResponse> response = accountController.getAccount(referenceCode);
        AccountRespDto respDto = (AccountRespDto) response.getBody().getData();

        // assert
        assertAll(() -> {
            assertEquals(accountDto.getUsername(), respDto.getUsername());
            assertEquals(accountDto.getFirstName(), respDto.getFirstName());
            assertEquals(accountDto.getLastName(), respDto.getLastName());
            assertEquals(accountDto.getPhoneNumber(), respDto.getPhoneNumber());
            assertEquals(accountDto.getAddress(), respDto.getAddress());
            assertEquals(accountDto.getEmail(), respDto.getEmail());
            assertEquals(accountDto.getSalary(), respDto.getSalary());
            assertNotNull(respDto.getMemberType());
            assertNotNull(respDto.getReferenceCode());
        });
    }

}