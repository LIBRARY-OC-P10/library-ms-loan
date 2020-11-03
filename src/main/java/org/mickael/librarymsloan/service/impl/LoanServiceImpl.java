package org.mickael.librarymsloan.service.impl;


import org.mickael.librarymsloan.exception.LoanNotFoundException;
import org.mickael.librarymsloan.model.Loan;
import org.mickael.librarymsloan.model.enumeration.LoanStatus;
import org.mickael.librarymsloan.repository.LoanRepository;
import org.mickael.librarymsloan.service.contract.LoanServiceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanServiceContract {

    private final LoanRepository loanRepository;
    private static final String NOT_FOUND_MSG = "Loan not found in repository";


    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public List<Loan> findAll() {
        List<Loan> loans = loanRepository.findAll();
        if (loans.isEmpty()){
            throw new LoanNotFoundException(NOT_FOUND_MSG);
        }
        return loans;
    }

    @Override
    public Loan findById(Integer id) {
        Optional<Loan> optionalLoan = loanRepository.findById(id);
        if (!optionalLoan.isPresent()){
            throw new LoanNotFoundException(NOT_FOUND_MSG);
        }
        return optionalLoan.get();
    }

    @Override
    public Loan save(Loan loan) {
        Loan savedLoan = new Loan();
        savedLoan.setLoanStatus(LoanStatus.ONGOING.getLabel());
        savedLoan.setBeginLoanDate(LocalDate.now());
        savedLoan.setEndingLoanDate(LocalDate.now().plusWeeks(4));
        savedLoan.setExtendLoanDate(LocalDate.now().plusWeeks(8));
        savedLoan.setExtend(false);
        savedLoan.setBookId(loan.getBookId());
        savedLoan.setCopyId(loan.getCopyId());
        savedLoan.setCustomerId(loan.getCustomerId());
        return loanRepository.save(savedLoan);
    }

    @Override
    public Loan update(Loan loan) {
        Optional<Loan> optionalLoan = loanRepository.findById(loan.getId());
        if (!optionalLoan.isPresent()){
            throw new LoanNotFoundException(NOT_FOUND_MSG);
        }
        if (loan.getLoanStatus().equalsIgnoreCase(LoanStatus.CLOSED.getLabel())){
            Loan savedLoan = optionalLoan.get();
            savedLoan.setReturnLoanDate(LocalDate.now());
            savedLoan.setLoanStatus(LoanStatus.CLOSED.getLabel());
            return loanRepository.save(savedLoan);
        }
        return loanRepository.save(loan);
    }

    @Override
    public void deleteById(Integer id) {
        loanRepository.deleteById(id);
    }

    @Override
    public List<Loan> findAllByCustomerId(Integer customerId) {
        List<Loan> loans = loanRepository.findAllByCustomerId(customerId, Sort.by("loanStatus"));
        if (loans.isEmpty()){
            throw new LoanNotFoundException(NOT_FOUND_MSG);
        }
        return loans;
    }

    @Override
    public List<Loan> findDelayLoan() {
        List<Loan> loans = loanRepository.findDelayLoan();
        if (loans.isEmpty()){
            throw new LoanNotFoundException(NOT_FOUND_MSG);
        }
        return loans;
    }

    @Override
    public List<LocalDate> findSoonestEndingLoan(Integer bookId) {
        List<Loan> loans = loanRepository.findAllByBookId(bookId);
        List<Loan> ongoingLoans = new ArrayList<>();
        for (Loan loan : loans){
            if (!loan.getLoanStatus().equalsIgnoreCase("rendu")){
                if (loan.isExtend()){
                    loan.setEndingLoanDate(loan.getExtendLoanDate());
                }
                ongoingLoans.add(loan);
            }
        }

        ongoingLoans.sort(Comparator.comparing(Loan::getEndingLoanDate));
        List<LocalDate> localDates = new ArrayList<>();
        for (Loan loan : ongoingLoans){
            localDates.add(loan.getEndingLoanDate());
        }
        return localDates;
    }

    @Override
    public boolean checkIfLoanExistForCustomerIdAndBookId(Integer customerId, Integer bookId) {
       Integer loan = loanRepository.checkIfLoanExistForCustomerIdAndBookId(customerId,bookId);
        return loan != 0;
    }


    @Override
    public Loan extendLoan(Integer id) {
        Optional<Loan> optionalLoan = loanRepository.findById(id);
        if (!optionalLoan.isPresent()){
            throw new LoanNotFoundException(NOT_FOUND_MSG);
        }
        Loan extendLoan = new Loan();
        extendLoan.setId(optionalLoan.get().getId());
        extendLoan.setBeginLoanDate(optionalLoan.get().getBeginLoanDate());
        extendLoan.setEndingLoanDate(optionalLoan.get().getEndingLoanDate());
        extendLoan.setExtendLoanDate(optionalLoan.get().getExtendLoanDate());
        extendLoan.setCustomerId(optionalLoan.get().getCustomerId());
        extendLoan.setCopyId(optionalLoan.get().getCopyId());
        extendLoan.setBookId(optionalLoan.get().getBookId());
        extendLoan.setExtend(true);
        extendLoan.setLoanStatus(LoanStatus.EXTENDED.getLabel());
        return loanRepository.save(extendLoan);
    }

    @Override
    public int updateStatus(){
        List<Loan> loans = loanRepository.findAllForUpdateStatus();
        for (Loan loan : loans){
            if (loan.getEndingLoanDate().compareTo(LocalDate.now()) > 0){
                loan.setLoanStatus(LoanStatus.OUTDATED.getLabel());
            }
        }
        loanRepository.saveAll(loans);
        return loans.size();
    }

    @Override
    public Loan returnLoan(Integer id) {
        Optional<Loan> optionalLoan = loanRepository.findById(id);
        if (!optionalLoan.isPresent()){
            throw new LoanNotFoundException(NOT_FOUND_MSG);
        }
        Loan savedLoan = optionalLoan.get();
        savedLoan.setLoanStatus(LoanStatus.CLOSED.getLabel());
        savedLoan.setReturnLoanDate(LocalDate.now());
        return loanRepository.save(savedLoan);
    }
}
