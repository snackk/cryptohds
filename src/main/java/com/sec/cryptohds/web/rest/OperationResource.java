package com.sec.cryptohds.web.rest;

import com.sec.cryptohds.domain.Operation;
import com.sec.cryptohds.service.OperationService;
import com.sec.cryptohds.service.dto.LedgerDTO;
import com.sec.cryptohds.service.dto.OperationDTO;
import com.sec.cryptohds.service.exceptions.CryptohdsException;
import com.sec.cryptohds.service.exceptions.LedgerAlreadyExistsException;
import com.sec.cryptohds.service.exceptions.LedgerDoesNotExistException;

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
     * POST  /operations  : Creates a new operation.
     * <p>
     * Creates a new operations.
     *
     * @param operationDTO the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ledger, or with status 400 (Bad Request)
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/operations")
    public ResponseEntity<Operation> createOperation(@Valid @RequestBody OperationDTO operationDTO) throws CryptohdsException {
        log.debug("REST request to create Operation : {}", operationDTO);

        if (!operationService.getLedgerService().existsLedger(operationDTO.getOrigin())) {
            throw new LedgerDoesNotExistException(operationDTO.getOrigin().getPublicKey());
        } else {
            operationService.createOperation(operationDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


    /**
     * POST  /operations  : Creates a new operation.
     * <p>
     * Creates a new operations.
     *
     * @param operationDTO the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ledger, or with status 400 (Bad Request)
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sendAmount")
    public ResponseEntity<?> sendAmount(@Valid @RequestBody OperationDTO operationDTO) throws CryptohdsException {
        log.debug("REST request to send amount to Ledger : {}", operationDTO);

        if (!operationService.getLedgerService().existsLedger(operationDTO.getOrigin())) {
        	 throw new LedgerDoesNotExistException(operationDTO.getOrigin().getPublicKey());
        } else {
        	operationService.createOperation(operationDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

