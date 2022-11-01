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
            childNodes.add(new TreeNode(null, null, generateHash(set)));
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
                    rightChild = new TreeNode(null, null, leftChild.getHash());
                }
                HashMap.Entry<String,String> newSet = new AbstractMap.SimpleEntry<String, String>(leftChild.getHash() + rightChild.getHash(), "no");
                String parentHash = generateHash(newSet);
                parents.add(new TreeNode(leftChild, rightChild, parentHash));
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
        HashMap<String, String> transactions = new LinkedHashMap<>();


        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();

        String rootHash = "";
        while(!rootHash.startsWith("0000")){
            String randomLetters = generator.generate(10);
            String nonce = randomLetters;
            transactions.put("00000dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824", "no");
//            transactions.put(nonce,"no");
            transactions.put("Harry pays Robin 1.000","no");
            transactions.put("Maharshi pays Harry 1.000","no");
            transactions.put("Pranshu pays Maharshi 1.000","no");
            transactions.put("Robin pays Pranshu 1.000","no");
            transactions.put(nonce,"no");
            TreeNode root = generateTree(transactions);
            rootHash = root.getHash();
            System.out.println(root.getHash());
        }


//        printLevelOrderTraversal(root);
    }
}
