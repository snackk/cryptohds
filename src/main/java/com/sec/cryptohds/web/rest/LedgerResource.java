package com.sec.cryptohds.web.rest;

import com.sec.cryptohds.security.ServerKeyStore;
import com.sec.cryptohds.service.exceptions.CryptohdsException;
import com.sec.cryptohds.service.exceptions.LedgerAlreadyExistsException;
import com.sec.cryptohds.service.exceptions.LedgerDoesNotExistException;

import com.sec.cryptohds.service.exceptions.SecurityValidationException;
import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.envelope.Message;
import com.sec.cryptohdslibrary.service.dto.LedgerBalanceDTO;
import com.sec.cryptohdslibrary.service.dto.LedgerDTO;
import com.sec.cryptohdslibrary.service.dto.OperationDTO;
import com.sec.cryptohdslibrary.service.dto.OperationListDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sec.cryptohds.service.LedgerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class LedgerResource {

    private final Logger log = LoggerFactory.getLogger(LedgerResource.class);

    private final LedgerService ledgerService;

    private final ServerKeyStore serverKeyStore;

    public LedgerResource(LedgerService ledgerService,
                          ServerKeyStore serverKeyStore) {
        this.ledgerService = ledgerService;
        this.serverKeyStore = serverKeyStore;
    }

    /**
     * POST  /ledgers  : Creates a new ledger.
     * <p>
     * Creates a new ledger if not already used.
     *
     * @param envelope the ledger to create
     * @return the ResponseEntity with status 204 (Created) or with status 500 (Bad Request) if
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws LedgerAlreadyExistsException
     */
    @PostMapping("/ledgers")
    public ResponseEntity<?> createLedger(@Valid @RequestBody Envelope envelope) throws CryptohdsException, ClassNotFoundException, IOException {
        log.debug("REST request to create Ledger : {}", envelope);

        Message message = envelope.decipherEnvelope(this.serverKeyStore.getKeyStore());
        if (!message.verifyMessageSignature(envelope.getClientPublicKey())) {
            throw new SecurityValidationException(envelope.getClientPublicKey());
        }
        LedgerDTO ledgerDTO = (LedgerDTO) message.getContent();

        if (ledgerService.existsLedger(ledgerDTO.getPublicKey())) {
            throw new LedgerAlreadyExistsException(ledgerDTO.getPublicKey());
        } else {
            ledgerService.registerLedger(ledgerDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
//    @PostMapping("/ledgers")
//    public ResponseEntity<?> createLedger(@Valid @RequestBody LedgerDTO ledgerDTO) throws LedgerAlreadyExistsException {
//        log.debug("REST request to create Ledger : {}", ledgerDTO);
//
//        if (ledgerService.existsLedger(ledgerDTO.getPublicKey())) {
//            throw new LedgerAlreadyExistsException(ledgerDTO.getPublicKey());
//        } else {
//            ledgerService.registerLedger(ledgerDTO);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//    }
    

    /**
     * GET  /ledger/balance  : Returns balance of ledger.
     * <p>
     * Returns balance of ledger if it exists.
     *
     * @param publicKey the public key of the ledger
     * @return the ResponseEntity with status 200 and the balance on the body or with status 500 (Bad Request) if
     * @throws LedgerDoesNotExistException
     * @throws IOException 
     */
    @GetMapping("/ledger/balance")
    public ResponseEntity<?> checkBalance(@RequestParam(value = "publicKey") String publicKey) throws LedgerDoesNotExistException, IOException {
        log.debug("REST request to check Ledger's balance : {}", publicKey);

        if (!ledgerService.existsLedger(publicKey)) {
            throw new LedgerDoesNotExistException(publicKey);
        } else {
        	LedgerBalanceDTO ldto = ledgerService.getBalanceFromLedger(publicKey);
        	Message message = new Message(ldto,this.serverKeyStore.getKeyStore());
        	Envelope env = new Envelope();
        	env.cipherEnvelope(message, publicKey);
        	
            return new ResponseEntity<>(env, HttpStatus.OK);
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
     * @throws IOException 
     */
    @GetMapping("/ledger/audit")
    public ResponseEntity<?> audit(@RequestParam(value = "publicKey") String publicKey) throws LedgerDoesNotExistException, IOException {
        log.debug("REST request to check Ledger's operations : {}", publicKey);

        if (!ledgerService.existsLedger(publicKey)) {
            throw new LedgerDoesNotExistException(publicKey);
        } else {

        		OperationListDTO op = ledgerService.getOperationsDTOFromLedger(publicKey);
            	Message message = new Message(op,this.serverKeyStore.getKeyStore());
            	Envelope env = new Envelope();
            	env.cipherEnvelope(message, publicKey);
            	return new ResponseEntity<>(env, HttpStatus.OK);
        }	
    }
}
  
    
    
    
    
  
