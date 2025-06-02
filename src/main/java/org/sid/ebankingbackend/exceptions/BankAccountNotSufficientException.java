package org.sid.ebankingbackend.exceptions;

public class BankAccountNotSufficientException extends Exception {
    public BankAccountNotSufficientException(String message) {
        super(message);
    }
}
