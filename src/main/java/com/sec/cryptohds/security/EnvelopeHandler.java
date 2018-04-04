package com.sec.cryptohds.security;

import com.sec.cryptohds.service.exceptions.SecurityValidationException;
import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.envelope.Message;
import com.sec.cryptohdslibrary.security.CipherInstance;
import com.sec.cryptohdslibrary.service.dto.CryptohdsDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EnvelopeHandler {

    private final ServerKeyStore serverKeyStore;

    public EnvelopeHandler(ServerKeyStore serverKeyStore) {
        this.serverKeyStore = serverKeyStore;
    }

    public CryptohdsDTO handleIncomeEnvelope(Envelope envelope) throws IOException, ClassNotFoundException {
        Message message = envelope.decipherEnvelope(this.serverKeyStore.getKeyStore());
        if (!message.verifyMessageSignature(envelope.getClientPublicKey())) {
            throw new SecurityValidationException(envelope.getClientPublicKey());
        }
        return message.getContent();
    }

    public Envelope handleOutcomeEnvelope(CryptohdsDTO cryptohdsDTO, String ledgerPublicKey) throws IOException {
        String pKey = this.serverKeyStore.getKeyStorePublicKey();

        Message response = new Message(cryptohdsDTO, this.serverKeyStore.getKeyStore());
        Envelope env = new Envelope();
        env.cipherEnvelope(response, ledgerPublicKey);

        env.setClientPublicKey(pKey);
        return env;
    }

}
