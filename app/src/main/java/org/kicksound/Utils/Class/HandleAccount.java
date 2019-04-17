package org.kicksound.Utils.Class;

import org.kicksound.Utils.Enums.UserType;
import org.kicksound.Models.Account;

public class HandleAccount {
    public static Account userAccount = new Account();

    public static UserType getUserType() {
        UserType userType;
        int accountUserType = userAccount.getType();
        if(accountUserType == 0){
            userType = UserType.USER;
        } else if(accountUserType == 1){
            userType = UserType.ARTIST;
        } else {
            userType = UserType.FAMOUS_ARTIST;
        }
        return userType;
    }

    public static void setUserParameters(String id, String firstname, String lastname, String email, int type, String accessToken) {
        userAccount.setId(id);
        userAccount.setFirstname(firstname);
        userAccount.setLastname(lastname);
        userAccount.setEmail(email);
        userAccount.setType(type);
        userAccount.setAccessToken(accessToken);
    }
}
