package com.mindhub.homeBanking.repositories;

import com.mindhub.homeBanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan,Long> {
    boolean existsByNameIgnoreCase(String name);
}
