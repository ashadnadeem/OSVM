/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osvm;

/**
 *
 * @author Ashad Nadeem
 */
public class RoundRobin {

        CircularLinkedList rrqueue;
        int quantum;

        public RoundRobin(int quantum) {
                rrqueue = new CircularLinkedList();
                this.quantum = quantum; //predifined quantum
        }

        public void execute() {
                //need to execute each node qunatum times!!!!
                //the programcounter of each node will change quantum times
                //i have left this job for you Thanks :)
                CircularLinkedList.Node tmp = rrqueue.head;
                int counter = 0;
                boolean check;
                //runing each process
                while (rrqueue.size > 0) {
                        System.out.println((char)27+ "[31m" + "Process executing: " + tmp.data.processcontrolblock.pname);
                        //runnng the process quantum times
                        while (counter < this.quantum) {
                                tmp.data.processcontrolblock.osvm.run();
                                counter++;
                                check = rrqueue.check(tmp);
                                if (check) {
                                        System.out.println((char) 27 + "[31m" + "Time Quantum Left --> Process Ended");
                                        break;
                                }
                        }
                        //qunatum times execution completed
                        System.out.println((char) 27 + "[31m" + "Time Quantum expired --> Process Switched");
                        tmp = tmp.next;
                        counter = 0;
                }
        }

        public CircularLinkedList getQueue() {
                return rrqueue;
        }

}

class CircularLinkedList {

        public Node head;
        public Node tail;
        public int size;

        public CircularLinkedList() {
                Node HT = new Node(null, head, tail);
                head = HT;
                tail = HT;
                head.next = tail;
                tail.next = head;
                size = 0;
        }

        public class Node {

                Process data;
                Node next;
                Node prev;

                public Node(Process data, Node prev, Node next) {
                        this.data = data;
                        this.next = next;
                        this.prev = prev;
                }
        } // end of Node class

        public void insert(Process data) {
                Node tmp = new Node(data, tail, head);
                // If the list is empty
                if (head.data == null) {
                        head.data = data;
                        size++;
                        System.out.println("adding 1st: " + data);
                        return;
                }

                // If list is not empty
                tail.next = tmp;
                tail = tmp;
                tail.next = head;
                size++;
                System.out.println("adding : " + tail.data);
        }

        public void remove(int pid) {
                // If the list has only 1 Node
                if (head == tail) {
                        System.out.println("List has one Node");
                        head.data = null;
                        return;
                }

                // If list is not empty
                Node tmp = head;
                while (tmp.next != head) {
                        if (tmp.next.data.processcontrolblock.pid == pid) {
                                tmp.next = tmp.next.next;
                        } else {
                                tmp = tmp.next;
                        }
                }
                System.gc();
                size--;
                System.out.println("Removed");
        }

        public Node getNode(int pid) {
                Node tmp = head;
                if (head == tail && head.data == null) {
                        System.out.println("List Empty");
                        return null;
                }
                do {
                        if (tmp.data.processcontrolblock.pid == pid) {
                                return tmp;
                        }
                        tmp = tmp.next;
                } while (tmp != head);
                return null;
        }

        public void iterateForward() {
                Node tmp = head;
                if (head == tail && head.data == null) {
                        System.out.println("List Empty");
                        return;
                }
                do {
                        System.out.println(tmp.data);
                        tmp = tmp.next;
                } while (tmp != head);
                System.out.println();
        }

        public void displayList() {
                System.out.println("Printing List");
                Node tmp = head;
                if (head == tail && head.data == null) {
                        System.out.println("List Empty");
                        System.out.println();
                        return;
                }
                do {
                        System.out.println(tmp.data);
                        tmp = tmp.next;
                } while (tmp != head);
                System.out.println();
        }

        public boolean check(Node tmp) {
                if (tmp.data.processcontrolblock.osvm.codeCounter < tmp.data.processcontrolblock.osvm.programCounter) {
                        remove(tmp.data.processcontrolblock.pid);
                        return true;
                }
                return false;
        }

        public String format(Node node) {
                return "Node: " + node.data + " nextNode=" + node.next.data;
        }
// The method check circular traverses the list and checks if the list is circular. It return true if the list
//	is circular and returns false if list is not circular. 

        boolean Check_Circular() {
                //Check if Head and Tail are connected?
                if (tail.next != head) {
                        return false;
                }
                if (head == tail && head.data == null) {
                        return true;
                }
                //Check if Links are not Broken from in between?
                try {
                        //Checking All .next Links
                        Node tmp = head;
                        do {
                                tmp = tmp.next;
                        } while (tmp != head);

                } catch (NullPointerException ex) {
                        //Return False If Any Link Broken
                        return false;
                }
                return true;
        }
}
