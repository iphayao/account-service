package com.iphayao.accountservice.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iphayao.accountservice.account.exception.AccountNotFoundException;
import com.iphayao.accountservice.common.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static com.iphayao.accountservice.account.TestHelper.mockAccountDto;
import static com.iphayao.accountservice.account.TestHelper.mockAccountEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountControllerWebTest {
    private static final String CLIENT_ID = "app-client";
    private static final String CLIENT_SECRET = "noonewilleverguess";

    @MockBean
    private AccountService accountService;

    @SpyBean
    private AccountMapperImpl accountMapper;

    private MockMvc mockMvc;

    private ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext context;

    AccountControllerWebTest() {
    }

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void test_register_new_account_expect_http_status_200_ok() throws Exception {
        // arrange
        AccountDto accountDto = mockAccountDto();
        String accessToken = getBearerToken(CLIENT_ID, CLIENT_SECRET);

        // act
        MvcResult mvcResult = mockMvc.perform(post("/api/accounts")
                .header("Authorization", accessToken)
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
        String accessToken = getBearerToken(CLIENT_ID, CLIENT_SECRET);

        when(accountService.createNewAccount(eq(accountDto))).thenReturn(mockAccountEntity().get());

        // act
        MvcResult mvcResult = mockMvc.perform(post("/api/accounts")
                .header("Authorization", accessToken)
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
        String accessToken = getBearerToken(CLIENT_ID, CLIENT_SECRET);

        // act
        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/{reference_code}", referenceCode)
                .header("Authorization", accessToken))
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
        String accessToken = getBearerToken(CLIENT_ID, CLIENT_SECRET);

        when(accountService.findAccount(eq(referenceCode))).thenReturn(mockAccountEntity().get());

        // act
        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/{reference_code}", referenceCode)
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse resData = jsonMapper.readValue(mvcResult.getResponse().getContentAsString(), ApiResponse.class);

        // assert
        assertNotNull(resData.getData());
    }

    @Test
    void test_get_account_expect_http_status_500() throws Exception {
        // arrange
        String referenceCode = "201910131234";
        String accessToken = getBearerToken(CLIENT_ID, CLIENT_SECRET);

        when(accountService.findAccount(eq(referenceCode))).thenThrow(AccountNotFoundException.class);

        // act
        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/{reference_code}", referenceCode)
                .header("Authorization", accessToken))
                .andExpect(status().is5xxServerError())
                .andReturn();

        // assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), mvcResult.getResponse().getStatus());
        assertNotNull(mvcResult.getResponse().getContentAsString());
    }

    private String getBearerToken(String clientId, String clientSecret) throws Exception {
        return "Bearer " + getAccessToken(clientId, clientSecret);
    }

    private String getAccessToken(String clientId, String clientSecret) throws Exception {
        ResultActions result = mockMvc.perform(post("/oauth/token")
                .param("grant_type", "client_credentials")
                .param("scope", "openid")
                .with(httpBasic(clientId, clientSecret))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();

        return (String) jsonMapper.readValue(resultString, Map.class).get("access_token");
    }
}
