package org.sid.ebankinbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankinbackend.entities.*;
import org.sid.ebankinbackend.enums.OperationType;
import org.sid.ebankinbackend.exceptions.BalanceNotSufficientException;
import org.sid.ebankinbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankinbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankinbackend.repositories.AccountOperationRepository;
import org.sid.ebankinbackend.repositories.BankAccountRepository;
import org.sid.ebankinbackend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving new customer");
        return customerRepository.save(customer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance,
                                                 double overDraft,
                                                 Long customerId)
            throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);

        return bankAccountRepository.save(currentAccount);
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance,
                                               double interestRate,
                                               Long customerId)
            throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);

        return bankAccountRepository.save(savingAccount);
    }

    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountId)
            throws BankAccountNotFoundException {

        return bankAccountRepository.findById(accountId)
                .orElseThrow(() ->
                        new BankAccountNotFoundException("BankAccount not found"));
    }

    @Override
    public void debit(String accountId,
                      double amount,
                      String description)
            throws BankAccountNotFoundException,
            BalanceNotSufficientException {

        BankAccount bankAccount = getBankAccount(accountId);

        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance not sufficient");
        }

        AccountOperation operation = new AccountOperation();
        operation.setType(OperationType.DEBIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setOperationDate(new Date());
        operation.setBankAccount(bankAccount);

        accountOperationRepository.save(operation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);

        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId,
                       double amount,
                       String description)
            throws BankAccountNotFoundException {

        BankAccount bankAccount = getBankAccount(accountId);

        AccountOperation operation = new AccountOperation();
        operation.setType(OperationType.CREDIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setOperationDate(new Date());
        operation.setBankAccount(bankAccount);

        accountOperationRepository.save(operation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);

        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource,
                         String accountIdDestination,
                         double amount)
            throws BankAccountNotFoundException,
            BalanceNotSufficientException {

        debit(accountIdSource, amount,
                "Transfer to " + accountIdDestination);

        credit(accountIdDestination, amount,
                "Transfer from " + accountIdSource);
    }

    @Override
    public List<BankAccount> bankAccountList() {
        return bankAccountRepository.findAll();
    }



}