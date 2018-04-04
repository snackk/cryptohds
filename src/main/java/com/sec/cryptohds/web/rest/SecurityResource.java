package com.sec.cryptohds.web.rest;

import com.sec.cryptohds.security.keystore.ServerKeyStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class SecurityResource {
    private final Logger log = LoggerFactory.getLogger(SecurityResource.class);

    private final ServerKeyStore serverKeyStore;

    public SecurityResource(ServerKeyStore serverKeyStore) {
        this.serverKeyStore = serverKeyStore;
    }

    /**
     * GET  /security/keys : Get's this Server Public-key.
     * <p>
     * Get's this Server Public-key.
     *
     * @return the ResponseEntity with status 200 and with body the String with the public-key, or with status 400 (Bad Request)
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @GetMapping("/security/keys")
    public ResponseEntity<?> getKeys() throws URISyntaxException {
        log.debug("REST request to get Security keys");

        return new ResponseEntity<>(this.serverKeyStore.getKeyStorePublicKey(), HttpStatus.OK);
    }

}
