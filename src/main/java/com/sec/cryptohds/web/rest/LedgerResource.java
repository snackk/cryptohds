package com.sec.cryptohds.web.rest;

import java.io.IOException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sec.cryptohds.security.EnvelopeHandler;
import com.sec.cryptohds.service.LedgerService;
import com.sec.cryptohds.service.exceptions.CryptohdsException;
import com.sec.cryptohds.service.exceptions.LedgerAlreadyExistsException;
import com.sec.cryptohds.service.exceptions.LedgerDoesNotExistException;
import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.envelope.Message;
import com.sec.cryptohdslibrary.service.dto.LedgerBalanceDTO;
import com.sec.cryptohdslibrary.service.dto.LedgerDTO;
import com.sec.cryptohdslibrary.service.dto.OperationListDTO;

@RestController
@RequestMapping("/api")
public class LedgerResource {

    private final Logger log = LoggerFactory.getLogger(LedgerResource.class);

    private final LedgerService ledgerService;

    private final EnvelopeHandler envelopeHandler;

    public LedgerResource(LedgerService ledgerService,
                          EnvelopeHandler envelopeHandler) {
        this.ledgerService = ledgerService;
        this.envelopeHandler = envelopeHandler;
    }

    /**
     * POST  /ledger/update  : Updates the client ledger.
     * <p>
     * Returns the updated sequence number
     *
     * @param envelope that contains ciphered message.
     * @return the ResponseEntity with status 204 (Created) or with status 500 (Bad Request)
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws LedgerAlreadyExistsException
     */
    @PostMapping("/ledger/update")
    public ResponseEntity<?> clientUpdateLedger(@Valid @RequestBody Envelope envelope) throws CryptohdsException, ClassNotFoundException, IOException {
        log.debug("REST request to update Client's Ledger version : {}", envelope);

		Message message = this.envelopeHandler.handleIncomeEnvelope(envelope);
		LedgerDTO ledgerDTO = (LedgerDTO) message.getContent();

        this.ledgerService.updateLedgerSeqNumber(ledgerDTO);

        return new ResponseEntity<>(this.envelopeHandler.handleOutcomeEnvelope(ledgerDTO, ledgerDTO.getPublicKey()),
                HttpStatus.OK);
    }

    /**
     * POST  /ledgers  : Creates a new ledger.
     * <p>
     * Creates a new ledger if not already used.
     *
     * @param envelope that contains ciphered message.
     * @return the ResponseEntity with status 204 (Created) or with status 500 (Bad Request)
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws LedgerAlreadyExistsException
     */
    @PostMapping("/ledgers")
    public ResponseEntity<?> createLedger(@Valid @RequestBody Envelope envelope) throws CryptohdsException, ClassNotFoundException, IOException {
        log.debug("REST request to create Ledger : {}", envelope);

		Message message = this.envelopeHandler.handleIncomeEnvelope(envelope);
		LedgerDTO ledgerDTO = (LedgerDTO) message.getContent();

        if (ledgerService.existsLedger(ledgerDTO.getPublicKey())) {
            throw new LedgerAlreadyExistsException(ledgerDTO.getPublicKey());
        } else {
            ledgerService.registerLedger(ledgerDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * POST  /ledger/balance  : Returns balance of ledger.
     * <p>
     * Returns balance of ledger if it exists.
     *
     * @param envelope that contains ciphered message.
     * @return the ResponseEntity with status 204 (Created) or with status 500 (Bad Request)
     * @throws LedgerDoesNotExistException
     * @throws IOException 
     */
    @PostMapping("/ledger/balance")
    public ResponseEntity<?> checkBalance(@Valid @RequestBody Envelope envelope) throws LedgerDoesNotExistException, IOException, ClassNotFoundException {
        log.debug("REST request to check Ledger's balance : {}", envelope);

		Message message = this.envelopeHandler.handleIncomeEnvelope(envelope);
		LedgerDTO ledgerDTO = (LedgerDTO) message.getContent();

        if (!ledgerService.existsLedger(ledgerDTO.getPublicKey())) {
            throw new LedgerDoesNotExistException(ledgerDTO.getPublicKey());
        } else {
        	LedgerBalanceDTO balanceDTO = ledgerService.getBalanceFromLedger(ledgerDTO.getPublicKey());
        	
            return new ResponseEntity<>(this.envelopeHandler.handleOutcomeEnvelope(balanceDTO, ledgerDTO.getPublicKey()),
                    HttpStatus.OK);
        }
    }

    /**
     * POST  /ledger/audit  : Return all operations of a ledger.
     * <p>
     * Returns List <OperationDTO> with all operations.
     *
     * @param envelope that contains ciphered message.
     * @return the ResponseEntity with status 204 (Created) or with status 500 (Bad Request)
     * @throws LedgerDoesNotExistException
     * @throws IOException 
     */
    @PostMapping("/ledger/audit")
    public ResponseEntity<?> audit(@Valid @RequestBody Envelope envelope) throws LedgerDoesNotExistException, IOException, ClassNotFoundException {
        log.debug("REST request to check Ledger's operations : {}", envelope);

		Message message = this.envelopeHandler.handleIncomeEnvelope(envelope);
		LedgerDTO ledgerDTO = (LedgerDTO) message.getContent();

        if (!ledgerService.existsLedger(ledgerDTO.getPublicKey())) {
            throw new LedgerDoesNotExistException(ledgerDTO.getPublicKey());
        } else {
            OperationListDTO operationListDTO = ledgerService.getOperationsDTOFromLedger(ledgerDTO.getPublicKey());

            return new ResponseEntity<>(this.envelopeHandler.handleOutcomeEnvelope(operationListDTO, ledgerDTO.getPublicKey()),
                    HttpStatus.OK);
        }	
    }
}
  
    
    
    
    
  
