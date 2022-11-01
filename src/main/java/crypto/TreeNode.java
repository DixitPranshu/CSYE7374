package crypto;

public class TreeNode {

        private TreeNode left;
        private TreeNode right;
        private String hash;
        private String isHash;

        public TreeNode(TreeNode left, TreeNode right, String hash) {
            this.left = left;
            this.right = right;
            this.hash = hash;
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

}
