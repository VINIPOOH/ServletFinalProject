package bl.service.impl;


import bl.exeptions.PasswordEncodeException;
import bl.service.PasswordEncoderService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoderServiceImpl implements PasswordEncoderService {
    private static Logger log = LogManager.getLogger(PasswordEncoderServiceImpl.class);
    private static String encodingAlgorithmKey = "MD5";

    @Override
    public String encode(String password) {
        log.debug("password -" + password);

        try {
            MessageDigest md = MessageDigest.getInstance(encodingAlgorithmKey);
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.fatal("not correct encode algorithm", e);
            throw new PasswordEncodeException("Password encode error");
        }
    }
}
