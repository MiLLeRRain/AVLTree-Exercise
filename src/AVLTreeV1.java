import javafx.scene.control.TabPane;

/**
 * It is an AVL Tree, <PersonNode> is the Node here.
 */
public class AVLTreeV1{ //<PersonNode extends Comparable<? super PersonNode>>

    /**
     * Root Node
     */
    private PersonNode root;
    /**
     * Tree nodes size
     */
    private int size;
    /**
     * A place to hold the new Person who will be inserted to Node.
     */
    private Person newPerson;

    /**
     * Initialize an empty tree
     */
    public AVLTreeV1() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Obtain the tree height, using depth because this Node is a VBox has its own height
     * No empty tree root at height 1, empty tree height = 0.
     */
    private int depth() {
        return depth(root);
    }

    private int depth(PersonNode node) {
        if (node != null) return node.depth;
        return 0;
    }

    /**
     * Pre-order
     */
    public void preOrder() {
        preOrder(root, "");
    }

    private void preOrder(PersonNode node, String s) {
        if (node != null) {
            System.out.println(s + node);
            preOrder(node.left, s + "-");
            preOrder(node.right, s + "-");
        }
    }

    /**
     * In-order
     */
    public void inOrder() {
        inOrder(root, "");
    }

    private void inOrder(PersonNode node, String s) {
        if (node != null) {
            inOrder(node.left, s + "-");
            System.out.println(s + node);
            inOrder(node.right, s + "-");
        }
    }

    /**
     * Post-order
     */
    public void postOrder() {
        postOrder(root, "");
    }

    private void postOrder(PersonNode node, String s) {
        if (node != null) {
            postOrder(node.left, s + "-");
            postOrder(node.right, s + "-");
            System.out.println(s + node);
        }
    }

    /**
     * Input name as key, no matter it is first name or last name.
     * If return null, then the node with this key has not been created. Use in main class.
     *
     * @param key is the name input by user
     * @return the first found Person
     */
    public PersonNode search(String key) {
        return search(root, key);
    }

    /**
     * Recursively find all the Person fit the only key.
     *
     * @param node starts with root, and then its child
     * @param key  could be name+uid or surname+uid, depends on the tree we are using.
     *             The user does not have to know.
     * @return the first found Person
     */
    private PersonNode search(PersonNode node, String key) {
        if (node == null) return null;
        /* Compare the key and give out the direction */
        int direction = key.compareTo(node.key);
        if (direction < 0) return search(node.left, key);
        else if (direction > 0) return search(node.right, key);
        else return node;
    }

    /**
     * LLRotation of root
     *
     * @param pn2 the parent node of the heavier left branch
     * @return root after rotation (old root's left)
     */
    private PersonNode LLRotation(PersonNode pn2) {
        // Assign a new node to be root's left.
        PersonNode pn1;
        pn1 = pn2.left;

        // Cut off pn1's left attach to pn2's right.
        pn2.left = pn1.right;
        // Lift pn1 up to be new root.
        pn1.right = pn2;

        // Update depth/height of two nodes.
        pn2.depth = Math.max(depth(pn2.left), depth(pn2.right)) + 1;
        pn1.depth = Math.max(depth(pn1.left), pn2.depth) + 1;

        return pn1;
    }

    /**
     * RRRotation of root
     *
     * @param pn1 the parent node of the heavier right branch
     * @return root after rotation (old root's right)
     */
    private PersonNode RRRotation(PersonNode pn1) {
        // Assign a new node to be root's right
        PersonNode pn2;
        pn2 = pn1.right;

        // Cut off pn2's left attach to pn1's right.
        pn1.right = pn2.left;
        // Lift pn2 up to be new root.
        pn2.left = pn1;

        // Update depth/height of two nodes.
        pn1.depth = Math.max(depth(pn1.left), depth(pn1.right)) + 1;
        pn2.depth = Math.max(depth(pn2.right), pn1.depth) + 1;

        return pn2;
    }

    /**
     * LRRotation = RR at root's left + LL at root.
     *
     * @param pn3 the parent node of heavier left branch need a RRRotation then LLRotation.
     * @return root after rotation (old root's left)
     */
    private PersonNode LRRotation(PersonNode pn3) {
        // Do a RRRotation at the root's left
        pn3.left = RRRotation(pn3.left);
        // Do a LLRotation at the root.
        return LLRotation(pn3);
    }

    /**
     * RLRotation = LL at root's right + RR at root.
     *
     * @param pn3 the parent node of heavier right branch need a LLRotation then RRRotation
     * @return root after rotation (old root's right)
     */
    private PersonNode RLRotation(PersonNode pn3) {
        // Do a LLRotation at the root's right
        pn3.right = LLRotation(pn3.right);
        // Do a RRRotation at the root.
        return RRRotation(pn3);
    }

