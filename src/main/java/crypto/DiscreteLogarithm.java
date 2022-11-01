package crypto;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

public class DiscreteLogarithm {
    public static BigInteger i_b, result;
    static HashMap<BigInteger, ArrayList> find_roots(BigInteger n, BigInteger a){
        HashMap<BigInteger, ArrayList> k_a = new HashMap<>();
        BigInteger k = new BigInteger("0");
//        BigInteger a = new BigInteger("0");
        while(k.compareTo(n) < 0){
//            k_a.put(k, new ArrayList<BigInteger>());
//            System.out.println("k: "+k);
//            while(a.compareTo(n) < 0){
//                System.out.println("i: "+i);
                result = a.modPow(k,n);
                if(!k_a.containsKey(a)){
                    k_a.put(a, new ArrayList<BigInteger>());
                }
                ArrayList<BigInteger> a_list = k_a.get(a);
                a_list.add(result);
//                System.out.println("result: "+result);
//                a = a.add(BigInteger.ONE);
//            }
//            a = BigInteger.ZERO;
            k = k.add(BigInteger.ONE);
        }
//        System.out.println("k_a: "+k_a);
        return k_a;
    }

    static BigInteger getDiscreteLog(BigInteger n, BigInteger a, BigInteger k){
//        ArrayList<BigInteger> a_list = k_a.get(a);
        BigInteger val = new BigInteger("-1");
        BigInteger x = new BigInteger("0");
        while(x.compareTo(n) < 0){
            result = a.modPow(x,n);
            if(result.equals(k)){
                val = x;
                return val;
            }
            x = x.add(BigInteger.ONE);
        }
        return val;


    }


    public static void main(String[] args){
//        BigInteger n = new BigInteger("17");
        BigInteger k = new BigInteger("10");
        BigInteger a = new BigInteger("7");

        for(int j=0;j<10;j++){
            long start = System.currentTimeMillis();
            System.out.println(j+" run");
            for(int i = 0 ;i<1;i++){
                Random random = new Random();
                BigInteger n = BigInteger.probablePrime(15,random);
                System.out.println("n: ");getDiscreteLog(n,a,k);
            }
            long finish = System.nanoTime();
            long timeElapsed = finish - start;
            System.out.println("Elapsed time fo running 100000 times: "+timeElapsed + "ms j = " + j + " run");
        }

    }
}

