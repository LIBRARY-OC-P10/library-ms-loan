package org.mickael.librarymsloan.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mickael.librarymsloan.exception.LoanNotFoundException;
import org.mickael.librarymsloan.model.Loan;
import org.mickael.librarymsloan.model.enumeration.LoanStatus;
import org.mickael.librarymsloan.repository.LoanRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Captor
    private ArgumentCaptor<Loan> loanArgumentCaptor;

    private static final String NOT_FOUND_MSG = "Loan not found in repository";

    private LoanServiceImpl loanServiceUnderTest;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        loanServiceUnderTest = new LoanServiceImpl(loanRepository);
    }

    @Test
    void itShoudReturnAnEmptyListOfLoan(){
        //Given
        given(loanRepository.findAll()).willReturn(Collections.emptyList());

        //When


        //Then
        assertThatThrownBy(() ->loanServiceUnderTest.findAll())
                .isInstanceOf(LoanNotFoundException.class)
                .hasMessageContaining(NOT_FOUND_MSG);
    }

    @Test
    void itShoudReturnAListOfLoan(){
        //Given
        Loan loan = new Loan();
        given(loanRepository.findAll()).willReturn(Collections.singletonList(loan));

        //When


        //Then
        assertThat(loanServiceUnderTest.findAll()).isNotEmpty();
    }

    @Test
    @DisplayName("True if return one loan")
    void itShoudReturnOneLoan(){
        //Given
        Integer loanId = 1;
        Loan loan = new Loan();
        given(loanRepository.findById(loanId)).willReturn(Optional.of(loan));

        //When


        //Then
        assertThat(loanServiceUnderTest.findById(loanId)).isInstanceOf(Loan.class);
    }

    @Test
    void itShoudReturnExceptionIfLoanNotExist(){
        //Given
        Integer loanId = 1;
        given(loanRepository.findById(loanId)).willReturn(Optional.empty());

        //When


        //Then
        assertThatThrownBy(() ->loanServiceUnderTest.findById(loanId))
                .isInstanceOf(LoanNotFoundException.class)
                .hasMessageContaining(NOT_FOUND_MSG);
    }

    @Test
    @DisplayName("Should return true if a loan exist for a customer and book id")
    void itShouldReturnTrueIfLoanExistForCustomerAndBookId(){
        //Given
        Integer bookId = 2;
        Integer customerId = 2;

        given(loanRepository.checkIfLoanExistForCustomerIdAndBookId(customerId,bookId)).willReturn(1);

        //When


        //Then
        assertThat(loanServiceUnderTest.checkIfLoanExistForCustomerIdAndBookId(customerId,bookId)).isTrue();
    }

    @Test
    void itShouldSaveUpdateLoan(){
        //Given
        Integer loanId = 10;
        Integer customerId = 2;
        Integer bookId = 18;
        Integer copyId = 73;

        Loan loan = new Loan(loanId, LocalDate.now(),LocalDate.now().plusWeeks(4),LocalDate.now().plusWeeks(8),null,false,LoanStatus.ONGOING.getLabel(),customerId,copyId,bookId);

        given(loanRepository.findById(loanId)).willReturn(Optional.of(loan));

        //When
        loanServiceUnderTest.update(loan);

        //Then
        then(loanRepository).should().save(loanArgumentCaptor.capture());
        Loan loanArgumentCaptorValue = loanArgumentCaptor.getValue();
        assertThat(loanArgumentCaptorValue).isEqualTo(loan);
    }


}
