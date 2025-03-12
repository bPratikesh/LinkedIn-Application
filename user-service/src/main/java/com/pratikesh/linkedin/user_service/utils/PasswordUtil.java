package com.pratikesh.linkedin.user_service.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String unHashedPassword){
        return BCrypt.hashpw(unHashedPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String unHashedPassword, String hashedPassword){
        return BCrypt.checkpw(unHashedPassword, hashedPassword);
    }

}
