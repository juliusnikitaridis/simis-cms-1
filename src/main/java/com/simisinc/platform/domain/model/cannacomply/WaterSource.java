package com.simisinc.platform.domain.model.cannacomply;


import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WaterSource extends Entity {
    private String id;
    private String name;
    private String optimalReadingsJson;
    private String farmId;
    private String date;
    private String type;
    private String volume;
    private String usage;
    private String geoData;
    private String colour;
    private String units;
}
