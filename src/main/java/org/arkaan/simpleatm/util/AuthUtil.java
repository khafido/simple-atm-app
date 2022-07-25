package org.arkaan.simpleatm.util;

import org.arkaan.simpleatm.dto.response.AccountDto;
import org.springframework.util.Base64Utils;

public final class AuthUtil {
    
    private static String SECRET = "super_secret_key";
    
    private AuthUtil() {}
    
    public static String encodeBase64(AccountDto dto) {
        String token = String.format("%d:%s:%s", dto.getAccountNumber(), dto.getName(), SECRET);
        return Base64Utils.encodeToString(token.getBytes());
    }
    
    public static AccountDto decodeBase64(String base64) {
        String token = new String(Base64Utils.decodeFromString(base64));
        String[] tokenSplit = token.split(":");
        if (tokenSplit[2] != null && tokenSplit[2].equals(SECRET)) {
            return new AccountDto(Integer.parseInt(tokenSplit[0]), tokenSplit[1]);
        }
        return null;
    }
}
