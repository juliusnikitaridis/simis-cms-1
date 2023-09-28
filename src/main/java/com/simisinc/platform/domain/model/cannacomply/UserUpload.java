package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserUpload extends Entity {
    private String id;
    private String name;
    private String fileName;
    private String filePath;
    private String fileType;
    private String createdBy;
    private String createdDate;
    private String fileSize;
    private String description;
}
