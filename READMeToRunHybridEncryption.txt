Before running the java file, your private and public key need to converted into a specific format for the java code to understand

Convert private Key to PKCS#8 format (so Java can read it)

$ openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key.pem -out private_key.der -nocrypt

Output public key portion in DER format (so Java can read it)

$ openssl rsa -in private_key.pem -pubout -outform DER -out public_key.der

Ref: https://stackoverflow.com/questions/11410770/load-rsa-public-key-from-file

To run the file hybridEncryption you need to pass in 3 command line arguments
1. file to be encrypted
2. public key
3. private key

The code will use the private key to decrypt the the encoded key and using the decoded key you can decrypt the encrypted message. 



java hybridEncryption.java D:\\Masters_Information_Systems\\Semester_3\\Cryptography\\Assignments\\Assignment_6\\test.txt D:\\Masters_Information_Systems\\Semester_3\\Cryptography\\Assignments\\Assignment_6\\bob_public_key D:\\Masters_Information_Systems\\Semester_3\\Cryptography\\Assignments\\Assignment_6\\bob_private.der