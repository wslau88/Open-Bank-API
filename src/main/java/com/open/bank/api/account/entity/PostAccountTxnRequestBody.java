package com.open.bank.api.account.entity;

import java.math.BigDecimal;

public class PostAccountTxnRequestBody {
	private String fromAccount;
	private String toAccount;
    private BigDecimal txnAmount;
    private String currency;
//    private String accountOp;
    
    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }
    
    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }
    
    public BigDecimal getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(BigDecimal txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

//    public String getAccountOp() {
//        return accountOp;
//    }
//
//    public void setAccountOp(String accountOp) {
//        this.accountOp = accountOp;
//    }
}
