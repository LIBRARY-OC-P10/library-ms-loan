package org.mickael.librarymsloan.controller;

import org.mickael.librarymsloan.exception.LoanNotFoundException;
import org.mickael.librarymsloan.model.Loan;
import org.mickael.librarymsloan.proxy.FeignBookProxy;
import org.mickael.librarymsloan.service.contract.LoanServiceContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@PreAuthorize("isAuthenticated()")
public class LoanRestController {

    private static final Logger logger = LoggerFactory.getLogger(LoanRestController.class);
    private final LoanServiceContract loanServiceContract;
    private final FeignBookProxy feignBookProxy;

    @Autowired
    public LoanRestController(LoanServiceContract loanServiceContract, FeignBookProxy feignBookProxy) {
        this.loanServiceContract = loanServiceContract;
        this.feignBookProxy = feignBookProxy;
    }

    @GetMapping
    public List<Loan> getLoans(){
        try {
            return loanServiceContract.findAll();
        } catch (LoanNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Loans found", ex);
        }
    }

    @GetMapping("/{id}")
    public Loan retrieveLoan(@PathVariable Integer id){
        logger.debug("Get loan by id : " + id);
        try {
            return loanServiceContract.findById(id);
        } catch (LoanNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found", ex);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createNewLoan(@Valid @RequestBody Loan newLoan, @RequestHeader("Authorization") String accessToken){
        if (newLoan == null){
            return ResponseEntity.noContent().build();
        }
        Loan loanSaved = loanServiceContract.save(newLoan);
        feignBookProxy.updateLoanCopy(loanSaved.getCopyId(), accessToken);
        URI location = ServletUriComponentsBuilder
                               .fromCurrentRequest()
                               .path("/{id}")
                               .buildAndExpand(loanSaved.getId()).toUri();
        return ResponseEntity.created(location).body(loanSaved);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Loan updateLoan(@PathVariable Integer id, @RequestBody Loan loan){
        try {
            return loanServiceContract.update(loan);
        } catch (LoanNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide correct Loan ID", ex);
        }
    }
    @PutMapping("/return/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Loan returnLoan(@PathVariable Integer id, @RequestHeader("Authorization") String accessToken){
        try {
            Loan loan = loanServiceContract.returnLoan(id);
            feignBookProxy.updateLoanCopy(loan.getCopyId(), accessToken);

            return loan;
        } catch (LoanNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide correct Loan ID", ex);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable Integer id){
        loanServiceContract.deleteById(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<Loan> findAllByCustomerId(@PathVariable Integer customerId){
        try {
            return loanServiceContract.findAllByCustomerId(customerId);
        } catch (LoanNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Loans found", ex);
        }
    }

    @GetMapping("/extend/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Loan extendLoan(@PathVariable Integer id){
        try{
            Loan loan = loanServiceContract.extendLoan(id);
            return loan;
        } catch (LoanNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide correct Loan ID", ex);
        }
    }

    @GetMapping("/delay")
    public List<Loan> getLoanDelayLoan(){
        return loanServiceContract.findDelayLoan();
    }

    @GetMapping("/update-status")
    public int updateLoanStatus() {
        return loanServiceContract.updateStatus();
    }



}
