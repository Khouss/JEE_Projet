package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.Customer;

import java.util.List;

public interface BankAccountService {
     Customer saveCustomer(Customer customer);
     BankAccount saveBankAccount(double initialBalance, String type, Long customerId);
     List<Customer> listCustomers();
     BankAccount getBankAccountById(String accountId);
     void debit(String accountId, double amount,String description);
    void credit(String accountId, double amount,String description);
    void transfer(String accountIdSource, String accountIdDestination,double amount);
    


}
