/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osvm;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Ashad Nadeem
 */
public class Driver {
        static String cyan = (char)27+ "[36m";
        static String red = (char)27+ "[31m";
        public static Process[] proc;
        public static FCFSqueue fcfs = new FCFSqueue(); //0-15
        public static RoundRobin rr = new RoundRobin(8); //16-31 //8 for quantum
        public static FCFS waiting = new FCFS();// waiting queue

        public static void main(String[] args) throws FileNotFoundException, IOException {
                String[] filearray = {"power", "sfull", "flags", "noop", "large0"};
                proc = new Process[5];
                for (int i = 0; i < 5; i++) {
                        System.out.println(red+ "Process " + filearray[i] + " Executing");
                        proc[i] = new Process();
                        proc[i].initialiseProcess(filearray[i]);
                        System.out.println(cyan + "PCB LOADED");
                        System.out.println(proc[i].processcontrolblock);
                        //Load data
                        proc[i].addPageToPageTable();
                        //Codes in mmory loadng
                        proc[i].loadCodes(filearray[i]);
                        proc[i].fileIS.close();
                        System.out.println(cyan+ "Process " + filearray[i] + " Executed");

                        System.out.println("Adding Process " + filearray[i] + " to waiting queue");
                        waiting.enqueue(proc[i]);//waiting queue enqueing
                }
                //System.out.println("Size: " + waiting.size);
                System.out.println("");

                System.out.println("checking priority and enqueing them in respective queues");
                checkPriority();//checking priority and enqueing them in respective queues
                System.out.println("checking completed");

                System.out.println("executing MultiLevelScheduling");
                executeMultiLevelScheduling();
                System.out.println("Execution completed completed");
        }

        public static void checkPriority() {
                Process temp;
                while (waiting.getSize() > 0) {
                        System.out.println("Size: " + waiting.getSize());
                        temp = waiting.dequeue();
                        System.out.println("Dequing from waiting: " + temp.processcontrolblock.getPname());
                        if (temp.processcontrolblock.ppriority <= 15 && temp.processcontrolblock.ppriority >= 0) {
                                fcfs.getFcfsqueue().enqueue(temp);
                                System.out.println("Added to FIRST COME FIRST SERVE, tSize: " + fcfs.getFcfsqueue().getSize());
                        } else {
                                if (temp.processcontrolblock.ppriority <= 31 && temp.processcontrolblock.ppriority >= 16) {
                                        rr.getQueue().insert(temp);
                                        System.out.println("Added to ROUND ROBIN, tSize: " + rr.getQueue().size);
                                }
                                else {
                                        throw new IllegalArgumentException("Priority Cant be less then 0 and cant be greater than 31");
                                }
                        }
                }
        }

        public static void executeMultiLevelScheduling() {
                //execute all the process in FCFS
//                System.out.println("Executing FCFS");
                System.out.println(red +"Executing FCFS");
                //System.out.println("FCFS Queue size: "+ fcfs.getFcfsqueue().getSize());
                fcfs.execute();
                System.out.println(red +"Executed FCFS successfully");
//                System.out.println("Executed FCFS successfully");
                //Till this point FCFS will be empty
                
                System.out.println("");
                System.out.println("");
                
                //execute all the process in RoundRobin
                System.out.println(red+"Executing roundrobin");
//                System.out.println("Executing roundrobin");
                rr.execute();
                System.out.println(red +"Executed RoundRobin successfully");
//                System.out.println("Executed RoundRobin successfully");
                //Till this point round robin will be empty

        }

}
