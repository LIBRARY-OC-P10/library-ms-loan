package org.mickael.librarymsloan.service.contract;

import org.mickael.librarymsloan.exception.LoanNotFoundException;
import org.mickael.librarymsloan.model.Loan;

import java.util.List;

public interface LoanServiceContract {

    List<Loan> findAll() throws LoanNotFoundException;

    Loan findById(Integer id) throws LoanNotFoundException;

    Loan save(Loan loan);

    Loan update(Loan loan) throws LoanNotFoundException;

    Loan returnLoan(Integer id) throws LoanNotFoundException;

    int updateStatus();

    void deleteById(Integer id);

    Loan extendLoan(Integer id) throws LoanNotFoundException;

    List<Loan> findAllByCustomerId(Integer id) throws LoanNotFoundException;

    List<Loan> findDelayLoan();

}
