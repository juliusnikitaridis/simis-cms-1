package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import com.simisinc.platform.domain.model.carfix.QuoteItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Julius Nikitaridis
 *class that will represent a farm object
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Farm extends Entity {

    private String id;
    private String name;
    private String latitude;
    private String longitude;
    private String logoData;
    private String type;
    private String locationData;
    private String address;

}




