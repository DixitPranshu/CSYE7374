package crypto;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import org.apache.commons.text.RandomStringGenerator;

public class MerkleTree {

    public static String generateHash(HashMap.Entry<String, String> set) throws NoSuchAlgorithmException {
        if(set.getValue().equals("yes")){
            return set.getKey();
        }
        String message = set.getKey();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(message.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static TreeNode generateTree(HashMap<String, String> transactions) throws NoSuchAlgorithmException {
        ArrayList<TreeNode> childNodes = new ArrayList<>();

        for(HashMap.Entry<String, String> set: transactions.entrySet()){
            childNodes.add(new TreeNode(null, null, generateHash(set), set.getKey()));
        }
//        for (String message : dataBlocks) {
//            childNodes.add(new TreeNode(null, null, getHash(message)));
//        }

        return buildTree(childNodes);
    }

    private static TreeNode buildTree(ArrayList<TreeNode> children) throws NoSuchAlgorithmException {
        ArrayList<TreeNode> parents = new ArrayList<>();

        while (children.size() != 1) {
            int index = 0, length = children.size();
            while (index < length) {
                TreeNode leftChild = children.get(index);
                TreeNode rightChild = null;

                if ((index + 1) < length) {
                    rightChild = children.get(index + 1);
                } else {
                    rightChild = new TreeNode(null, null, leftChild.getHash(), leftChild.getOriginalString());
                }
                HashMap.Entry<String,String> newSet = new AbstractMap.SimpleEntry<String, String>(leftChild.getHash() + rightChild.getHash(), "no");
                String parentHash = generateHash(newSet);
                parents.add(new TreeNode(leftChild, rightChild, parentHash, newSet.getKey()));
                index += 2;
            }
            children = parents;
            parents = new ArrayList<>();
        }
        return children.get(0);
    }

    private static void printLevelOrderTraversal(TreeNode root) {
        if (root == null) {
            return;
        }

        if ((root.getLeft() == null && root.getRight() == null)) {
            System.out.println(root.getHash());
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        queue.add(null);

        while (!queue.isEmpty()) {
            TreeNode TreeNode = queue.poll();
            if (TreeNode != null) {
                System.out.println(TreeNode.getHash());
            } else {
                System.out.println();
                if (!queue.isEmpty()) {
                    queue.add(null);
                }
            }

            if (TreeNode != null && TreeNode.getLeft() != null) {
                queue.add(TreeNode.getLeft());
            }

            if (TreeNode != null && TreeNode.getRight() != null) {
                queue.add(TreeNode.getRight());
            }

        }

    }

    public static void main(String[] args) throws NoSuchAlgorithmException {

        HashMap<String, String> transactions1 = new LinkedHashMap<>();
        transactions1.put("00000dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824", "no");
        transactions1.put("Harry pays Robin 1.000","no");
        transactions1.put("Maharshi pays Harry 1.000","no");
        transactions1.put("Pranshu pays Maharshi 1.000","no");
        TreeNode root1 = generateTree(transactions1);
        String rootHash1 = root1.getHash();

        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();


        long total_program_run_time = 0;
        for(int i=0;i<100;i++){
            System.out.println("Run "+(i+1));
            String rootHash = "";
            long startTime = System.nanoTime();
            while(!rootHash.startsWith("00000")){
                HashMap<String, String> transactions2 = new LinkedHashMap<>();
                HashMap<String, String> transactions3 = new LinkedHashMap<>();
                String nonce = generator.generate(10);
//            transactions.put(nonce,"no");

                transactions2.put("Robin pays Pranshu 1.000","no");
                transactions2.put(nonce,"no");
                TreeNode root2 = generateTree(transactions2);
                String rootHash2 = root2.getHash();

                transactions3.put(rootHash1,"yes");
                transactions3.put(rootHash2,"yes");
                TreeNode root3 = generateTree(transactions3);
                rootHash = root3.getHash();

            }
//            System.out.println(rootHash);
            long endTime = System.nanoTime();
            long totalTime = (endTime - startTime)/1000000000;
            total_program_run_time+=totalTime;
//            System.out.println("totalTime for mining: "+totalTime+" seconds");
        }
        System.out.println("Avg totalTime for mining: "+total_program_run_time+" seconds");



//        printLevelOrderTraversal(root);
    }
}
