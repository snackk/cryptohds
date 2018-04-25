package com.sec.cryptohds.web.rest;

import java.io.IOException;
import java.net.URISyntaxException;

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
import com.sec.cryptohds.service.OperationService;
import com.sec.cryptohds.service.exceptions.CryptohdsException;
import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.envelope.Message;
import com.sec.cryptohdslibrary.service.dto.OperationDTO;
import com.sec.cryptohdslibrary.service.dto.ReceiveOperationDTO;

@RestController
@RequestMapping("/api")
public class OperationResource {

    private final Logger log = LoggerFactory.getLogger(OperationResource.class);

    private final OperationService operationService;

    private final EnvelopeHandler envelopeHandler;

    public OperationResource(OperationService operationService,
                             EnvelopeHandler envelopeHandler) {

        this.operationService = operationService;
        this.envelopeHandler = envelopeHandler;
    }

    /**
     * POST  /operation/send  : Sends an amount from destination to source.
     * <p>
     * Sends an amount from destination to source.
     *
     * @param envelope that contains ciphered message.
     * @return the ResponseEntity with status 201 (Created) and with body the operation, or with status 400 (Bad Request)
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/operation/send")
    public ResponseEntity<?> sendOperation(@Valid @RequestBody Envelope envelope) throws CryptohdsException, IOException, ClassNotFoundException {
        log.debug("REST request to send Operation : {}", envelope);

		Message message = this.envelopeHandler.handleIncomeEnvelope(envelope);
		OperationDTO operationDTO = (OperationDTO) message.getContent();

		operationService.createOperation(operationDTO, message.getSignedContent());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * POST  /operation/receive  : Receives an amount from source.
     * <p>
     * Receives an amount from source.
     *
     * @param envelope that contains ciphered message.
     * @return the ResponseEntity with status 201 (Received) and with body the operation, or with status 400 (Bad Request)
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/operation/receive")
    public ResponseEntity<?> receiveOperation(@Valid @RequestBody Envelope envelope) throws CryptohdsException, IOException, ClassNotFoundException {
        log.debug("REST request to receive Operation : {}", envelope);

		Message message = this.envelopeHandler.handleIncomeEnvelope(envelope);
		ReceiveOperationDTO receiveOperationDTO = (ReceiveOperationDTO) message.getContent();

		operationService.receiveOperation(receiveOperationDTO, message.getSignedContent());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

