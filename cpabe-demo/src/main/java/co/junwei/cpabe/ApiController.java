package co.junwei.cpabe;

import co.junwei.bswabe.Bswabe;
import co.junwei.bswabe.SerializeUtils;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@RestController
public class ApiController {

    public static Policy p;

    static {
        Bswabe.setup(ABEUtils.pub, ABEUtils.msk);

        //ABEUtils.pub = SerializeUtils.unserializeBswabePub(Base64.getDecoder().decode("AAABZ3R5cGUgYQpxIDg3ODA3MTA3OTk2NjMzMTI1MjI0Mzc3ODE5ODQ3NTQwNDk4MTU4MDY4ODMxOTk0MTQyMDgyMTEwMjg2NTMzOTkyNjY0NzU2MzA4ODAyMjI5NTcwNzg2MjUxNzk0MjI2NjIyMjE0MjMxNTU4NTg3Njk1ODIzMTc0NTkyNzc3MTMzNjczMTc0ODEzMjQ5MjUxMjk5OTgyMjQ3OTEKaCAxMjAxNjAxMjI2NDg5MTE0NjA3OTM4ODgyMTM2Njc0MDUzNDIwNDgwMjk1NDQwMTI1MTMxMTgyMjkxOTYxNTEzMTA0NzIwNzI4OTM1OTcwNDUzMTEwMjg0NDgwMjE4MzkwNjUzNzc4Njc3NgpyIDczMDc1MDgxODY2NTQ1MTYyMTM2MTExOTI0NTU3MTUwNDkwMTQwNTk3NjU1OTYxNwpleHAyIDE1OQpleHAxIDEwNwpzaWduMSAxCnNpZ24wIDEKAAAAgJSviCNQZZCK"));
       // ABEUtils.msk = SerializeUtils.unserializeBswabeMsk(ABEUtils.pub , Base64.getDecoder().decode("AAAAFGh+me6FMFbUYykGKfCVqD59BIBCAAAAgHn8MfXXplzBbG+bYtHe+BC5uFR7fvoVdAqIOxXncCAv0Mx4btuLU/BWgCh34jbTrNVIu63327VsQeLXSVKIW/RmruKjJ2zIxtufaPhSV5VXRCOPrumUFbhu7xpRD3qswMvMibb+HEukavZUkZlN3l9hKR2zkpnr33VMNRBRgqxX"));

        System.out.println(Base64.getEncoder().encodeToString(SerializeUtils.serializeBswabePub(ABEUtils.pub)));
        System.out.println("----");
        System.out.println(Base64.getEncoder().encodeToString(SerializeUtils.serializeBswabeMsk(ABEUtils.msk)));
    }

    @PostMapping("/encrypt")
    public String encryptMessage(@RequestBody EncryptRequest encryptRequest) throws Exception {
         p = ABEUtils.getPolicy(encryptRequest.getPolicy());

        return Base64.getEncoder().encodeToString(ABEUtils.getAesEncryptedBuf(encryptRequest.getContent(), p.getM()));
    }


    @PostMapping("/decrypt")
    public String decryptMessage(@RequestBody DecryptRequest decryptRequest) throws Exception {

//        Policy p = ABEUtils.getPolicy(decryptRequest.getPolicy());

        return ABEUtils.decryptMessage(
                SerializeUtils.unserializeBswabePrv(ABEUtils.pub, Base64.getDecoder().decode(decryptRequest.getUserKey())),
                Base64.getDecoder().decode(decryptRequest.getContent()),
                p.getCypherBuffer()
        );
    }


    @GetMapping(value = "/createKey", produces = {"application/json"})
    public String createKey(@RequestParam List<String> attributes) throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(
                SerializeUtils.serializeBswabePrv(
                        ABEUtils.getUserKey(attributes.toArray(new String[0]))
                )
        );
    }

}
