package com.dimu.dimuapi.service.token;


import com.dimu.dimuapi.model.DiimuToken;

public interface DiimuTokenService {
   public String createToken(String email);

   public String verifyToken(String email, String token);

   public String resendToken(String email) throws Exception;
}
