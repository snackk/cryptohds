package com.sec.cryptohds;

import com.sec.cryptohds.domain.Ledger;
import com.sec.cryptohds.domain.Operation;
import com.sec.cryptohds.domain.OperationType;
import com.sec.cryptohds.service.LedgerService;
import com.sec.cryptohds.service.OperationService;
import com.sec.cryptohds.service.dto.LedgerDTO;
import com.sec.cryptohds.service.dto.OperationDTO;
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
	public CommandLineRunner LedgerCreation(LedgerService ledgerService, OperationService operationService) {
		return (args) -> {
			if(!ledgerService.existsAnyLedger()) {
				ledgerService.registerLedger(new LedgerDTO("name_1", "pubkey_1"));
				ledgerService.registerLedger(new LedgerDTO("name_2", "pubkey_2"));
				ledgerService.registerLedger(new LedgerDTO("name_3", "pubkey_3"));
				ledgerService.registerLedger(new LedgerDTO("name_4", "pubkey_4"));
				ledgerService.registerLedger(new LedgerDTO("name_5", "pubkey_5"));
				ledgerService.registerLedger(new LedgerDTO("name_6", "pubkey_6"));
				ledgerService.registerLedger(new LedgerDTO("name_7", "pubkey_7"));
				ledgerService.registerLedger(new LedgerDTO("name_8", "pubkey_8"));
				ledgerService.registerLedger(new LedgerDTO("name_9", "pubkey_9"));
				ledgerService.registerLedger(new LedgerDTO("name_10", "pubkey_10"));

				LedgerDTO ledgerDTO = new LedgerDTO("pubkey_1");
				operationService.createOperation(new OperationDTO(ledgerDTO, OperationType.INCOMING, 1L));
			}

			log.info("Ledgers found with findLedgers():");
			log.info("-------------------------------");
			for (Ledger ledger : ledgerService.findLedgers()) {
				log.info(ledger.toString());
				for (Operation operation: ledger.getOperations()) {
					log.info("    ->Operation ID: " + operation.toString());
				}
			}
			log.info("-------------------------------");
		};
	}
}
