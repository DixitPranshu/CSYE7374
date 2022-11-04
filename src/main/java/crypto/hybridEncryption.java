package crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class hybridEncryption {

    public static byte[] getFileInBytes(File file) throws IOException{
        FileInputStream fis = new FileInputStream(file);
        byte[] file_bytes = new byte[(int) file.length()];
        fis.read(file_bytes);
        fis.close();
        return file_bytes;
    }

    private static void writeToFile(File output, byte[] toWrite)
            throws IllegalBlockSizeException, BadPaddingException, IOException{

        FileOutputStream fos = new FileOutputStream(output);
        fos.write(toWrite);
        fos.flush();
        fos.close();
        System.out.println("The file was successfully encrypted and stored in: " + output.getPath());

    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        return pair;
    }
    public static SecretKeySpec generateSymmetricKey(int length) throws NoSuchAlgorithmException {
        SecureRandom secure_random = new SecureRandom();
        byte[] key = new byte[length];
        secure_random.nextBytes(key);
        SecretKeySpec symmetric_key = new SecretKeySpec(key, "AES");
        return symmetric_key;
    }

    public static String encryptMessageFile(SecretKeySpec key, String filepath) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        File plaintext_file = new File(filepath);
        Cipher encrypt_cipher = Cipher.getInstance("AES");
        encrypt_cipher.init(Cipher.ENCRYPT_MODE, key);
//        byte[] secret_item_bytes = item.getBytes(StandardCharsets.UTF_8);
        byte[] secret_message_bytes = getFileInBytes(plaintext_file);
        byte[] encrypted_item_bytes = encrypt_cipher.doFinal(secret_message_bytes);
        String output_filepath = "test.enc";
        File output_file = new File(output_filepath);
        writeToFile(output_file,encrypted_item_bytes);
        String encoded_item = Base64.getEncoder().encodeToString(encrypted_item_bytes);

        return output_filepath;
    }

    public static String encryptKey(SecretKeySpec symmetric_key, String bob_public_key_path) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {

        byte[] keyBytes = Files.readAllBytes(new File(bob_public_key_path).toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey bob_public_key = kf.generatePublic(spec);
        Cipher encrypt_cipher = Cipher.getInstance("RSA");

        encrypt_cipher.init(Cipher.ENCRYPT_MODE, bob_public_key);
        byte[] symmetric_key_bytes = symmetric_key.getEncoded();
        byte[] encrypted_item_bytes = encrypt_cipher.doFinal(symmetric_key_bytes);
        String output_filepath = "alice_symm_key.enc";
        File output_file = new File(output_filepath);
        writeToFile(output_file,encrypted_item_bytes);
        String encodedItem = Base64.getEncoder().encodeToString(encrypted_item_bytes);
        return output_filepath;
    }

    public static String decryptKey(String encoded_key_path, String bob_private_key_path) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {

        Cipher decryptCipher = Cipher.getInstance("RSA");
        byte[] keyBytes = Files.readAllBytes(Paths.get(bob_private_key_path));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey bob_private_key = kf.generatePrivate(spec);
        decryptCipher.init(Cipher.DECRYPT_MODE, bob_private_key);

        byte[] encoded_key = getFileInBytes(new File(encoded_key_path));
//        byte[] decoded_bytes = Base64.getDecoder().decode(encoded_key);
        byte[] decrypted_key_bytes = decryptCipher.doFinal(encoded_key);
        String output_filepath = "alice_dec_symm_key.enc";
        File output_file = new File(output_filepath);
        writeToFile(output_file,decrypted_key_bytes);
        String decoded_item = Base64.getEncoder().encodeToString(decrypted_key_bytes);
        return output_filepath;
    }

    public static String decryptMessage(String decoded_key_path, String encrypted_file_path) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {

        Cipher decrypt_cipher = Cipher.getInstance("AES");
        byte[] decoded_bytes = Files.readAllBytes(Paths.get(decoded_key_path));
//        byte[] decoded_bytes = Base64.getDecoder().decode(decoded_key);
        SecretKeySpec symmetric_key = new SecretKeySpec(decoded_bytes, "AES");
        decrypt_cipher.init(Cipher.DECRYPT_MODE, symmetric_key);
        byte[] encrypted_message_bytes = getFileInBytes(new File(encrypted_file_path));
//        byte[] secret_item_bytes = Base64.getDecoder().decode(encrypted_message);
        byte[] decrypted_message_bytes = decrypt_cipher.doFinal(encrypted_message_bytes);
        String decoded_message = new String(decrypted_message_bytes, StandardCharsets.UTF_8);
        return decoded_message;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, IOException {
        SecretKeySpec alice_symmetric_key = generateSymmetricKey(16);

        KeyPair bob_key_pair = generateKeyPair();
        String filepath = args[0];
        String bob_public_key_path = args[1];
        String bob_private_key_path = args[2];
        String encrypted_filepath = encryptMessageFile(alice_symmetric_key, filepath);
//        System.out.println("encoded_message: "+plain_text);
        String encoded_key_path = encryptKey(alice_symmetric_key, bob_public_key_path);
//        System.out.println("encoded_key: "+encoded_key);
//
        String decrypted_key_path = decryptKey(encoded_key_path, bob_private_key_path);
//        System.out.println("decrypted_key: "+decrypted_key);
//
        String decoded_message = decryptMessage(decrypted_key_path, encrypted_filepath);
        System.out.println("decoded_message: "+decoded_message);

    }
}