    /**
     * Insert a new Person
     *
     * @param key the key of the node needed to be created, or existing node with this key
     * @param p   is the new Person need to be inserted
     */
    public void insert(String key, Person p) {
        newPerson = p; // Assign the new Person p to the helper field
        root = insert(root, key);
        // After tree is updated or not because it is already there, insert the new Person.
        insertPersonToNode(key, newPerson);
        size++;
    }

    /**
     * Recursive insert helper
     *
     * @param pn  the PersonNode currently being processed
     * @param key the key of the node needed to be created, or existing node with this key
     * @return the Node after maintenance, and the root Node at final step recursion.
     */
    private PersonNode insert(PersonNode pn, String key) {
        // Make a new tree from pn if its empty.
        if (pn == null) {
            pn = new PersonNode(key, null, null);
        }

        else {
            int direction = key.compareTo(pn.key);
            if (direction < 0) { // Enter left branch
                pn.left = insert(pn.left, key);
                // If left branch is too heavy, deal with LLR or LRR situation
                if (depth(pn.left) - depth(pn.right) == 2) {
                    if (key.compareTo(pn.left.key) < 0) pn = LLRotation(pn);
                    else pn = LRRotation(pn);
                }
            } else if (direction > 0) { // Enter right branch
                pn.right = insert(pn.right, key);
                // If right branch is too heavy, deal with RRR or RLR situation
                if (depth(pn.right) - depth(pn.left) == 2) {
                    if (key.compareTo(pn.right.key) > 0) pn = RRRotation(pn);
                    else pn = RLRotation(pn);
                }
            }
        }

        pn.depth = Math.max(depth(pn.left), depth(pn.right)) + 1;

        return pn;
    }

    /**
     * Insert the person into the Node box.
     *
     * @param key is the input to obtain the PersonNode
     * @param p   is the new person to be inserted into map
     */
    private void insertPersonToNode(String key, Person p) {
        PersonNode pn = search(key);
        if (pn != null) pn.insertPerson(p);
    }

    /**
     * Remove the person the user wants.
     *
     * @param key is the key of the targeting PersonNode
     * @param p   is the person need to be removed
     */
    public void remove(String key, Person p) {
        PersonNode target = search(key);
        // If found this target, then proceed.
        if (target != null) {
            // Remove the Person p from the Node box.
            // If successful and the box is now empty, remove this node, maintain the Tree.
            if (target.removePerson(p) != null && target.isEmpty()) {
                root = remove(root, target);
            }
            size--;
        }
    }

    /**
     * Recursive remove helper
     *
     * @param pn     is the PersonNode currently being processed
     * @param target is the targeting PersonNode
     * @return the Node after maintenance, and the root Node at final step recursion.
     */
    private PersonNode remove(PersonNode pn, PersonNode target) {
        if (pn == null) return null;

        int direction = target.compareTo(pn);
        if (direction < 0) { // Enter left branch
            pn.left = remove(pn.left, target);
            // If right branch is too heavy, deal with RLR or RRR situation
            if (depth(pn.right) - depth(pn.left) == 2) {
                PersonNode pnr = pn.left;
                if (depth(pnr.left) > depth(pnr.right)) pn = RLRotation(pn);
                else pn = RRRotation(pn);
            }
        } else if (direction > 0) { // Enter right branch
            pn.right = remove(pn.right, target);
            // If left branch is too heavy, deal with LRR or LLR situation
            if (depth(pn.left) - depth(pn.right) == 2) {
                PersonNode pnl = pn.right;
                if (depth(pnl.right) > depth(pnl.left)) pn = LRRotation(pn);
                else pn = LLRotation(pn);
            }
        } else { // At the target
            if (pn.left != null && pn.right != null) {
                // Find the node with max key on the left side, replace the target with it.
                if (depth(pn.left) > depth(pn.right)) {
                    PersonNode max = findMax(pn.left);
                    pn.key = max.key;
                    pn.updateText();
                    pn.left = remove(pn.left, max);
                }
                // Find the node with min key on the right side, replace the target with it.
                else {
                    PersonNode min = findMin(pn.right);
                    pn.key = min.key;
                    pn.updateText();
                    pn.right = remove(pn.right, min);
                }
            } else {
                // Replace the target with its non-null branch.
                if (pn.left != null) pn = pn.left;
                else pn = pn.right;
            }
        }

        return pn;
    }

    /**
     * Find the node with max key
     * @param pn starting point
     * @return the node with max key
     */
    private PersonNode findMax(PersonNode pn) {
        if (pn == null) return null;
        PersonNode toReturn = pn.right;
        while (toReturn.right != null) {
            toReturn = toReturn.right;

        }
        return toReturn;
    }

    /**
     * Find the node with min key
     * @param pn starting point
     * @return the node with min key
     */
    private PersonNode findMin(PersonNode pn) {
        if (pn == null) return null;
        PersonNode toReturn = pn.left;
        while (toReturn.left != null) {
            toReturn = toReturn.left;
        }
        return toReturn;
    }

    /**
     * Empty the whole tree including root
     */
    public void destroy() {
        this.root = null;
        this.size = 0;
    }

}
