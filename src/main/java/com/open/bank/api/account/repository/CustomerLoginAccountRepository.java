package com.open.bank.api.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.open.bank.api.account.entity.CustomerLoginAccount;

public interface CustomerLoginAccountRepository extends JpaRepository<CustomerLoginAccount, String> {
	
	@Query("SELECT cla FROM CustomerLoginAccount cla WHERE cla.username = :username")
	CustomerLoginAccount findByUsername(String username);
	
	@Query(nativeQuery = true, value = "SELECT get_hashed_request_password AS hashed_password FROM GET_HASHED_REQUEST_PASSWORD(:in_username,:in_password)")
	String getHashedRequestPassword(@Param("in_username") String inputUsername, @Param("in_password") String inputPassword);
	
}
