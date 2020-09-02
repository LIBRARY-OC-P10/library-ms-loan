package org.mickael.librarymsloan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Loan Not Found")
public class LoanNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public LoanNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
