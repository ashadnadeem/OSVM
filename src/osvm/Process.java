/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osvm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author naqui
 */
public class Process {

        //public OSVM m = new OSVM();
        public PCB processcontrolblock;
        public int memoryval = 0;
        public short datasize = 0;
        public int codesize;
        public FileInputStream fileIS;
        public int startLocation;
        public int endLocation;

        public Process() {
        }

        private Byte priority;

        public void initialiseProcess(String nameOfFile) throws FileNotFoundException, IOException {
                processcontrolblock = new PCB();
                fileIS = new FileInputStream(new File(nameOfFile));
                processcontrolblock.pname = nameOfFile;

                priority = (byte) fileIS.read(); //read priority
                if (priority >= 0 && priority < 32) {
                        processcontrolblock.ppriority = priority;
                }

                byte secondbyte = (byte) fileIS.read(); //read process id
                byte thirdbyte = (byte) fileIS.read(); //read process id

                processcontrolblock.pid = Bytetoshort(secondbyte, thirdbyte);
                //int processid = processcontrolblock.pid;

                byte fourthbyte = (byte) fileIS.read(); //read datasize
                byte fifthbyte = (byte) fileIS.read(); // read datasize

                processcontrolblock.psize = Bytetoshort(fourthbyte, fifthbyte);
                datasize = processcontrolblock.psize;
                fileIS.skip(3);
        }

        public void loadCodes(String filearray) throws IOException {
                //loading the process codes into memory block right after
                // first 8 bytes and the data size
                this.processcontrolblock.osvm.loadMemory(filearray, 8 + datasize);
        }

        public void addPageToPageTable() throws IOException {
                processcontrolblock.data_pagetable = new PageTable();
                int count = 0;
                for (int i = memoryval; i <= memoryval + datasize; i++) {
                        //initialising data into memory block
                        //right after the codes
                        this.processcontrolblock.osvm.memory[i] = (byte) fileIS.read();
                        if (i % 128 == 0) {
                                processcontrolblock.data_pagetable.AddEntry((byte) this.processcontrolblock.osvm.memory[i], (byte) this.processcontrolblock.osvm.memory[i + 127]);
                        }
                        count = i;
                }
                memoryval = memoryval + datasize;
                if (memoryval % 128 != 0) {
                        memoryval = (memoryval) + (128 - (memoryval % 128));
                }
                startLocation = 0;
                endLocation = count;
        }

        public short Bytetoshort(byte Fbyte, byte Sbyte) { 
                //concat two bytes into short
                short temp = (short) (Fbyte * 256);
                temp = (short) (temp + Sbyte);
                return temp;
        }

}
