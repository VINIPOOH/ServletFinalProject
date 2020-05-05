package bll.service;

import exeptions.PasswordEncodeException;

public interface PasswordEncoderService {
    String encode(String password);
}
