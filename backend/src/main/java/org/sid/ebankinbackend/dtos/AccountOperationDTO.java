package org.sid.ebankinbackend.dtos;

import lombok.Data;
import org.sid.ebankinbackend.enums.OperationType;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}
