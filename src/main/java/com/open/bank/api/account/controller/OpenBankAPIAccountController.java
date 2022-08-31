package com.open.bank.api.account.controller;

import static com.open.bank.api.constant.SecurityConstants.HEADER_STRING;
import static com.open.bank.api.constant.SecurityConstants.TOKEN_PREFIX;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.bank.api.account.entity.AccountBalance;
import com.open.bank.api.account.entity.AccountBalanceAllCurrency;
import com.open.bank.api.account.entity.CurrencyBalance;
import com.open.bank.api.account.entity.LoginAccountRequestBody;
import com.open.bank.api.account.entity.PostAccountTxnRequestBody;
import com.open.bank.api.account.exception.AccountNotFoundException;
import com.open.bank.api.account.exception.BadRequestBodyException;
import com.open.bank.api.account.exception.InvalidTxnAmountException;
import com.open.bank.api.account.service.AccountBalanceService;
import com.open.bank.api.account.service.CustomerLoginAccountService;
import com.open.bank.api.auth.AESDecryptUtil;
import com.open.bank.api.auth.JWTUtil;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OpenBankAPIAccountController {
	private static final Logger logger = LoggerFactory.getLogger(OpenBankAPIAccountController.class);
	
	@Autowired
	private AccountBalanceService accountBalanceService;
	
	@Autowired
	private CustomerLoginAccountService customerLoginAccountService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AESDecryptUtil aesDecryptUtil;
	
	@PostMapping("/auth/registry/user/login")
	public ResponseEntity<Object> getUserLoginJWT(@RequestBody LoginAccountRequestBody body){
		logger.info("POST /auth/registry/user/login called");
		try {
            UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
        		body.getUsername(),
        		customerLoginAccountService.getHashedRequestPassword(body.getUsername(), aesDecryptUtil.decrypt(body.getPassword()))
    		);
            
            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(body.getUsername());

            return ResponseEntity.ok().body(Collections.singletonMap("jwt-token", token));
        } catch (AuthenticationException ex) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Not authenticated"));
        } catch (Exception ex) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Not authenticated"));
        }
	}
	
	@GetMapping("/accountInfoEnquiry")
	public ResponseEntity<Object> getAccountInfo(@RequestHeader(value=HEADER_STRING) String jwt, @RequestParam(value = "currency", required = false) String paramCurrency){
		logger.info("getAccountInfo called");
		
		try {
			String username = jwtUtil.validateTokenAndRetrieveSubject(jwt.replace(TOKEN_PREFIX, ""));
			
			if(paramCurrency == null) {
				return ResponseEntity.ok().body(accountBalanceService.getAccountInfo(username));
			} else {
				return ResponseEntity.ok().body(accountBalanceService.getAccountInfoByCurrency(username, paramCurrency));
			}
		} catch(AccountNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", ex.getMessage()));
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Internal server error"));
		}
	}
		

	@GetMapping("/accountBalEnquiry/{paramAccountNumber}")
	public ResponseEntity<Object> getOneAccountBalance(@RequestHeader(value=HEADER_STRING) String jwt, @PathVariable String paramAccountNumber, @RequestParam(value = "currency", required = false) String paramCurrency){
		logger.info(String.format("GET /accountBalEnquiry/%s called", paramAccountNumber));
		
		try {
			String username = jwtUtil.validateTokenAndRetrieveSubject(jwt.replace(TOKEN_PREFIX, ""));
			
			if(paramCurrency == null) {
				AccountBalanceAllCurrency response = new AccountBalanceAllCurrency();
				List<AccountBalance> accountBalances = accountBalanceService.getOneAccountBalance(paramAccountNumber, username);
				
				if(!accountBalances.isEmpty()) {
					List<CurrencyBalance> currencyBalances = new ArrayList<CurrencyBalance>();
					response.setAccountNumber(accountBalances.get(0).getAccountNumber());
					
					ZonedDateTime maxLastUpdateTime = null;
					
					for(AccountBalance ab : accountBalances) {
						String currency = ab.getCurrency();
						BigDecimal availableBalance = ab.getAvailableBalance();
						currencyBalances.add(new CurrencyBalance(availableBalance, currency));
						
						ZonedDateTime lastUpdateTime = ab.getLastUpdateTime();
						if(maxLastUpdateTime == null || maxLastUpdateTime.compareTo(lastUpdateTime) < 0) {
							maxLastUpdateTime = lastUpdateTime;
						}
					}
					response.setBalances(currencyBalances);
					
					response.setLastUpdateTime(maxLastUpdateTime);
				}
				
				return ResponseEntity.ok().body(response);
			} else {
				return ResponseEntity.ok().body(accountBalanceService.getOneAccountBalanceByCurrency(paramAccountNumber, paramCurrency, username));
			}
		} catch(AccountNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", ex.getMessage()));
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Internal server error"));
		}
	}
		
//	@PostMapping("/accountTxn/{paramAccountNumber}")
//	public ResponseEntity<Object> postAccountTxn(@RequestHeader(value=HEADER_STRING) String jwt, @PathVariable String paramAccountNumber, @RequestBody PostAccountTxnRequestBody requestBody){
//		try {
//			String username = jwtUtil.validateTokenAndRetrieveSubject(jwt.replace(TOKEN_PREFIX, ""));
//			
//			return ResponseEntity.ok().body(accountBalanceService.postAccountOp(paramAccountNumber, requestBody, username));
//		} catch(AccountOpNotSupportedException ex) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", ex.getMessage()));
//		} catch(AccountNotFoundException ex) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", ex.getMessage()));
//		} catch(Exception ex) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Internal server error"));
//		}
//	}
	
	@PostMapping("/fundTransfer")
	public ResponseEntity<Object> postFundTransfer(@RequestHeader(value=HEADER_STRING) String jwt, @RequestBody PostAccountTxnRequestBody requestBody){
		logger.info("POST /fundTransfer called");
		
		try {
			String username = jwtUtil.validateTokenAndRetrieveSubject(jwt.replace(TOKEN_PREFIX, ""));
			
			return ResponseEntity.ok().body(accountBalanceService.postTransactions(requestBody, username));
		} catch(BadRequestBodyException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", ex.getMessage()));
		} catch(AccountNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", ex.getMessage()));
		} catch(InvalidTxnAmountException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", ex.getMessage()));
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Internal server error"));
		}
	}
	
}
