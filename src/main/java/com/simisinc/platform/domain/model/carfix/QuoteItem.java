package com.simisinc.platform.domain.model.carfix;

import com.simisinc.platform.domain.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Julius Nikitaridis
 * This will represent the service requests that are lodged by members of the platform. There can
 * be 3 types of service requests
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuoteItem extends Entity {

    private String id;
    private String quoteId;
    private String partNumber;
    private String partDescription;
    private String itemTotalPrice;
    private String quantity;
    private String itemType;
    private String itemStatus;
    private String replacementReason;
    private String partsTotal;
    private String labourTotal;
    private String partsPicture;
    private String status;
}




