/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osvm;

import osvm.FCFS.Node;

/**
 *
 * @author Ashad Nadeem
 */
public class FCFSqueue {

        public FCFS fcfsqueue;

        public FCFSqueue() {
                fcfsqueue = new FCFS();

        }

        public FCFS getFcfsqueue() {
                return fcfsqueue;
        }

        public void execute() {
                Node tmp;
                //runing each process
                while (fcfsqueue.getSize() > 0) {
                        tmp = fcfsqueue.front;
                        //all the codes of processes
                        System.out.println((char)27+ "[31m" +"Process executing:" + tmp.data.processcontrolblock.pname);
                        System.out.println("pc: " + tmp.data.processcontrolblock.osvm.programCounter);
                        System.out.println("cc: " + tmp.data.processcontrolblock.osvm.codeCounter);
                        while (tmp.data.processcontrolblock.osvm.codeCounter >  tmp.data.processcontrolblock.osvm.programCounter) {
                                tmp.data.processcontrolblock.osvm.run();
                        }
                        fcfsqueue.dequeue();
                        System.out.println((char)27+ "[31m" +"FCFS process executed: " + tmp.data.processcontrolblock.pname);
                                
                        tmp = tmp.next;
                }
        }

}

class FCFS {
        //        implement first come first served
        public Node front;
        public Node rear;
        public  int size;
        
        static class Node {
                Process data;
                Node next;
                Node() {
                        this.data = null;
                        this.next = null;
                }
                Node(Process data) {
                        this.data = data;
                        this.next = null;
                }
        }
        public  int getSize() {
                return this.size;
        }

        FCFS() { //constructor
                Node tmp = new Node();
                front = tmp;
                rear = tmp;
                this.size = 0;
        }

        public void enqueue(Process data) {
                //insert in the queue
                Node tmp = new Node(data);
                if (isEmpty()) {
                        front = tmp;
                        rear = tmp;
                        this.size = 1;
                        return;
                }
                rear.next = tmp;
                rear = tmp;
                this.size++;
        }

        public Process dequeue() {
                Node tmp = new Node();
                //remove from queue
                Process tobereturn = null;
                if (isEmpty()) {
                        System.err.println("Queue is Empty, Cannot DeQueue");
                } else {
                        tobereturn = front.data;
                        front = front.next;
                        if (front == null) {
                                rear = null;
                        }
                        this.size--;
                }
//                if (tmp.next == front)
//                    tmp.next = null;
                return tobereturn;
        }

        public  Object seek_front() { //returns data of the data at front
                return front.data;
        }

        public Boolean isEmpty() {
                //returns true if queue is empty
                return (front.data == null);
        }

        public void Display() {
                //prints the queue
                Node curr = front;
                System.out.println("Displaying Nodes");
                while (curr != null) {
                        System.out.println(curr.data);
                        curr = curr.next;
                }
                System.out.println();
        }

        public void put_front(Process data) {
                //puts data at front
                Node tmp = new Node(data);
                tmp.next = front;
                front = tmp;
        }

        public Object get_rear() {
                //gets data from rear
                Object tobereturn = null;
                if (isEmpty()) {
                        System.err.println("Queue is Empty, Cannot DeQueue");
                } else {
                        Node curr = front;
                        while (curr.next != rear) {
                                curr = curr.next;
                        }
                        tobereturn = rear.data;
                        curr.next = null;
                        rear = curr;
                }
                return tobereturn;
        }

}

