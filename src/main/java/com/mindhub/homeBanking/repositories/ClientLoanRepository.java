package com.mindhub.homeBanking.repositories;

import com.mindhub.homeBanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientLoanRepository extends JpaRepository<ClientLoan,Long> {
    boolean existsById(Long id);
    ClientLoan findClientLoanById(Long id);
}
