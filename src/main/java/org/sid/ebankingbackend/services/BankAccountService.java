package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.dtos.CustomerDTO;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.exceptions.BankAccountNotSufficientException;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {


    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();
     BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
     void debit(String accountId, double amount,String description) throws BankAccountNotFoundException, BankAccountNotSufficientException;
    void credit(String accountId, double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination,double amount) throws BankAccountNotFoundException, BankAccountNotSufficientException;


    List<BankAccount> bankAccountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
}
