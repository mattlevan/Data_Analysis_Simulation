/*
 * Matt Levan
 * CSC 331, Dr. Amlan Chatterjee
 * Data Structures
 *
 * Queue implementation
 * 
 * Must include six methods:
 * enqueue, dequeue, isFull, isEmpty, display, reallocate
 *
 */

import java.util.*;
import java.io.*;

interface Queue {
    public boolean isEmpty();
    public boolean isFull();
    public boolean enqueue(Object obj);
    public Object dequeue();
    public void display();
    public void reallocate();
}

public class QueueImplementation implements Queue {
    // Primitive variables

    private static int capacity = 100;
    private Object[] Q = new Object[capacity];
    private int front = 0;
    private int rear = 0;
    private int count = 0;

    // Methods

    public boolean isEmpty() {
        return (count == 0);
    }

    public boolean isFull() {
        return (count == capacity);
    }

    public boolean enqueue(Object obj) {
        if (isFull()) {
            reallocate();
        }
         
        Q[rear] = obj;
        rear = (rear + 1) % capacity;
        count++;

        return true;
    }

    public Object dequeue() {
        Object dequeuedElement; 

        if (isEmpty()) {
            System.out.println("Sorry, dequeue failed. Queue is empty.");
            return null;
        }
        
        dequeuedElement = Q[front];
        front = (front + 1) % capacity;
        count--;

        return dequeuedElement;
    }

    public void display() {
        for (int i = front; i < (front + count); i++) {
            System.out.print(Q[i % capacity] + "\n");
        }

        System.out.println("");
    }

    public void reallocate() {
        Object[] tempArray = new Object[capacity];

        // Copy each item in Q array into tempArray
        for (int i = 0; i < Q.length; i++) {
            tempArray[i] = Q[i];
        }

        capacity = capacity * 2; // Double the capacity
        Object[] Q = new Object[capacity]; // Make new Q array

        for (int i = 0; i < tempArray.length; i++) {
            Q[i] = tempArray[i];
        }
    }
}