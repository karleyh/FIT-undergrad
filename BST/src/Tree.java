/*
 * Author : Karley Herschelman, kherschelman2012@my.fit.edu
 * Course : CSE 2010, Section E1, Spring 2014
 * Project: lab 12, BST
 */
/**
 * @author kherschelman2012
 *
 * @param <Key>
 */
public interface Tree<Key extends Comparable<Key>> {
    /**
     * insert the node into the tree
     * @param node
     */
    public void insert(Node<Key> node);

    /**
     * delete the node from the tree
     * @param node
     * @return
     */
    public Node<Key> delete(Node<Key> node);

    /**
     * insert the node into the tree
     * @param key
     */
    public void insert(Key key);

    /**
     * delete the node from the tree, if key is not present null should be returned
     * @param key
     * @return
     */
    public Node<Key> delete(Key key);

    /**
     * return true if the key is present or false otherwise
     * @param key
     * @return
     */
    public boolean search(Key key);

    /**
     * print inorder tree traversal of the tree
     * @param x
     */
    public void inorderTreeWalk(Node<Key> x);

    /**
     * find the minimum of the tree;
     * @param node to start transversing
     * @return minimum value in tree
     */
    public Node<Key> findMinimum(Node<Key> node);

    /**
     * find the maximum of the tree
     * @param node to start transversing
     * @return maximum value in tree
     */
    public Node<Key> findMaximum(Node<Key> node);

    /**
     * return a String representation of a level-order traversal of a tree
     * @return
     */
    public String showTree();


    /**
     * @return root of tree
     */
    public Node<Key> getRoot();

    /**
     * search for key in tree to see if it is in the tree
     * @param key 
     * @return
     */
    Node searchNode(Key key);

    /**
     * search for the node to return recursively
     * @param n node to start transversing
     * @return true if key is in tree
     */
    Node searchNodeR(Node<Key> n);

    /**
     * search for the node to return if it is there
     * @param node to start transversing
     * @return node where key is in tree
     */
    boolean searchR(Node<Key> n);

}
