package org.sid.ebankinbackend.repositories;

import org.sid.ebankinbackend.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}