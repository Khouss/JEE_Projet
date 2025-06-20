package org.sid.ebankingbackend.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.CustomerDTO;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
private BankAccountService bankAccountService;
@GetMapping("/customers")
public List<CustomerDTO> customers(){
    return bankAccountService.listCustomers();
}
    @GetMapping("/customers/{id}")
 public CustomerDTO getCustomer(@PathVariable(name="id") Long customerId) throws CustomerNotFoundException {
    return bankAccountService.getCustomer(customerId);

 }
 public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
   return  bankAccountService.saveCustomer(customerDTO);
 }
}
