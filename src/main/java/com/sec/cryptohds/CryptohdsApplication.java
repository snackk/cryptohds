package com.sec.cryptohds;

import com.sec.cryptohds.domain.Ledger;
import com.sec.cryptohds.service.LedgerService;
import com.sec.cryptohds.service.dto.LedgerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CryptohdsApplication {

	private static final Logger log = LoggerFactory.getLogger(CryptohdsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CryptohdsApplication.class, args);
	}

	@Bean
	public CommandLineRunner LedgerCreation(LedgerService ledgerService) {
		return (args) -> {
			if(!ledgerService.existsAnyLedger()) {
				ledgerService.registerLedger(new LedgerDTO("pubkey_1"));
				ledgerService.registerLedger(new LedgerDTO("pubkey_2"));
				ledgerService.registerLedger(new LedgerDTO("pubkey_3"));
				ledgerService.registerLedger(new LedgerDTO("pubkey_4"));
				ledgerService.registerLedger(new LedgerDTO("pubkey_5"));
				ledgerService.registerLedger(new LedgerDTO("pubkey_6"));
				ledgerService.registerLedger(new LedgerDTO("pubkey_7"));
				ledgerService.registerLedger(new LedgerDTO("pubkey_8"));
				ledgerService.registerLedger(new LedgerDTO("pubkey_9"));
				ledgerService.registerLedger(new LedgerDTO("pubkey_10"));
			}

			log.info("Ledgers found with findLedgers():");
			log.info("-------------------------------");
			for (Ledger ledger : ledgerService.findLedgers()) {
				log.info(ledger.toString());
			}
			log.info("-------------------------------");
		};
	}
}
