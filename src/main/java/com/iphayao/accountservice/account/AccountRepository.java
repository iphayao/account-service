package com.iphayao.accountservice.account;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Integer> {
    Optional<Account> findByReferenceCode(String referenceCode);
}
