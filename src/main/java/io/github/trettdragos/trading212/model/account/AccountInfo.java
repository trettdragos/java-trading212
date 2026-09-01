package io.github.trettdragos.trading212.model.account;

/**
 * @param currencyCode ISO 4217 currency code the account is denominated in, e.g. "USD"
 * @param id            Trading212 account id
 */
public record AccountInfo(String currencyCode, long id) {
}
