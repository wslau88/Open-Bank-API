package com.open.bank.api.account.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.open.bank.api.account.entity.AccountBalance;
import com.open.bank.api.account.entity.Accounts;
import com.open.bank.api.account.entity.FundTransferResponse;
import com.open.bank.api.account.entity.PostAccountTxnRequestBody;
import com.open.bank.api.account.exception.AccountNotFoundException;
import com.open.bank.api.account.exception.BadRequestBodyException;
import com.open.bank.api.account.exception.InvalidTxnAmountException;
import com.open.bank.api.account.repository.AccountBalanceRepository;
import com.open.bank.api.account.repository.AccountsRepository;

@Service
public class AccountBalanceService {
	private static final Logger logger = LoggerFactory.getLogger(AccountBalanceService.class);
	
	@Autowired
	private AccountsRepository accountsRepo;
	
	@Autowired
	private AccountBalanceRepository accountBalanceRepo;
	
	public List<Accounts> getAccountInfo(String username) {
		return accountsRepo.findByUsername(username)
				.orElseThrow(() -> new AccountNotFoundException("Cannot find any account with your token"));
	}
	
	public List<Accounts> getAccountInfoByCurrency(String username, String currency) {
		Optional<List<Accounts>> opAccountList = accountsRepo.findByUsernameAndCurrency(username, currency);
		if(opAccountList.isPresent()) {
			List<Accounts> accountList = opAccountList.get();
			for(Accounts acc : accountList) {
				List<AccountBalance> abList = acc.getAccountBalance();
				List<AccountBalance> filteredAbList = abList.stream().filter(e -> e.getCurrency().equals(currency)).collect(Collectors.toCollection(ArrayList::new));
				acc.setAccountBalance(filteredAbList);
			}
			return accountList;
		} else {
			throw new AccountNotFoundException(String.format("Cannot find any account with currency %s with your token", currency));
		}
//		return accountsRepo.findByUsernameAndCurrency(username, currency).orElseThrow(() -> new AccountNotFoundException(String.format("Cannot find any account with currency %s with your token", currency)));
	}

	private List<AccountBalance> getOneAccountBalanceInternal(String accountNumber) {
		return accountBalanceRepo.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new AccountNotFoundException(String.format("Cannot find the balance for the account %s", accountNumber)));
	}
	
	private AccountBalance getOneAccountBalanceByCurrencyInternal(String accountNumber, String currency) {
		return accountBalanceRepo.findByAccountNumberAndCurrency(accountNumber, currency)
				.orElseThrow(() -> new AccountNotFoundException(String.format("Cannot find the balance for the account %s, currency %s", accountNumber, currency)));
	}
	
	public List<AccountBalance> getOneAccountBalance(String accountNumber, String username) {
		return accountBalanceRepo.findByAccountNumberAndUsername(accountNumber, username)
				.orElseThrow(() -> new AccountNotFoundException(String.format("Cannot find the balance for the account %s with your token", accountNumber)));
	}
	
	public AccountBalance getOneAccountBalanceByCurrency(String accountNumber, String currency, String username) {
		return accountBalanceRepo.findByAccountNumberAndCurrencyAndUsername(accountNumber, currency, username)
				.orElseThrow(() -> new AccountNotFoundException(String.format("Cannot find the balance for the account %s, currency %s with your token", accountNumber, currency)));
	}
	
//	public AccountBalance postAccountOp(String accountNumber, PostAccountTxnRequestBody requestBody, String username) {
//		BigDecimal requestTxnAmount = requestBody.getTxnAmount();
//		String requestCurrency = requestBody.getCurrency();
//		String requestAccountOp = requestBody.getAccountOp();
//		
//		logger.info(String.format("%s, %s, %s, %s", accountNumber, requestTxnAmount, requestCurrency, requestAccountOp));
//		
//		if(!requestAccountOp.equals("DR") && !requestAccountOp.equals("CR")) {
//			throw new AccountOpNotSupportedException("Account operation not supported, should be either 'DR' or 'CR'");
//		}
//		
//		try {
//			AccountBalance ab = getOneAccountBalanceByCurrency(accountNumber, requestCurrency, username);
//			if(requestAccountOp.equals("DR")) {
//				ab.setCurrentBalance(ab.getCurrentBalance().subtract(requestTxnAmount));
//				ab.setAvailableBalance(ab.getAvailableBalance().subtract(requestTxnAmount));
//				ab.setLastUpdateTime(ZonedDateTime.now());
//			} else if(requestAccountOp.equals("CR")) {
//				ab.setCurrentBalance(ab.getCurrentBalance().add(requestTxnAmount));
//				ab.setAvailableBalance(ab.getAvailableBalance().add(requestTxnAmount));
//				ab.setLastUpdateTime(ZonedDateTime.now());
//			}
//			
//			return accountBalanceRepo.saveAndFlush(ab);
//		} catch(AccountNotFoundException ex) {
//			throw(ex);
//		}
//	}
	
	public FundTransferResponse postTransactions(PostAccountTxnRequestBody requestBody, String username) {
		String fromAccount = requestBody.getFromAccount();
		String toAccount = requestBody.getToAccount();
		BigDecimal requestTxnAmount = BigDecimal.ZERO;
		String requestCurrency = requestBody.getCurrency();
		
		// Validate fromAccount, toAccount
		if(fromAccount.equals(toAccount)) {
			throw new BadRequestBodyException("fromAccount and toAccount should be different");
		}
		
		// Validate requestTxnAmount
		try {
			requestTxnAmount = requestBody.getTxnAmount().setScale(2, RoundingMode.DOWN);
			
			if(requestTxnAmount.signum() < 0) {
				throw new InvalidTxnAmountException("txnAmount should not be negative");
			}
			
			if(requestTxnAmount.signum() == 0) {
				throw new InvalidTxnAmountException("txnAmount should not be 0");
			}
		} catch(Exception ex) {
			if(ex.getMessage().length() > 0)
				throw new InvalidTxnAmountException(ex.getMessage());
			else
				throw new InvalidTxnAmountException();
		}
		
		logger.info(String.format("postTransactions - From: %s, To: %s, Amount: %s, Currency: %s", fromAccount, toAccount, requestTxnAmount, requestCurrency));
		
		try {
			AccountBalance fromAB = getOneAccountBalanceByCurrency(fromAccount, requestCurrency, username);
			AccountBalance toAB = getOneAccountBalanceByCurrencyInternal(toAccount, requestCurrency);
			
			if(fromAB.getCurrentBalance().subtract(requestTxnAmount).signum() < 0) {
				throw new InvalidTxnAmountException("From account has insufficient balance");
			}
			
			fromAB.setCurrentBalance(fromAB.getCurrentBalance().subtract(requestTxnAmount));
			fromAB.setAvailableBalance(fromAB.getAvailableBalance().subtract(requestTxnAmount));
			fromAB.setLastUpdateTime(ZonedDateTime.now());
			
			toAB.setCurrentBalance(toAB.getCurrentBalance().add(requestTxnAmount));
			toAB.setAvailableBalance(toAB.getAvailableBalance().add(requestTxnAmount));
			toAB.setLastUpdateTime(ZonedDateTime.now());
			
			AccountBalance newFromAB = accountBalanceRepo.saveAndFlush(fromAB);
			AccountBalance newToAB = accountBalanceRepo.saveAndFlush(toAB);
			
			FundTransferResponse fundTransferResponse = new FundTransferResponse(newFromAB, newToAB);
			return fundTransferResponse;
			
		} catch(AccountNotFoundException ex) {
			throw(ex);
		}
	}
}
