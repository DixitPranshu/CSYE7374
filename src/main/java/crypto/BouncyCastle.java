package crypto;
import java.security.Security;
import java.security.MessageDigest;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
public class BouncyCastle {

    public static void main(String args[]){
        Security.addProvider(new BouncyCastleProvider());
//        String plainString = "hello";
        String plainString = "goodbye";
        MessageDigest messageDigest = new SHA256.Digest();
        byte[] hashed = messageDigest.digest(plainString.getBytes());
        System.out.println(Hex.encodeHexString(hashed));
    }
}
