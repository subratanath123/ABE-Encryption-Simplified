package co.junwei.cpabe;

import co.junwei.bswabe.*;
import it.unisa.dia.gas.jpbc.Element;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ABEUtils {
    final static boolean DEBUG = true;

    public static String[] doctorAttr = {"doctor"};
    public static String[] nurseAttr = {"nurse"};

    public static String doctorPolicyRule = "doctor";
    public static String everyOneAccessPolicyRule = "doctor nurse 1of2";

    public static BswabePub pub = new BswabePub();
    public static BswabeMsk msk = new BswabeMsk();

    public static void main(String[] args) throws Exception {
        Bswabe.setup(pub, msk);

        BswabePrv doctorUserKey =SerializeUtils.unserializeBswabePrv( ABEUtils.pub,
                Base64.getDecoder().decode(Base64.getEncoder().encodeToString(
                SerializeUtils.serializeBswabePrv(getUserKey(doctorAttr))
        ))
        );
        BswabePrv nurseUserKey = getUserKey(nurseAttr);

        Policy doctorPolicy = getPolicy(doctorPolicyRule);
        Policy everyOneAccessPolicy = getPolicy(everyOneAccessPolicyRule);
        Policy everyOneAccessPolicy2 = getPolicy(everyOneAccessPolicyRule);

        byte[] patientCriticalDataAesEncryptedBuf = getAesEncryptedBuf("PatientCriticalData", doctorPolicy.getM());
        byte[] patientPublicDataAesEncryptedBuf = getAesEncryptedBuf("PatientPublicData", everyOneAccessPolicy2.getM());

        decryptMessage(doctorUserKey, patientCriticalDataAesEncryptedBuf, doctorPolicy.getCypherBuffer());
        decryptMessage(nurseUserKey, patientCriticalDataAesEncryptedBuf, doctorPolicy.getCypherBuffer());

        decryptMessage(doctorUserKey, patientPublicDataAesEncryptedBuf, everyOneAccessPolicy.getCypherBuffer());
        decryptMessage(nurseUserKey, patientPublicDataAesEncryptedBuf, everyOneAccessPolicy.getCypherBuffer());
        decryptMessage(nurseUserKey, patientPublicDataAesEncryptedBuf, everyOneAccessPolicy2.getCypherBuffer());
    }

    public static BswabePrv getUserKey(String[] userAttribute) throws NoSuchAlgorithmException {
        return Bswabe.keygen(pub, msk, userAttribute);
    }

    public static Policy getPolicy(String policyMessage) throws Exception {

        BswabeCphKey keyCph = Bswabe.enc(pub, policyMessage);
        BswabeCph cph = keyCph.cph;
        Element m = keyCph.key;

        return new Policy(SerializeUtils.bswabeCphSerialize(cph), m);
    }

    public static byte[] getAesEncryptedBuf(String dataMessage, Element cypherElement) throws Exception {
        return AESCoder.encrypt(cypherElement.toBytes(), dataMessage.getBytes());
    }

    public static String decryptMessage(BswabePrv userPrivateKey,
                                        byte[] aesEncryptedBuf,
                                        byte[] cphBuf) throws Exception {

        try {
            BswabeCph cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

            BswabeElementBoolean beb = Bswabe.dec(pub, userPrivateKey, cph);

            if (beb.b) {
                byte[] plt = AESCoder.decrypt(beb.e.toBytes(), aesEncryptedBuf);
                String s = new String(plt);


                System.out.println("Decrypted Data: " + s);
                return s;

            } else {
                System.out.println("CANNOT dECRYPT!");
            }

            return "";
        } catch (Exception e) {
            System.out.println("CANNOT DECRYPT!");
            return "";
        }
    }

}
