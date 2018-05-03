package com.sec.cryptohds.security;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.sec.cryptohds.domain.Ledger;
import com.sec.cryptohds.service.LedgerService;
import com.sec.cryptohds.service.exceptions.CryptohdsException;
import com.sec.cryptohds.service.exceptions.SecurityValidationException;
import com.sec.cryptohds.service.exceptions.SequenceNumberNoMatchException;
import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.envelope.Message;
import com.sec.cryptohdslibrary.service.dto.CryptohdsDTO;

@Service
public class EnvelopeHandler {

    private final ServerKeyStore serverKeyStore;

    private final LedgerService ledgerService;

    public EnvelopeHandler(ServerKeyStore serverKeyStore, LedgerService ledgerService) {
        this.serverKeyStore = serverKeyStore;
        this.ledgerService = ledgerService;
    }

	public Message handleIncomeEnvelope(Envelope envelope) throws IOException, ClassNotFoundException, CryptohdsException {
        Message message = envelope.decipherEnvelope(this.serverKeyStore.getKeyStore());

        /*Verify if Message Signature is valid*/
        if (!message.verifyMessageSignature(envelope.getClientPublicKey())) {
            throw new SecurityValidationException(envelope.getClientPublicKey());
        }

        boolean existsLedger = this.ledgerService.existsLedger(envelope.getClientPublicKey());
        Ledger ledger = null;

        if (existsLedger)
            ledger = this.ledgerService.findLedgerByPublicKey(envelope.getClientPublicKey());

        /*Verifies if Sequence number match*/
        if (existsLedger && message.getSeqNumber() != -1 && (message.getSeqNumber() != ledger.getSeqNumber())) {
            throw new SequenceNumberNoMatchException();
        }

        if (existsLedger) {
            /*Validation correct, time to increment sequence number*/
            ledger.setSeqNumber(ledger.getSeqNumber() + 1);
            this.ledgerService.saveLedger(ledger);
        }

		return message;
    }

    public Envelope handleOutcomeEnvelope(CryptohdsDTO cryptohdsDTO, String ledgerPublicKey) throws IOException {
        String pKey = this.serverKeyStore.getKeyStorePublicKey();
        Ledger ledger = this.ledgerService.findLedgerByPublicKey(ledgerPublicKey);

        Message response = new Message(cryptohdsDTO, this.serverKeyStore.getKeyStore(), ledger.getSeqNumber());
        Envelope env = new Envelope();
        env.cipherEnvelope(response, ledgerPublicKey);

        env.setClientPublicKey(pKey);
        return env;
    }

}
