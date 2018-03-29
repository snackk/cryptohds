package com.sec.cryptohds.service.security;

import com.sec.cryptohdslibrary.keystore.KeyStoreImpl;
import org.springframework.stereotype.Service;

@Service
public class ServerKeyStore {

    private KeyStoreImpl serverKeyStore = new KeyStoreImpl("cryptohds", "cryptohds");

    private KeyStoreImpl getKeyStore() {
        return serverKeyStore;
    }
}
