package com.open.bank.api.account.entity;

public class FundTransferResponse {
	private class UpdatedBalances {
		private AccountBalance fromAccountBalance;
		private AccountBalance toAccountBalance;
		
		public UpdatedBalances(AccountBalance fromAccountBalance, AccountBalance toAccountBalance) {
			setFromAccountBalance(fromAccountBalance);
			setToAccountBalance(toAccountBalance);
		}
		
		public AccountBalance getFromAccountBalance() {
			return this.fromAccountBalance;
		}
		
		public void setFromAccountBalance(AccountBalance fromAccountBalance) {
			this.fromAccountBalance = fromAccountBalance;
		}
		
		public AccountBalance getToAccountBalance() {
			return this.toAccountBalance;
		}
		
		public void setToAccountBalance(AccountBalance toAccountBalance) {
			this.toAccountBalance = toAccountBalance;
		}
	}
	
	private UpdatedBalances updatedBalances;
	
	public FundTransferResponse(AccountBalance fromAccountBalance, AccountBalance toAccountBalance) {
		setUpdatedBalances(fromAccountBalance, toAccountBalance);
	}
	
	public UpdatedBalances getUpdatedBalances() {
		return updatedBalances;
	}
	
	public void setUpdatedBalances(AccountBalance fromAccountBalance, AccountBalance toAccountBalance) {
		this.updatedBalances = new UpdatedBalances(fromAccountBalance, toAccountBalance);
	}
}
