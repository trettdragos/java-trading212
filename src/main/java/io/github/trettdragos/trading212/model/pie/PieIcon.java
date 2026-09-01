package io.github.trettdragos.trading212.model.pie;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Icon shown for a Pie in the Trading212 app. */
public enum PieIcon {
    Airplane, Apartments, Bills, BillsAndCoins, Briefcase, Burger, Bus, Cabin, Car, Child,
    Coins, Convertable, Education, Energy, Factory, Family, Global, Home, Iceberg, Landscape,
    Leaf, Materials, Medical, PiggyBank, Pill, Ring, RV, Shipping, Storefront, Tech, Travel,
    Umbrella, Unicorn, Vault, Water, Whale, Wind,
    @JsonEnumDefaultValue
    UNKNOWN
}
