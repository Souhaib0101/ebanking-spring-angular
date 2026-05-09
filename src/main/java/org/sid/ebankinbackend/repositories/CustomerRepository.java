package org.sid.ebankinbackend.repositories;


import org.sid.ebankinbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer,Long> {
}