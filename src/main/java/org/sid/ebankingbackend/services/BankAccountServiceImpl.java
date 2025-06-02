package org.sid.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.CustomerDTO;
import org.sid.ebankingbackend.entities.*;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.exceptions.BankAccountNotSufficientException;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.mappers.BankAccountMapperImpl;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl  implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
private BankAccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer=  customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer= customerRepository.findById(customerId).orElse(null);
        if(customer == null)
            throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount= new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount=bankAccountRepository.save(currentAccount);

        return savedBankAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer= customerRepository.findById(customerId).orElse(null);
        if(customer == null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount= new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount=bankAccountRepository.save(savingAccount);

        return savedBankAccount;    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
         List<CustomerDTO> customerDTOS=customers.stream()
                 .map(customer->dtoMapper.fromCustomer(customer))
                 .collect(Collectors.toList());
        /*
        List<CustomerDTO> customerDTOS = new ArrayList<>();

        for(Customer customer : customers) {
            CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }
        */

        return customerDTOS;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(
                ()->new BankAccountNotFoundException("BankAccount not found")
        );
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BankAccountNotSufficientException {
        BankAccount bankAccount=getBankAccount(accountId);
        if(bankAccount.getBalance() < amount)
            throw new BankAccountNotSufficientException("Balance not sufficient");
      AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=getBankAccount(accountId);

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BankAccountNotSufficientException {
        debit(accountIdSource,amount,"Transfer to"+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+accountIdSource );
    }


   @Override
   public List<BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
  }
@Override
public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
       Customer customer =   customerRepository.findById(customerId)
                .orElseThrow(()->new CustomerNotFoundException("Customer Not found"));
      return dtoMapper.fromCustomer(customer);
    }


}
