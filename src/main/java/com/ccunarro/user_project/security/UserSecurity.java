package com.ccunarro.user_project.security;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("userSecurity")
public class UserSecurity {

    public boolean isSameUser(SimpleUserDetails simpleUserDetails, UUID userId) {
       if (simpleUserDetails == null) return false;
       if (simpleUserDetails.getUser() == null) return false;
       return userId.equals(simpleUserDetails.getUser().getId());
    }

    //obviously this would never be implemented in a real app, it should be created a proper user_role table
    public boolean isSuperAdmin(SimpleUserDetails simpleUserDetails) {
       return "thesuperadmin@company.com".equals(simpleUserDetails.getUser().getEmail());
    }


}
