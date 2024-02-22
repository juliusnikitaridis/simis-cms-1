package com.simisinc.platform.domain.model.cannacomply;


import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CropData extends Entity {
    private String id;
    private String itemCode;
    private String itemName;
    private String country;
    private String seasonality;
    private String cropType;
    private String variety;
    private String commodity;
}
