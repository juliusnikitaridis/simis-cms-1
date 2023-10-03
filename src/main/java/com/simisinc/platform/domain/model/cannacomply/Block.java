package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Block extends Entity {
    private String id;
    private String blockLocation;
    private String barcodeData;
    private String farmId;
    private String date;
    private String locationId;
}
