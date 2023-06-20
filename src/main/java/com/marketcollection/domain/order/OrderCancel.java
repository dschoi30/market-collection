package com.marketcollection.domain.order;

import com.marketcollection.domain.order.dto.PGResponse;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class OrderCancel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int cancelAmount;
    private String cancelReason;
    private LocalDateTime canceledAt;
    private String transactionKey;
    String receiptKey;

    public static OrderCancel createOrderCancel(PGResponse pgResponse) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        PGResponse.CancelInfo[] cancels = pgResponse.getCancels();
        PGResponse.CancelInfo cancelInfo = cancels[0];
        return OrderCancel.builder()
                .cancelAmount(Integer.parseInt(cancelInfo.getCancelAmount()))
                .cancelReason(cancelInfo.getCancelReason())
                .canceledAt(OffsetDateTime.parse(cancelInfo.getCanceledAt(), formatter).toLocalDateTime())
                .transactionKey(cancelInfo.getTransactionKey())
                .receiptKey(cancelInfo.getReceiptKey())
                .build();
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
