/*
 * Author : Karley Herschelman, kherschelman2012@my.fit.edu
 * Course : CSE 2010, Section E1, Spring 2014
 * Project: lab 12, BST
 */

public class BST<Key extends Comparable<Key>> implements Tree<Key> {
    private int nodeCount = 0;
    private Node<Key> root = null;
    private Node<Key> current = null;

    @Override
    public void insert(Node<Key> node) {
        // insert current
        current = node;
        // if root just set root and stop
        if (root == null) {
            root = node;
            return;
        }

        // otherwise insert recursively
        insertR(root);
    }

    public void insertR(Node<Key> node) {
        // if root, don't do anything
        if (node == null) {
            return;
        }

        // if less than the node, move pointer left until reaching a null value,
        // then insert the node
        if (((Key) current.getKey()).compareTo(node.getKey()) < 0) {
            if (node.getLeft() == null) {
                node.setLeft(current);
                current.setParent(node);
            } else {
                insertR(node.getLeft());
            }
        }
        // if greater than the node, move pointer right until reaching a null
        // value, then insert the node
        else {
            if (node.getRight() == null) {
                node.setRight(current);
                current.setParent(node);
            } else {
                insertR(node.getRight());
            }
        }
    }

    @Override
    public void insert(Key key) {
        // insert as current
        current = new Node<Key>(key);

        // root set as root and stop
        if (root == null) {
            root = new Node<Key>(key);
            return;
        }

        // otherwise insert recursively
        insertR(root);
    }

    @Override
    public Node<Key> delete(Node<Key> node) {
        Node<Key> value = node;
        //node cannot be null
        assert node != null;
        // has a parent
        
        Node<Key> present = root;
        boolean goLeft = false;
        
        while (present != null) {
            //finds the node and tells what direction it came from
            if (present.getKey().compareTo(node.getKey()) > 0) {
                present = present.getLeft();
                goLeft = true;
            } else if (present.getKey().compareTo(node.getKey()) == 0) {
                break;
            } else {
                present = present.getRight();
                goLeft = false;
            }
        }
        if (node.getParent() != null) {
            // has a right node
            if (node.getRight() != null) {
                node.getRight().setParent(node.getParent()); // make the parent
                                                             // the parent of
                                                             // the right
                node.getParent().setRight(node.getRight()); // make right the
                                                            // new parent
                if (node.getLeft() != null) {
                    // node.getRight().setLeft(findMinimum(node)); // set the
                    // left
                    // child to the new
                    // parent
                    Node<Key> min = findMinimum(node.getRight());
                    min.setLeft(node.getLeft());
                    node.getLeft().setParent(min);
                }
                //has parent, has left, doesnt have right
            } else if (node.getLeft() != null) {
 
                node.getParent().setRight(node.getLeft());
                node.getLeft().setParent(node.getParent());
            } else {
                // doesnt have a right node
                if (goLeft){
                    node.getParent().setLeft(null);
                }
                else{
                    node.getParent().setRight(null);
                }
                return value;
            }
        }
        // has no parent, left, or right
        if (node.getParent() == null && node.getLeft() == null
                && node.getRight() == null) {
            root = null;
        }
        // has no parent
        if (node.getParent() == null) {
            // has right
            if (node.getRight() != null) {
                Node<Key> right = node.getRight();
                Node<Key> left = node.getLeft();
                Node<Key> parent = node.getParent();
                root = right;
                root.setParent(null);

                if (left != null) {

                    Node<Key> min = findMinimum(right);
                    left.setParent(min);
                    // System.out.println(left.getKey());
                    min.setLeft(left);
                }
            }
            // doesnt have right
            else {
                // has left
                if (node.getLeft() != null) {
                    root = node.getLeft();
                    node.setParent(null);
                }
            }
        }
        // return node deleted
        return value;
    }

    /**
     * delete node where key is held
     * 
     * @param key
     *            data to delete
     * @return node deleted
     */
    @Override
    public Node delete(Key key) {

        // find where the key is held and delete that node
        Node node = searchNode(key);
        return delete(node);
    }

    /**
     * search if the key is in the tree
     * 
     * @param key
     *            to find
     * @return true if the key is in the tree
     */
    @Override
    public boolean search(Key key) {
        // search if the key is in the tree
        current.setKey(key);
        return searchR(root);
    }

    /*
     * (non-Javadoc)
     * @see Tree#searchR(Node)
     */
    @Override
    // find if the value is the the tree
    public boolean searchR(Node<Key> n) {
        Node<Key> found = null;
        if (n == null) {
            return false;
        }//return true when found
        if (n.getKey().equals(current.getKey())) {
            return true;
        }
        //return the value to the left
        if (current.getKey().compareTo((n).getKey()) < 0) {
            return searchR(n.getLeft());
        }
        //return the value to the right
        if (current.getKey().compareTo((n).getKey()) > 0) {
            return searchR(n.getRight());
        }
        return false;
    }

    Node<Key> c = new Node(null);

    // find where the value is in the tree
    public Node searchNode(Key key) {
        c.setKey(key);
        return searchNodeR(root);
    }

    /*
     * (non-Javadoc)
     * @see Tree#searchNodeR(Node)
     */
    @Override
    public Node searchNodeR(Node<Key> n) {
        Node found = null;
        // return null if key is not in tree
        if (n == null) {
            return null;
        }
        // return the node with key when found
        if (n.getKey().equals(c.getKey())) {
            found = n;
            return found;
        }

        // go in the direction the key will be
        if (c.getKey().compareTo((n).getKey()) < 0) {
            found = searchNodeR(n.getLeft());
        }
        if (c.getKey().compareTo((n).getKey()) > 0) {
            found = searchNodeR(n.getRight());
        }
        return found;
    }

    /*
     * (non-Javadoc)
     * @see Tree#inorderTreeWalk(Node)
     */
    @Override
    public void inorderTreeWalk(Node x) {
        root.inorderTreeWalk();
    }

    /*
     * (non-Javadoc)
     * @see Tree#findMinimum(Node)
     */
    @Override
    public Node findMinimum(Node node) {
        // keep going left to find minimum
        if (node.getLeft() == null) {
            return node;
        }
        return findMinimum(node.getLeft());
    }

    /*
     * (non-Javadoc)
     * @see Tree#findMaximum(Node)
     */
    @Override
    public Node findMaximum(Node node) {
        // keep going right to find maximum
        if (node.getRight() == null) {
            return node;
        }
        return findMaximum(node.getRight());
    }

    @Override
    public String showTree() {
        return root.toString();
    }

    /*
     * Queue<Node> q = new LinkedList<Node>();
     * public void levelOrder(Node node) {
     * q.add(node);
     * while (!q.isEmpty()) {
     * node = q.peek();
     * System.out.println(node.getKey());
     * q.remove();
     * // list.add(node);
     * if (node.getLeft() != null) {
     * q.add(node.getLeft());
     * }
     * if (node.getRight() != null) {
     * q.add(node.getRight());
     * }
     * }
     * }
     */

    /*
     * (non-Javadoc)
     * @see Tree#getRoot()
     */
    public Node getRoot() {
        return root;
    }

}
