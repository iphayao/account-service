package com.iphayao.accountservice.account;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account accountDtoToAccount(AccountDto dto);
    AccountRespDto accountToAccountRespDto(Account entity);

}
