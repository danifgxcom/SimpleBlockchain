package com.danifgx.blockchain.validator;

import com.danifgx.blockchain.model.Wallet;

import java.util.List;
import java.util.Random;

public class ValidatorSelector {
    private List<Wallet> wallets;
    private Random random;

    public ValidatorSelector(List<Wallet> wallets) {
        this(wallets, new Random());
    }

    public ValidatorSelector(List<Wallet> wallets, Random random) {
        if (wallets == null || wallets.isEmpty()) {
            throw new IllegalArgumentException("La lista de billeteras no puede ser nula o estar vacía");
        }
        this.wallets = wallets;
        this.random = random;
    }

    public Wallet selectValidator() {
        double totalStake = wallets.stream().mapToDouble(Wallet::getBalance).sum();
        double draw = random.nextDouble() * totalStake;

        for (Wallet wallet : wallets) {
            if (draw < wallet.getBalance()) {
                return wallet;
            }
            draw -= wallet.getBalance();
        }

        throw new IllegalStateException("No se pudo seleccionar un validador. Esto no debería ocurrir si los balances están correctos.");
    }
}
