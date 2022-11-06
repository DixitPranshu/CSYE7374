package crypto;

public class TreeNode {

        private TreeNode left;
        private TreeNode right;
        private String originalString;
        private String hash;
        private String isHash;

        public TreeNode(TreeNode left, TreeNode right, String hash, String originalString) {
            this.left = left;
            this.right = right;
            this.hash = hash;
            this.originalString = originalString;
        }

        public TreeNode getLeft() {
            return left;
        }

        public void setLeft(TreeNode left) {
            this.left = left;
        }

        public TreeNode getRight() {
            return right;
        }

        public void setRight(TreeNode right) {
            this.right = right;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getOriginalString() {
            return originalString;
        }

        public void setOriginalString(String originalString) {
            this.originalString = originalString;
        }
}
