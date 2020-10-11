package org.mickael.librarymsloan.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "library-ms-reservation", url = "localhost:8300")
public interface FeignReservationProxy {


    @GetMapping("/api/reservations/customer/{customerId}/book/{bookId}/")
    boolean checkIfReservationExist(@PathVariable("customerId") Integer customerId, @PathVariable("bookId") Integer bookId, @RequestHeader("Authorization") String accessToken);

    @DeleteMapping("/api/reservations/customer/{customerId}/book/{bookId}/")
    void deleteReservationAfterLoan(@PathVariable("customerId") Integer customerId, @PathVariable("bookId") Integer bookId, @RequestHeader("Authorization") String accessToken);


    @PutMapping("/api/reservations/book/{bookId}")
    void updateReservation(@PathVariable("bookId") Integer bookId, @RequestHeader("Authorization") String accessToken);

    @PutMapping("/api/reservations/book/{bookId}/refresh")
    void updateDateReservation(@PathVariable("bookId") Integer bookId, @RequestHeader("Authorization") String accessToken);

}
