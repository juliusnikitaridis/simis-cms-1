package com.simisinc.platform.domain.model.carfix;

import com.simisinc.platform.domain.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Julius Nikitaridis
 * class that will represent a service provider.
 * This class should be saved to the db along with the previously created users unique id.
 * 14-12-2022
 */

@Getter
@Setter
@NoArgsConstructor
public class ServiceProvider extends Entity {

    private String id;
    private String user_id;
    private String supportedBrands; //this should be a jsonarray string ? //expand to table ?
    private String name;
    private String services;
    private String certifications;
}




