//package cn.com.ctsi.constant;
//
//import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
//import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
//
//// 密码加密解密工具类
//public class JasyptUtil {
//    public static String secretKey = "";
//    public static String p = ""; //漏洞扫描加
//
//
//    public static void main(String[] args) {
//        testEncrypt();
//        //testDecrypt();
//    }
//
//    public static void testEncrypt() {
//        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
//        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
//        // 加密的密钥
//        config.setPassword(secretKey); //漏洞扫描加-
//        standardPBEStringEncryptor.setConfig(config);
//        //自己的密码
//        String plainText = p;
//        // 加密后的密码
//        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
//        System.out.println(encryptedText);
//    }
//
//    public static String encryptedP = ""; //
//
//    public static void testDecrypt() {
//        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
//        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
//        config.setPassword(secretKey);//漏洞扫描加-
//        standardPBEStringEncryptor.setConfig(config);
//        String encryptedText = encryptedP;   //加密后的密码漏洞扫描加-
//        String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
//        System.out.println(plainText);
//    }
//}
