package com.simisinc.platform.domain.model.cannacomply;


import com.simisinc.platform.domain.model.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Users extends User {

    private String sysUniqueUserId;
    private String farmId;
}
