/*
 * Author : Karley Herschelman, kherschelman2012@my.fit.edu
 * Course : CSE 2010, Section E1, Spring 2014
 * Project: lab 12, BST
 */

import java.util.LinkedList;
import java.util.Queue;

public class Node<Key extends Comparable<Key>> {
    private Node<Key> left = null;
    private Node<Key> right = null;
    private Key key = null;
    private Node<Key> parent = null;
    private int height = 0;

    /**
     * @return the left
     */
    public Node<Key> getLeft() {
        return left;
    }

    /**
     * @param left
     *            the left to set
     */
    public void setLeft(Node<Key> left) {
        this.left = left;
    }

    /**
     * @return the right
     */
    public Node<Key> getRight() {
        return right;
    }

    /**
     * @param right
     *            the right to set
     */
    public void setRight(Node<Key> right) {
        this.right = right;
    }

    /**
     * @return the parent
     */
    public Node<Key> getParent() {
        return parent;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(Node<Key> parent) {
        this.parent = parent;
    }

    /**
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * @param key2
     *            the key to set
     */
    public void setKey(Key key2) {
        this.key = key2;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * constructor
     * 
     * @param key
     *            data of node
     */
    public Node(Key key) {
        this.key = key;
        // height = (int)(Math.log(count)/Math.log(2));
    }

    /**
     * level order traversal
     * 
     * @returns level order traversal starting at this node
     */
    @Override
    public String toString() {
        assert this != null;
        Queue<Node> q = new LinkedList<Node>();
        String s = "";
        q.add(this);
        while (!q.isEmpty()) {
            Node node = q.peek();
            s += node.getKey() + " ";
            q.remove();
            if (node.getLeft() != null) {
                q.add(node.getLeft());
            }
            if (node.getRight() != null) {
                q.add(node.getRight());
            }
        }
        return s;
    }

    /**
     * inorder traversal
     */
    public void inorderTreeWalk() {
        assert this != null;
        if (this == null) {
            System.out.println("");
            return;
        }
        if (this.getLeft() != null) {
            this.getLeft().inorderTreeWalk();
        }
        System.out.print(this.getKey() + " ");
        if (this.getRight() != null) {
            this.getRight().inorderTreeWalk();
        }

    }

}
