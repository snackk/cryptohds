package com.sec.cryptohds.web.rest;

import com.sec.cryptohds.service.OperationService;
import com.sec.cryptohds.service.exceptions.CryptohdsException;

import com.sec.cryptohdslibrary.service.dto.OperationDTO;
import com.sec.cryptohdslibrary.service.dto.ReceiveOperationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class OperationResource {

    private final Logger log = LoggerFactory.getLogger(OperationResource.class);

    private final OperationService operationService;

    public OperationResource(OperationService operationService) {
        this.operationService = operationService;
    }

    /**
     * POST  /operation/send  : Sends an amount from destination to source.
     * <p>
     * Sends an amount from destination to source.
     *
     * @param operationDTO the operation to create
     * @return the ResponseEntity with status 201 (Created) and with body the operation, or with status 400 (Bad Request)
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/operation/send")
    public ResponseEntity<?> sendOperation(@Valid @RequestBody OperationDTO operationDTO) throws CryptohdsException {
        log.debug("REST request to send Operation : {}", operationDTO);

        operationService.createOperation(operationDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    /**
     * POST  /operation/receive  : Receives an amount from source.
     * <p>
     * Receives an amount from source.
     *
     * @param receiveOperationDTO the operation to receive
     * @return the ResponseEntity with status 201 (Received) and with body the operation, or with status 400 (Bad Request)
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/operation/receive")
    public ResponseEntity<?> receiveOperation(@Valid @RequestBody ReceiveOperationDTO receiveOperationDTO) throws CryptohdsException {
        log.debug("REST request to receive Operation : {}", receiveOperationDTO);

        operationService.receiveOperation(receiveOperationDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

