package com.iphayao.accountservice.account;

import java.util.Optional;

public class TestHelper {

    public static AccountDto mockAccountDto() {
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

    public static Optional<Account> mockAccountEntity() {
        AccountDto dto = mockAccountDto();
        Account account = new Account();
        account.setUsername(dto.getUsername());
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setPhoneNumber(dto.getPhoneNumber());
        account.setAddress(dto.getAddress());
        account.setEmail(dto.getEmail());
        account.setSalary(dto.getSalary());
        account.setMemberType(MemberType.GOLD);
        account.setReferenceCode("201910131234");

        return Optional.of(account);
    }
}
