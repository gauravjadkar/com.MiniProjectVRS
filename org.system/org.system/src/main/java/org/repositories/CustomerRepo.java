package org.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.Entities.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Long> {
}
