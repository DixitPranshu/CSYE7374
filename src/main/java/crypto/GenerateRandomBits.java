import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

public class GenerateRandomBits {
    public String getBytes(int n) throws IOException {
        Process p = Runtime.getRuntime().exec("dd if=/dev/urandom bs=1 count="+String.valueOf(n));
        final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        String output = "";
        while ((line = reader.readLine()) != null) {
            output+=line;
        }
        reader.close();
        String base64out = Base64.getEncoder().encodeToString(output.getBytes());
        return base64out;
    }

    public static void main(String []args) throws IOException {
        int n = 10;
        HelloWorld helloWorld = new HelloWorld();
        String output = helloWorld.getBytes(n);
        System.out.println("output: "+output);
    }
}
