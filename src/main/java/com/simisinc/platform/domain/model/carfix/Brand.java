package com.simisinc.platform.domain.model.carfix;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Julius Nikitaridis
 * This will represent a supported brand in the system
 * be 3 types of service requests
 */

@Getter
@Setter
@NoArgsConstructor
public class Brand extends Entity {

    private String id;
    private String brandName;
}




