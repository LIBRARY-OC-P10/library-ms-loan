package org.mickael.librarymsloan.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "public.loan_loan_id_seq")
    @SequenceGenerator(name = "public.loan_loan_id_seq", sequenceName = "public.loan_loan_id_seq", allocationSize = 1)
    @Column(name = "loan_id")
    private Integer id;

    @Column(name = "begin_loan_date")
    private LocalDate beginLoanDate;

    @Column(name = "ending_loan_date")
    private LocalDate endingLoanDate;

    @Column(name = "extend_loan_date")
    private LocalDate extendLoanDate;

    @Column(name = "return_loan_date")
    private LocalDate returnLoanDate;

    @Column(name = "extend")
    private boolean extend;

    @Column(name = "loan_status")
    private String loanStatus;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "copy_id")
    private Integer copyId;

    @Column(name = "book_id")
    private Integer bookId;
}
