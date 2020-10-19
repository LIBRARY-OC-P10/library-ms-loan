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
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

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
        //List<Loan> loans= new ArrayList<>();
        given(loanRepository.findAll()).willReturn(Collections.emptyList());
        //When
        //when(loanRepository.findAll()).thenReturn(loans);

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
    void itShouldSaveLoan(){
        //Given
        Integer customerId = 2;
        Integer bookId = 18;
        Integer copyId = 73;
        Loan loan = new Loan();
        loan.setBookId(bookId);
        loan.setCopyId(copyId);
        loan.setCustomerId(customerId);

        //When
        loanServiceUnderTest.save(loan);

        //Then
        then(loanRepository).should().save(loanArgumentCaptor.capture());
        Loan loanArgumentCaptorValue = loanArgumentCaptor.getValue();

        assertThat(loanArgumentCaptorValue.getBookId()).isEqualTo(loan.getBookId());
        assertThat(loanArgumentCaptorValue.getCopyId()).isEqualTo(loan.getCopyId());
        assertThat(loanArgumentCaptorValue.getCustomerId()).isEqualTo(loan.getCustomerId());
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

    @Test
    void itShouldReturnExceptionWhenUpdateLoan(){
        //Given
        Integer loanId = 10;
        Integer customerId = 2;
        Integer bookId = 18;
        Integer copyId = 73;

        Loan loan = new Loan(loanId, LocalDate.now(),LocalDate.now().plusWeeks(4),LocalDate.now().plusWeeks(8),null,false,LoanStatus.ONGOING.getLabel(),customerId,copyId,bookId);

        given(loanRepository.findById(loanId)).willReturn(Optional.empty());

        //When

        //Then
        assertThatThrownBy(() ->loanServiceUnderTest.update(loan))
                .isInstanceOf(LoanNotFoundException.class)
                .hasMessageContaining(NOT_FOUND_MSG);
    }

    @Test
    void itShouldSaveUpdateClosedLoan(){
        //Given
        Integer loanId = 10;
        Integer customerId = 2;
        Integer bookId = 18;
        Integer copyId = 73;

        Loan loan = new Loan(loanId, LocalDate.now(),LocalDate.now().plusWeeks(4),LocalDate.now().plusWeeks(8),null,false,LoanStatus.CLOSED.getLabel(),customerId,copyId,bookId);

        given(loanRepository.findById(loanId)).willReturn(Optional.of(loan));

        //When
        loanServiceUnderTest.update(loan);

        //Then
        then(loanRepository).should().save(loanArgumentCaptor.capture());
        Loan loanArgumentCaptorValue = loanArgumentCaptor.getValue();
        assertThat(loanArgumentCaptorValue).isEqualTo(loan);
    }

    @Test
    void itShoudReturnAnEmptyListOfLoanByCustomerId(){
        //Given
        Integer customerId = 2;
                given(loanRepository.findAllByCustomerId(customerId, Sort.by("loanStatus"))).willReturn(Collections.emptyList());
        //When

        //Then
        assertThatThrownBy(() ->loanServiceUnderTest.findAllByCustomerId(customerId))
                .isInstanceOf(LoanNotFoundException.class)
                .hasMessageContaining(NOT_FOUND_MSG);
    }

    @Test
    void itShoudReturnAListOfLoanByCustomerId(){
        //Given
        Integer customerId = 2;
        Loan loan = new Loan();
        given(loanRepository.findAllByCustomerId(customerId, Sort.by("loanStatus"))).willReturn(Collections.singletonList(loan));

        //When


        //Then
        assertThat(loanServiceUnderTest.findAllByCustomerId(customerId)).isNotEmpty();
    }

    @Test
    void itShoudReturnAnEmptyListOfDelayLoan(){
        //Given
        given(loanRepository.findDelayLoan()).willReturn(Collections.emptyList());
        //When

        //Then
        assertThatThrownBy(() ->loanServiceUnderTest.findDelayLoan())
                .isInstanceOf(LoanNotFoundException.class)
                .hasMessageContaining(NOT_FOUND_MSG);
    }

    @Test
    void itShoudReturnAListOfDelayLoan(){
        //Given
        Loan loan = new Loan();
        given(loanRepository.findDelayLoan()).willReturn(Collections.singletonList(loan));

        //When

        //Then
        assertThat(loanServiceUnderTest.findDelayLoan()).isNotEmpty();
    }

    @Test
    void itShouldReturnListOfLocalDateOfSoonestEndingLoan(){
        //Given
        Integer bookId = 3;
        Loan loan1 = new Loan(1, LocalDate.now(),LocalDate.now().plusWeeks(4),
                LocalDate.now().plusWeeks(8),null,false,
                LoanStatus.ONGOING.getLabel(),2,51,bookId);
        Loan loan2 = new Loan(2, LocalDate.now(),LocalDate.now().plusWeeks(4),
                LocalDate.now().plusWeeks(8),null,false,
                LoanStatus.ONGOING.getLabel(),3,52,bookId);
        Loan loan3 = new Loan(3, LocalDate.now(),LocalDate.now().plusWeeks(4),
                LocalDate.now().plusWeeks(8),null,false,
                LoanStatus.ONGOING.getLabel(),4,53,bookId);
        Loan loan4 = new Loan(4, LocalDate.now(),LocalDate.now().plusWeeks(4),
                LocalDate.now().plusWeeks(8),null,true,
                LoanStatus.ONGOING.getLabel(),5,54,bookId);
        Loan loan5 = new Loan(5, LocalDate.now(),LocalDate.now().plusWeeks(4),
                LocalDate.now().plusWeeks(8),null,false,
                LoanStatus.ONGOING.getLabel(),6,55,bookId);
        List<Loan> loans = new ArrayList<>();
        loans.add(loan1);
        loans.add(loan2);
        loans.add(loan3);
        loans.add(loan4);
        loans.add(loan5);

        //when
        when(loanRepository.findAllByBookId(bookId)).thenReturn(loans);

        //Then
        assertThat(loanServiceUnderTest.findSoonestEndingLoan(bookId)).isNotEmpty();

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
    @DisplayName("Should return false if a loan doesn't exist for a customer and book id")
    void itShouldReturnTrueIfLoanNotExistForCustomerAndBookId(){
        //Given
        Integer bookId = 2;
        Integer customerId = 2;

        given(loanRepository.checkIfLoanExistForCustomerIdAndBookId(customerId,bookId)).willReturn(0);

        //When


        //Then
        assertThat(loanServiceUnderTest.checkIfLoanExistForCustomerIdAndBookId(customerId,bookId)).isFalse();
    }

    @Test
    void itShouldReturnExceptionWhenExtendingNotExistingLoan(){
        //Given
        Integer loanId = 1;
        given(loanRepository.findById(loanId)).willReturn(Optional.empty());

        //When


        //Then
        assertThatThrownBy(() ->loanServiceUnderTest.extendLoan(loanId))
                .isInstanceOf(LoanNotFoundException.class)
                .hasMessageContaining(NOT_FOUND_MSG);
    }
}
