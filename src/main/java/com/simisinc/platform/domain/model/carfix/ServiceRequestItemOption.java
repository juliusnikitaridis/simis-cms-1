package com.simisinc.platform.domain.model.carfix;

import com.simisinc.platform.domain.model.Entity;
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
@NoArgsConstructor
public class ServiceRequestItemOption extends Entity {

    private String id;
    private String description;
    private String category;
}




