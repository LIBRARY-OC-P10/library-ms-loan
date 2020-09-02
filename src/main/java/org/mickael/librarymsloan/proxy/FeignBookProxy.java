package org.mickael.librarymsloan.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "library-ms-book", url = "localhost:8100")//il faut modifier les uri avec le nom du ms à appeler
//@RibbonClient(name = "micro service à appeler")
public interface FeignBookProxy {


    @PutMapping("/api/copies/book/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateLoanCopy(@PathVariable("id") Integer id, @RequestHeader("Authorization") String accessToken);



}
