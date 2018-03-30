package com.sec.cryptohds.web.rest;

import com.sec.cryptohds.service.exceptions.LedgerAlreadyExistsException;
import com.sec.cryptohds.service.exceptions.LedgerDoesNotExistException;

import com.sec.cryptohdslibrary.service.dto.LedgerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sec.cryptohds.service.LedgerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class LedgerResource {

    private final Logger log = LoggerFactory.getLogger(LedgerResource.class);

    private final LedgerService ledgerService;

    public LedgerResource(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    /**
     * POST  /ledgers  : Creates a new ledger.
     * <p>
     * Creates a new ledger if not already used.
     *
     * @param ledgerDTO the ledger to create
     * @return the ResponseEntity with status 204 (Created) or with status 500 (Bad Request) if
     * @throws LedgerAlreadyExistsException
     */
    @PostMapping("/ledgers")
    public ResponseEntity<?> createLedger(@Valid @RequestBody LedgerDTO ledgerDTO) throws LedgerAlreadyExistsException {
        log.debug("REST request to create Ledger : {}", ledgerDTO);

        if (ledgerService.existsLedger(ledgerDTO.getPublicKey())) {
            throw new LedgerAlreadyExistsException(ledgerDTO.getPublicKey());
        } else {
            ledgerService.registerLedger(ledgerDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    

    /**
     * GET  /ledger/balance  : Returns balance of ledger.
     * <p>
     * Returns balance of ledger if it exists.
     *
     * @param publicKey the public key of the ledger
     * @return the ResponseEntity with status 200 and the balance on the body or with status 500 (Bad Request) if
     * @throws LedgerDoesNotExistException
     */
    @GetMapping("/ledger/balance")
    public ResponseEntity<?> checkBalance(@RequestParam(value = "publicKey") String publicKey) throws LedgerDoesNotExistException {
        log.debug("REST request to check Ledger's balance : {}", publicKey);

        if (!ledgerService.existsLedger(publicKey)) {
            throw new LedgerDoesNotExistException(publicKey);
        } else {
            return new ResponseEntity<>(ledgerService.getBalanceFromLedger(publicKey), HttpStatus.OK);
        }
    }

    /**
     * GET  /ledger/audit  : Return all operations of a ledger.
     * <p>
     * Returns List <OperationDTO> with all operations.
     *
     * @param publicKey the public key of the ledger
     * @return the ResponseEntity with status 200 and the List <OperationDTO> on the body or with status 500 (Bad Request) if
     * @throws LedgerDoesNotExistException
     */
    @GetMapping("/ledger/audit")
    public ResponseEntity<?> audit(@RequestParam(value = "publicKey") String publicKey) throws LedgerDoesNotExistException {
        log.debug("REST request to check Ledger's operations : {}", publicKey);

        if (!ledgerService.existsLedger(publicKey)) {
            throw new LedgerDoesNotExistException(publicKey);
        } else {
            return new ResponseEntity<>(ledgerService.getOperationsDTOFromLedger(publicKey), HttpStatus.OK);
        }
    }
}
  
    
    
    
    
  
