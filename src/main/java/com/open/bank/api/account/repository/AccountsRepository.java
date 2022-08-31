package com.open.bank.api.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.open.bank.api.account.entity.Accounts;

@Repository
public interface AccountsRepository  extends JpaRepository<Accounts, String> {
	
	@Query("SELECT acc FROM Accounts acc WHERE acc.customerInfo.username = :username AND acc.accountStatusCode = '001'")
	Optional<List<Accounts>> findByUsername(String username);
	
	@Query("SELECT acc FROM Accounts acc INNER JOIN acc.accountBalance ab WHERE acc.customerInfo.username = :username AND acc.accountStatusCode = '001' AND ab.currency = :currency")
	Optional<List<Accounts>> findByUsernameAndCurrency(String username, String currency);
	
	
}
