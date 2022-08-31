package com.open.bank.api.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.open.bank.api.account.entity.AccountBalance;

@Repository
public interface AccountBalanceRepository extends JpaRepository<AccountBalance, String> {
	
	@Query("SELECT ab FROM AccountBalance ab WHERE ab.accountNumber = :accountNumber AND ab.accounts.accountStatusCode = '001'")
	Optional<List<AccountBalance>> findByAccountNumber(String accountNumber);
	
	@Query("SELECT ab FROM AccountBalance ab WHERE ab.accountNumber = :accountNumber AND ab.accounts.accountStatusCode = '001' AND ab.currency = :currency")
	Optional<AccountBalance> findByAccountNumberAndCurrency(String accountNumber, String currency);
	
	@Query("SELECT ab FROM AccountBalance ab WHERE ab.accountNumber = :accountNumber AND ab.accounts.accountStatusCode = '001' AND ab.accounts.customerInfo.username = :username")
	Optional<List<AccountBalance>> findByAccountNumberAndUsername(String accountNumber, String username);
	
	@Query("SELECT ab FROM AccountBalance ab WHERE ab.accountNumber = :accountNumber AND ab.accounts.accountStatusCode = '001' AND ab.currency = :currency and ab.accounts.customerInfo.username = :username")
	Optional<AccountBalance> findByAccountNumberAndCurrencyAndUsername(String accountNumber, String currency, String username);
	
}
