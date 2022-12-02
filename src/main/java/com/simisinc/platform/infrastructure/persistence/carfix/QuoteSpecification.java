package com.simisinc.platform.infrastructure.persistence.carfix;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuoteSpecification {

    private long requestForServiceId = -1L;
    private long serviceProviderId = -1L;

}
