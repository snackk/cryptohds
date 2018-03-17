package com.sec.cryptohds.web.rest;

import com.sec.cryptohds.service.exceptions.LedgerAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sec.cryptohds.service.LedgerService;
import com.sec.cryptohds.service.dto.LedgerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * @throws LedgerAlreadyExistsException is throw.
     */
    @PostMapping("/ledgers")
    public ResponseEntity<?> createLedger(@Valid @RequestBody LedgerDTO ledgerDTO) throws LedgerAlreadyExistsException {
        log.debug("REST request to create Ledger : {}", ledgerDTO);

        if (ledgerService.existsLedger(ledgerDTO)) {
            throw new LedgerAlreadyExistsException(ledgerDTO.getPublicKey());
        } else {
            ledgerService.registerLedger(ledgerDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
