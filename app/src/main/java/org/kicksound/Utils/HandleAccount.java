package org.kicksound.Utils;

import org.kicksound.Controllers.Statics.StaticObjects;

public class HandleAccount {
    public static void setUserParameters(String id, String firstname, String lastname, String email, int type) {
        StaticObjects.userAccount.setId(id);
        StaticObjects.userAccount.setFirstname(firstname);
        StaticObjects.userAccount.setLastname(lastname);
        StaticObjects.userAccount.setEmail(email);
        StaticObjects.userAccount.setType(type);
    }
}
