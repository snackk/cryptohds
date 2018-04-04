package com.sec.cryptohds.security;

import com.sec.cryptohdslibrary.keystore.KeyStoreImpl;
import com.sec.cryptohdslibrary.security.CipherInstance;
import org.springframework.stereotype.Service;

@Service
public class ServerKeyStore {

    private KeyStoreImpl serverKeyStore = new KeyStoreImpl("cryptohds", "cryptohds");

    public KeyStoreImpl getKeyStore() {
        return serverKeyStore;
    }

    public String getKeyStorePublicKey() {
        return CipherInstance.encodePublicKey(getKeyStore().getkeyPairHDS().getPublic());
    }
}
