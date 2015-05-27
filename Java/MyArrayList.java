/*
 * Author: Karley Herschelman, kherschelman2012@my.fit.edu
 * Course: CSE 1002, Section 01/02, Fall 2013
 * Project: myarraylist
 */
import java.util.Arrays;

public class MyArrayList {
    private static final int TEN = 10;
    private static int[] array = new int[0];
    private static int size = 0;

    // doubles the size of the array
    private void ensureCapacity (final int minCapacity) {
        array = Arrays.copyOf(array, array.length * 2);
    }

    // clears contents of the array and then copies the array to initial
    // capacity of 10
    public MyArrayList() {
        clear();
        array = Arrays.copyOf(array, TEN);
    }

    // clears contents of the array and then copies the array to specified
    // initial capacity
    public MyArrayList(final int initialCapacity)
            throws NegativeArraySizeException {
        if (initialCapacity < 0) {
            throw new NegativeArraySizeException();
        }
        clear();
        array = Arrays.copyOf(array, initialCapacity);
    }

    // checks that size will fit in capacity
    // if it doesn't it increases the size using ensure capacity
    // when it does, it sets the value at the next index to the element
    // this adds the element to the end of the list and increase size
    // an element can always be added so this will always return true
    public final boolean add (final Integer element) {
        if (size >= array.length) {
            ensureCapacity(array.length);
        }
        array[size] = element;
        size++;
        return true;
    }

    // checks that the index is within capacity, returns element at index
    public final Integer get (final int index) throws IndexOutOfBoundsException {
        checkRange(index);
        return array[index];
    }

    // checks that the index is within capacity, sets element at this index to
    // new specified element, returns the element that was previously at this
    // index
    // if element is not present, returns false
    public final Integer set (final int index, final Integer element)
            throws IndexOutOfBoundsException {
        checkRange(index);
        final Integer currentElement = array[index];
        array[index] = element;
        return currentElement;
    }

    // checks that the index is within capacity, removes and element using the
    // index, sliding all data past this index one down
    public final Integer remove (final int index)
            throws IndexOutOfBoundsException {
        checkRange(index);
        final Integer currentElement = array[index];
        for (int i = index; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }
        array[array.length - 1] = 0;
        return currentElement;
    }

    // checks that the index is within capacity
    // throws exception is it is not
    public final void checkRange (final int index)
            throws IndexOutOfBoundsException {
        if (index >= array.length) {
            throw new IndexOutOfBoundsException();
        }
    }

    // finds the first matching element in this list and removes it using the
    // index
    public final boolean remove (final Integer element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    // number of elements in list
    public final int size () {
        return size;
    }

    // creates an array of the same capacity, old data goes away
    public final void clear () {
        array = new int[array.length];
    }

}
