package org.mickael.librarymsloan.repository;

import org.mickael.librarymsloan.model.Loan;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {

    @Query(value = "SELECT loan FROM Loan loan WHERE loan.customerId = :customerId")
    List<Loan> findAllByCustomerId(@Param("customerId") Integer customerId, Sort sort);

    @Query(value = "SELECT loan FROM Loan loan WHERE " +
                           "(loan.loanStatus = 'En cours' AND loan.endingLoanDate <= current_date ) " +
                           "OR (loan.loanStatus = 'Prolongé' AND loan.extend = true AND loan.extendLoanDate <= current_date) " +
                           "OR (loan.loanStatus = 'En retard')")
    List<Loan> findDelayLoan();

    @Query(value = "SELECT loan FROM Loan loan WHERE " +
                           "(loan.loanStatus = 'En cours' AND loan.endingLoanDate <= current_date) " +
                           "OR (loan.loanStatus = 'Prolongé' AND loan.extend = true AND loan.extendLoanDate <= current_date)")
    List<Loan> findAllForUpdateStatus();

    List<Loan> findAllByBookId(Integer bookId);
}
