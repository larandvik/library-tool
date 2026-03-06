package com.lav.libtool.exceptions;

public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException(Long loanId) {
        super("Loan with id " + loanId + " not found");
    }

}
