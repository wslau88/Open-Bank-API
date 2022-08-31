package com.open.bank.api.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.open.bank.api.account.entity.CustomerLoginAccount;
import com.open.bank.api.account.repository.CustomerLoginAccountRepository;

@Service
public class CustomerLoginAccountService implements UserDetailsService {
	
    @Autowired
    private CustomerLoginAccountRepository customerLoginAccountRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserBuilder builder = null;
        CustomerLoginAccount user = customerLoginAccountRepository.findByUsername(username);
        if (user==null) {
        	System.out.println("User not found: " + username);
            throw new UsernameNotFoundException(username);
        }else{
        	String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        	builder = User.withUsername(username);
            builder.username(user.getUsername());
            builder.password(hashedPassword);
            builder.roles("CUSTOMER");
        }
        return builder == null ? null : builder.build();
	}
	
	public String getHashedRequestPassword(String inputUsername, String inputPassword) {
		return customerLoginAccountRepository.getHashedRequestPassword(inputUsername, inputPassword);
	}

}
