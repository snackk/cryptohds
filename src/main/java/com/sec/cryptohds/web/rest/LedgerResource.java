package com.sec.cryptohds.web.rest;

import com.sec.cryptohds.domain.Ledger;
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
import java.net.URI;
import java.net.URISyntaxException;

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
     * @param ledgerDTO the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ledger, or with status 400 (Bad Request)
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ledgers")
    public ResponseEntity<Ledger> createLedger(@Valid @RequestBody LedgerDTO ledgerDTO) throws URISyntaxException {
        log.debug("REST request to create Ledger : {}", ledgerDTO);

        if (ledgerService.existsLedger(ledgerDTO)) {
            return null;
        } else {
            Ledger newLedger = ledgerService.registerLedger(ledgerDTO);
            return new ResponseEntity<>(newLedger, HttpStatus.OK);
        }
    }
}
