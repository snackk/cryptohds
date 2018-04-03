package com.sec.cryptohds.security;

import com.sec.cryptohdslibrary.keystore.KeyStoreImpl;
import com.sec.cryptohdslibrary.security.RsaRelatedMethods;
import org.springframework.stereotype.Service;

@Service
public class ServerKeyStore {

    private KeyStoreImpl serverKeyStore = new KeyStoreImpl("cryptohds", "cryptohds");

    private KeyStoreImpl getKeyStore() {
        return serverKeyStore;
    }

    public String getKeyStorePublicKey() {
        return RsaRelatedMethods.encodePublicKey(getKeyStore().getkeyPairHDS().getPublic());
    }
}
