package org.sid.ebankinbackend.dtos;

import lombok.Data;
import org.sid.ebankinbackend.enums.AccountStatus;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private String type;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private List<AccountOperationDTO> accountOperationDTOS;
}