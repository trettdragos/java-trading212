package io.github.trettdragos.trading212.model.history;

import io.github.trettdragos.trading212.model.common.Device;
import io.github.trettdragos.trading212.model.common.FillType;
import io.github.trettdragos.trading212.model.order.OrderStatus;
import io.github.trettdragos.trading212.model.order.OrderType;
import io.github.trettdragos.trading212.model.order.TimeValidity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

/** A historical (completed, cancelled, or rejected) equity order, as returned by history endpoints. */
public record HistoryOrder(
        long id,
        long parentOrder,
        String ticker,
        OrderType type,
        OrderStatus status,
        Device executor,
        BigDecimal orderedQuantity,
        BigDecimal orderedValue,
        BigDecimal filledQuantity,
        BigDecimal filledValue,
        BigDecimal limitPrice,
        BigDecimal stopPrice,
        Long fillId,
        BigDecimal fillPrice,
        BigDecimal fillCost,
        BigDecimal fillResult,
        FillType fillType,
        TimeValidity timeValidity,
        OffsetDateTime dateCreated,
        OffsetDateTime dateModified,
        OffsetDateTime dateExecuted,
        List<Tax> taxes) {
}
