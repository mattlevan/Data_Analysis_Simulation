/*
 * Matt Levan
 * CSC 331, Dr. Amlan Chatterjee
 * Data Structures
 *
 * Stack implementation
 * 
 * Must include four methods:
 * push, pop, peek, isEmpty
 *
 */

import java.util.*;
import java.io.*;

interface Stack {
    public boolean push(Object obj);
    public Object pop();
    public Object peek();
    public boolean isEmpty();
}

// Stack implemented using a primitive array

public class StackImplementation implements Stack {
    // Variables

    private int top = -1; // Index of the top element 
    public int count = 0; // Count of elements 
    private int capacity = 100; // Maximum capacity 
    private Object[] stackArray = new Object[capacity];

    // Methods

    public boolean push(Object obj) {
        if (top == capacity - 1) { // Check if top is larger than capacity
            System.out.println("Push of " + obj + " failed! Stack overflow.");
            return false; // Push failed
        }
        else {
            top++;
            count++;
            stackArray[top] = obj; // obj assigned to top of stack
            return true; // Push succeeded
        }
    }

    public Object pop() {
        Object returnValue = null;

        if (!isEmpty()) { // If stack is NOT empty
            returnValue = stackArray[top];
            stackArray[top] = null; // Remove element at top of stack
            top--;
            count--;
        }
        else {
            System.out.println("Pop failed! Stack underflow.");
        }

        return returnValue;
    }

    public Object peek() {
        if (isEmpty()) {
            System.out.println("Peek failed! Stack is empty.");
            return null;
        }

        return stackArray[top];
    }

    public boolean isEmpty() {
        return (top == -1);
    }
}