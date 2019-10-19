package com.iphayao.accountservice.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iphayao.accountservice.common.ApiResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.iphayao.accountservice.account.TestHelper.mockAccountDto;
import static com.iphayao.accountservice.account.TestHelper.mockAccountEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(secure = false)
@Disabled
public class AccountControllerWebTest {
    @MockBean
    private AccountService accountService;

    @SpyBean
    private AccountMapperImpl accountMapper;// = new AccountMapperImpl();

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    void test_register_new_account_expect_http_status_200_ok() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();

        // act
        MvcResult mvcResult = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(accountDto)))
                .andExpect(status().isCreated())
                .andReturn();

        // assert
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        assertNotNull(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void test_register_new_account_expect_response_data_not_null() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();
        when(accountService.createNewAccount(eq(accountDto))).thenReturn(mockAccountEntity().get());

        // act
        MvcResult mvcResult = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(accountDto)))
                .andExpect(status().isCreated())
                .andReturn();

        ApiResponse resData = jsonMapper.readValue(mvcResult.getResponse().getContentAsString(), ApiResponse.class);

        // assert
        assertNotNull(resData.getData());
    }

    @Test
    void test_get_account_expect_http_status_200_ok() throws Exception {
        // arrange
        String referenceCode = "201910131234";

        // act
        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/{reference_code}", referenceCode))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertNotNull(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void test_get_account_expect_response_data_not_null() throws Exception {
        // arrange
        String referenceCode = "201910131234";
        when(accountService.findAccount(eq(referenceCode))).thenReturn(mockAccountEntity().get());

        // act
        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/{reference_code}", referenceCode))
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse resData = jsonMapper.readValue(mvcResult.getResponse().getContentAsString(), ApiResponse.class);

        // assert
        assertNotNull(resData.getData());

    }
}
