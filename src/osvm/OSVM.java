/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osvm;

import java.io.File;  // Import the File class
import java.io.FileInputStream;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ashad Nadeem
 */
public class OSVM {

        private final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray(); //output array
        short[] memory = new short[65536]; //memory array
        short[] gpr = new short[16]; //general purpose register array
        short[] spr = new short[16]; //special purpose register array
        boolean[] flagreg = new boolean[16]; //flag register array
        short programCounter = 0; //program counter declaration and initialisation
        int codeCounter; //code counter declaration
        short codebase = 0; //code base declaration and initialisation
        short instructionReg; //instruction register declaration
        String[] rii = new String[4]; //register immeditae instruction array
        String[] rri = new String[3]; //register register instruction array
        String[] mi = new String[4]; //memory instruction array
        String[] soi = new String[2]; //single operand instruction array
        String[] noi = new String[1]; //no operand instruction array
        Stack stck = new Stack();

        public void main(String[] args) {
                 // TODO code application logic here
                //loading input file
               loadMemory("p0.txt", 0);
               //System.out.println(memory);
               initialiseMemory(); //making the memory array work to start the process
               run(); //calling method for executing the input
        }

        public OSVM() {
        }

        public void initialiseMemory() {
                for (int i = 0; i < gpr.length; i++) {
                        gpr[i] = 0; //initialising all the elements of the general purpose register by zero
                }
                for (int i = 0; i < spr.length; i++) {
                        spr[i] = 0; //initialising all the elements of the special purpose register by zero
                }
                //Code counter stored in SPR
                //System.out.println("code counter:" + spr[2]);
                spr[2] = (short) (codeCounter - 1);
                //System.out.println("code counter:" + spr[2]);
        }

        //function to load the input array into memory by reading file
        public void loadMemory(String filename, int reference)  {
                try {
                        FileInputStream fileIS = new FileInputStream(new File(filename));
                        //To skip till reference number of bytes
                        fileIS.skip(reference);

                        while (fileIS.available() > 0) {
                                memory[codeCounter] = Short.parseShort(fileIS.read() + "");
                                codeCounter++;
                        }
                        fileIS.close();

                } catch (FileNotFoundException e) {
                        System.out.println("An error occurred.");
                } catch (IOException ex) {
                        Logger.getLogger(OSVM.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        public void decodeInstruction() {
        }

        public void loadPCtoIR() {
                //Load contents of PC to IR
                programCounter = spr[9];
                instructionReg = memory[programCounter];//make function
                spr[10] = instructionReg;
        }

        public void incrementPC() {
                //Increment PC
                programCounter++;
                spr[9] = Short.parseShort(programCounter + "");
        }

        //converting the instruction (in decimal) in the instruction register to hex
        public String IRtoHex(short IR) {
                String hexi = Integer.toHexString(IR);
                return hexi;
        }

        public void run() {
                //run and call all instructions one by one

//        while (programCounter != codeCounter) {
                loadPCtoIR();
                incrementPC();
                //calling method to convert IR contents to hex
                callInstruction(IRtoHex(instructionReg));

                //}
        }

//        public String bytesToHex(byte[] bytes) {
//                char[] hexChars = new char[bytes.length * 2];
//                for (int j = 0; j < bytes.length; j++) {
//                        int v = bytes[j] & 0xFF;
//                        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
//                        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
//                }
//                return new String(hexChars);
//        }
        public void callInstruction(String hexi) {
                //calling an instruction and printing the current program counter, the instruction 
                //register and the hexadecimal conversion of that instruction
                System.out.println((char) 27 + "[36m" + "PC: " + programCounter + ", IR: " + instructionReg + ", CC: " + codeCounter);
                System.out.println("Instuction called: " + hexi);

                //  while (!hexi.equals("f3")){
                switch (hexi) {
                        //register-register Instructions 
                        case "16":
                                RegRegInstr(hexi);
                                MOV();
                                break;
                        case "17":
                                RegRegInstr(hexi);
                                ADD();
                                break;
                        case "18":
                                RegRegInstr(hexi);
                                SUB();
                                break;
                        case "19":
                                RegRegInstr(hexi);
                                MUL();
                                break;
                        case "1a":
                                RegRegInstr(hexi);
                                DIV();
                                break;
                        case "1b":
                                RegRegInstr(hexi);
                                AND();
                                break;
                        case "1c":
                                RegRegInstr(hexi);
                                OR();
                                break;

                        //Register-Immediate Instructions
                        case "30":
                                RegImInstr(hexi);
                                MOVI();
                                break;
                        case "31":
                                RegImInstr(hexi);
                                ADDI();
                                break;
                        case "32":
                                RegImInstr(hexi);
                                SUBI();
                                break;
                        case "33":
                                RegImInstr(hexi);
                                MULI();
                                break;
                        case "34":
                                RegImInstr(hexi);
                                DIVI();
                                break;
                        case "35":
                                RegImInstr(hexi);
                                ANDI();
                                break;
                        case "36":
                                RegImInstr(hexi);
                                ORI();
                                break;
                        case "37":
                                RegImInstr(hexi);
                                BZ(hexi);
                                break;
                        case "38":
                                RegImInstr(hexi);
                                BNZ(hexi);
                                break;
                        case "39":
                                RegImInstr(hexi);
                                BC(hexi);
                                break;
                        case "3a":
                                RegImInstr(hexi);
                                BS(hexi);
                                break;
                        case "3b":
                                RegImInstr(hexi);
                                JMP(hexi);
                                break;
                        case "3c":
                                RegImInstr(hexi);
                                CALL();
                                break;
                        case "3d":
                                RegImInstr(hexi);
                                ACT();
                                break;

                        //memory instructions
                        case "51":
                                MemInstr(hexi);
                                MOVL();
                                break;

                        case "52":
                                MemInstr(hexi);
                                MOVS();
                                break;

                        //single operand instruction
                        case "71":
                                SinOpInstr(hexi);
                                SHL();
                                break;

                        case "72":
                                SinOpInstr(hexi);
                                SHR();
                                break;

                        case "73":
                                SinOpInstr(hexi);
                                RTL();
                                break;

                        case "74":
                                SinOpInstr(hexi);
                                RTR();
                                break;

                        case "75":
                                SinOpInstr(hexi);
                                INC();
                                break;

                        case "76":
                                SinOpInstr(hexi);
                                DEC();
                                break;

                        case "77":
                                SinOpInstr(hexi);
                                PUSH();
                                break;

                        case "78":
                                SinOpInstr(hexi);
                                POP();
                                break;

                        //no operand instructions
                        case "f1":
                                NoOpInstr(hexi);
                                RETURN();
                                break;

                        case "f2":
                                NoOpInstr(hexi);
                                NOOP();
                                break;

                        case "f3":
                                NoOpInstr(hexi);
                                END();
                                break;

                }
                //   }

                ///  throw new UnsupportedOperationException("Not supported yet.");
        }

        //UPDATING THE FLAG REGISTER\
        //arithmetic, logical, shift and rotate operations
        private void checkZero(short gpr) {

                if (gpr == 0) { //checking whether the result of the operation is 0
                        flagreg[1] = true;
                } else {
                        flagreg[1] = false;
                }
        }

        //checking carry bit for shift and rotate operations
        private void checkCarry(char msb) {
                if (msb == '1') {
                        flagreg[0] = true; //msb is true
                } else if (msb == '0') {
                        flagreg[0] = false;
                }
        }

        //checking sign bit for arithmetic, logical, shift and rotate operations
        private void checkSign(char msb) {
                if (msb == '1') {
                        flagreg[2] = true; //if negative
                } else {
                        flagreg[2] = false; //if positive
                }
        }

        //checking overflow for arithmetic and logical operations 
        private void checkOverflow(Short arithmetic) {
                if (arithmetic > Math.pow(2, 16)) {
                        flagreg[3] = true;
                } else {
                        flagreg[3] = false;
                }
        }

        //trimming the most significant bit from the binary value
        private void MSB(String bin) {
                char msb = bin.trim().charAt(0);
                checkSign(msb);
        }

        //converting the instruction to binary
        private String toBinary(Short gpr) {
                String bin = Integer.toBinaryString(gpr);
                //Fit the extra zeros before if necessary
                while (bin.length() <= 15) {
                        bin = '0' + bin;
                }
                return bin;
        }

        private void MOV() {
                System.out.println("MOV is running");
                //loading the index of first register
                int reg1 = Integer.parseInt(rri[1], 16);
                //loading the index of second register
                int reg2 = Integer.parseInt(rri[2], 16);
                String HexNum = Integer.toHexString(reg1) + Integer.toHexString(reg2);
                //Copy register contents
                gpr[reg1] = gpr[reg2];

                checkZero(gpr[reg1]);
                //Print values
        }

        private void ADD() {
                System.out.println("ADD is running");
                int reg1 = Integer.parseInt(rri[1], 16);
                int reg2 = Integer.parseInt(rri[2], 16);
                String HexNum = Integer.toHexString(reg1) + Integer.toHexString(reg2);
                //Add register contents
                gpr[reg1] = (short) (gpr[reg1] + gpr[reg2]);

                //checking sign, overflow and zero bits
                MSB(toBinary(gpr[reg1]));
                checkOverflow(gpr[reg1]);
                checkZero(gpr[reg1]);
                //Print values
        }

        private void SUB() {
                System.out.println("SUB is running");
                int reg1 = Integer.parseInt(rri[1], 16);
                int reg2 = Integer.parseInt(rri[2], 16);
                String HexNum = Integer.toHexString(reg1) + Integer.toHexString(reg2);
                //subtract register contents
                gpr[reg1] = (short) (gpr[reg1] - gpr[reg2]);

                //checking sign, overflow and zero bits
                MSB(toBinary(gpr[reg1]));
                checkOverflow(gpr[reg1]);
                checkZero(gpr[reg1]);
                //Print values
        }

        private void MUL() {
                System.out.println("MUL is running");
                int reg1 = Integer.parseInt(rri[1], 16);
                int reg2 = Integer.parseInt(rri[2], 16);
                String HexNum = Integer.toHexString(reg1) + Integer.toHexString(reg2);
                //multiply register contents
                gpr[reg1] = (short) (gpr[reg1] * gpr[reg2]);

                //checking sign, overflow and zero bits
                checkOverflow(gpr[reg1]);
                MSB(toBinary(gpr[reg1]));
                checkZero(gpr[reg1]);
                //Print values
        }

        private void DIV() {
                System.out.println("DIVis running");
                int reg1 = Integer.parseInt(rri[1], 16);
                int reg2 = Integer.parseInt(rri[2], 16);
                String HexNum = Integer.toHexString(reg1) + Integer.toHexString(reg2);
                //divide register contents
                gpr[reg1] = (short) (gpr[reg1] / gpr[reg2]);

                //checking sign, overflow and zero bits
                checkOverflow(gpr[reg1]);
                MSB(toBinary(gpr[reg1]));
                checkZero(gpr[reg1]);
                //Print values
        }

        private void AND() {
                System.out.println("AND is running");
                int reg1 = Integer.parseInt(rri[1], 16);
                int reg2 = Integer.parseInt(rri[2], 16);
                String HexNum = Integer.toHexString(reg1) + Integer.toHexString(reg2);
                //logical AND register contents
                gpr[reg1] = (short) (gpr[reg1] & gpr[reg2]);
                //checking sign, overflow and zero bits
                checkOverflow(gpr[reg1]);
                checkZero(gpr[reg1]);
                MSB(toBinary(gpr[reg1]));
                //Print values
        }

        private void OR() {
                System.out.println("OR is running");
                int reg1 = Integer.parseInt(rri[1], 16);
                int reg2 = Integer.parseInt(rri[2], 16);
                String HexNum = Integer.toHexString(reg1) + Integer.toHexString(reg2);
                //logical OR register contents
                gpr[reg1] = (short) (gpr[reg1] | gpr[reg2]);
                //checking sign, overflow and zero bits
                checkOverflow(gpr[reg1]);
                MSB(toBinary(gpr[reg1]));
                checkZero(gpr[reg1]);
                //Print values
        }

        private void MOVI() {
                try {
                        System.out.println("MOVI is running");
                        short val1 = Short.parseShort(rii[2], 16); //00
                        short val2 = Short.parseShort(rii[3], 16); //01
                        String HexNum = Integer.toHexString(val1) + Integer.toHexString(val2); //00 01
                        //Copy Immediate to register
                        gpr[Integer.parseInt(rii[1])] = Short.parseShort(HexNum, 16);
                        //checking sign, overflow and zero bits
                        checkOverflow(gpr[Integer.parseInt(rii[1])]);
                        MSB(toBinary(gpr[Integer.parseInt(rii[1])]));
                        checkZero(gpr[Integer.parseInt(rii[1])]);
                        //Print values
                } catch (Exception e) {
                        
                }
        }

        private void ADDI() {
                System.out.println("ADDI is running");
                short val1 = Short.parseShort(rii[2], 16);
                short val2 = Short.parseShort(rii[3], 16);
                String HexNum = Integer.toHexString(val1) + Integer.toHexString(val2);
                //R1 = R1 + HexNum

                int reg1 = Integer.parseInt(rii[1], 16);
                //gpr[reg1] = Short.parseShort(HexNum, 16); --R1
                gpr[reg1] = (short) (gpr[reg1] + Short.parseShort(HexNum, 16));

                //checking sign, overflow and zero bits
                MSB(toBinary(gpr[reg1]));
                checkOverflow(gpr[reg1]);
                checkZero(gpr[reg1]);
                //Print values

        }

        private void SUBI() {
                System.out.println("SUBI is running");
                short val1 = Short.parseShort(rii[2], 16);
                short val2 = Short.parseShort(rii[3], 16);
                String HexNum = Integer.toHexString(val1) + Integer.toHexString(val2);
                //R1 = R1 - HexNum

                int reg1 = Integer.parseInt(rii[1], 16);
                //gpr[reg1] = Short.parseShort(HexNum, 16); --R1
                gpr[reg1] = (short) (gpr[reg1] - Short.parseShort(HexNum, 16));
                //checking sign, overflow and zero bits
                MSB(toBinary(gpr[reg1]));
                checkOverflow(gpr[reg1]);
                checkZero(gpr[reg1]);
                //Print values
        }

        private void DIVI() {
                System.out.println("DIVI is running");
                short val1 = Short.parseShort(rii[2], 16);
                short val2 = Short.parseShort(rii[3], 16);
                String HexNum = Integer.toHexString(val1) + Integer.toHexString(val2);
                //R1 = R1 / HexNum

                int reg1 = Integer.parseInt(rii[1], 16);
                //gpr[reg1] = Short.parseShort(HexNum, 16); --R1
                gpr[reg1] = (short) (gpr[reg1] / Short.parseShort(HexNum, 16));
                //checking sign, overflow and zero bits
                checkOverflow(gpr[reg1]);
                MSB(toBinary(gpr[reg1]));
                checkZero(gpr[reg1]);
                //Print values
        }

        private void ANDI() {
                System.out.println("ANDI is running");
                short val1 = Short.parseShort(rii[2], 16);
                short val2 = Short.parseShort(rii[3], 16);
                String HexNum = Integer.toHexString(val1) + Integer.toHexString(val2);
                //R1 = R1 & HexNum

                int reg1 = Integer.parseInt(rii[1], 16);
                //gpr[reg1] = Short.parseShort(HexNum, 16); --R1
                gpr[reg1] = (short) (gpr[reg1] & Short.parseShort(HexNum, 16));
                //checking sign, overflow and zero bits
                checkOverflow(gpr[reg1]);
                MSB(toBinary(gpr[reg1]));
                checkZero(gpr[reg1]);
                //Print values
        }

        private void MULI() {
                System.out.println("MULI is running");
                short val1 = Short.parseShort(rii[2], 16);
                short val2 = Short.parseShort(rii[3], 16);
                String HexNum = Integer.toHexString(val1) + Integer.toHexString(val2);
                //R1 = R1 * HexNum

                int reg1 = Integer.parseInt(rii[1], 16);
                //gpr[reg1] = Short.parseShort(HexNum, 16); --R1
                gpr[reg1] = (short) (gpr[reg1] * Short.parseShort(HexNum, 16));
                //checking sign, overflow and zero bits
                checkOverflow(gpr[reg1]);
                MSB(toBinary(gpr[reg1]));
                checkZero(gpr[reg1]);
                //Print values
        }

        private void ORI() {
                System.out.println("ADDI is running");
                short val1 = Short.parseShort(rii[2], 16);
                short val2 = Short.parseShort(rii[3], 16);
                String HexNum = Integer.toHexString(val1) + Integer.toHexString(val2);
                //R1 = R1 | HexNum

                int reg1 = Integer.parseInt(rii[1], 16);
                //gpr[reg1] = Short.parseShort(HexNum, 16); --R1
                gpr[reg1] = (short) (gpr[reg1] | Short.parseShort(HexNum, 16));
                //checking sign, overflow and zero bits
                checkOverflow(gpr[reg1]);
                MSB(toBinary(gpr[reg1]));
                checkZero(gpr[reg1]);
                //Print values
        }

        //Branch if equal to zero
        private void BZ(String hex) {
                if (flagreg[1] == false) {
                        //adding the offset to the codebase and updating it to the program counter
                        programCounter = (short) (codebase + Short.parseShort(hex, 16));
                }
        }

        //Branch if not equal to zero
        private void BNZ(String hex) {
                if (flagreg[1] == true) {
                        //adding the offset to the codebase and updating it to the program counter
                        programCounter = (short) (codebase + Short.parseShort(hex, 16));
                }
        }

        //Branch if carry
        private void BC(String hex) {
                if (flagreg[0] == true) {
                        //adding the offset to the codebase and updating it to the program counter
                        programCounter = (short) (codebase + Short.parseShort(hex, 16));
                }
        }

        //Branch if sign
        private void BS(String hex) {
                if (flagreg[2] == true) {
                        //adding the offset to the codebase and updating it to the program counter
                        programCounter = (short) (codebase + Short.parseShort(hex, 16));
                }
        }

        private void JMP(String hex) {
                //adding the offset to the codebase and updating it to the program counter
                programCounter = (short) (codebase + Short.parseShort(hex, 16));
        }

        private void CALL() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        private void ACT() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        private void MOVL() {
                // R1  Mem [location**] 
        }

        private void MOVS() {

        }
        //Shift left

        private void SHL() {
                System.out.println("SHL is running");
                int reg1 = Integer.parseInt(soi[1], 16);
                String bin = Integer.toBinaryString(gpr[reg1]);
                char msb = bin.trim().charAt(0); //checking msb before operation
                checkCarry(msb);
                gpr[reg1] = (short) (gpr[reg1] << 1);
                String HexNum = Integer.toHexString(reg1);
                //add carry 
                checkZero(gpr[reg1]);
                msb = bin.trim().charAt(0);
                checkSign(msb);
                //Print values
        }

        //shift right
        private void SHR() {
                System.out.println("SHR is running");

                int reg1 = Integer.parseInt(soi[1], 16);
                String bin = Integer.toBinaryString(gpr[reg1]);
                String HexNum = Integer.toHexString(reg1);
                char msb = bin.trim().charAt(0); //checking msb before operation
                checkCarry(msb);
                gpr[reg1] = (short) (gpr[reg1] >> 1);

                //add carry
                checkZero(gpr[reg1]);
                msb = bin.trim().charAt(0);
                checkSign(msb);
                //Print values
        }

        //rotate left
        private void RTL() {
                int reg1 = Integer.parseInt(soi[1], 16);
                //get value in Binary
                String bin = Integer.toBinaryString(gpr[reg1]);
                String HexNum = Integer.toHexString(reg1);
                //Fit the extra zeros before if necessary
                while (bin.length() <= 15) {
                        bin = '0' + bin;
                }
                char msb = bin.trim().charAt(0); //checking msb before operation
                checkCarry(msb);

                //Show num in binary
//                System.out.println("byte b4: " + bin);
                //Rotate Left
                //String msb = bin.substring(reg1) //msb
                bin = bin.substring(1, 16) + bin.trim().charAt(0);

                msb = bin.trim().charAt(0);
                System.out.println("msb =" + msb);
                checkSign(msb);

                //Show num in binary after rotation
//                System.out.println("byte bA: " + bin);
                //convert num to hex after rotation
//                System.out.println("hex = " + Integer.toHexString(Integer.parseInt(bin, 2)));
                //set register with updated value
                gpr[reg1] = Short.parseShort(Integer.toHexString(Integer.parseInt(bin, 2)), 16);

                checkZero(gpr[reg1]);
                //add carry
        }

        //rotate right
        private void RTR() {
                int reg1 = Integer.parseInt(soi[1], 16);
                String HexNum = Integer.toHexString(reg1);
                //get value in Binary
                String bin = Integer.toBinaryString(gpr[reg1]);
                //Fit the extra zeros before if necessary
                while (bin.length() <= 15) {
                        bin = '0' + bin;
                }
                char msb = bin.trim().charAt(0); //checking msb before operation
                checkCarry(msb);
                //Show num in binary
//                System.out.println("byte b4: " + bin);
                //Rotate Right
                System.out.println("before trim = " + bin);

                bin = bin.trim().charAt(15) + bin.substring(1, 15); ///
                System.out.println("lsb = " + bin);

                //checking for the most significant bit
                msb = bin.trim().charAt(0);
                System.out.println("msb =" + msb);
                checkSign(msb);
                //Show num in binary after rotation
//                System.out.println("byte bA: " + bin);
                //convert num to hex after rotation
//                System.out.println("hex = " + Integer.toHexString(Integer.parseInt(bin, 2)));
                //set register with updated value
                gpr[reg1] = Short.parseShort(Integer.toHexString(Integer.parseInt(bin, 2)), 16);
                checkZero(gpr[reg1]);
                //add carry
        }

        //increment
        private void INC() {
                System.out.println("INC is running");

                int reg1 = Integer.parseInt(soi[1], 16);
                gpr[reg1] = (short) (gpr[reg1] + 1);
                //Print values
        }

        //decrement
        private void DEC() {
                System.out.println("INC is running");

                int reg1 = Integer.parseInt(soi[1], 16);
                gpr[reg1] = (short) (gpr[reg1] - 1);
                //Print values
        }

        private void PUSH() {
                System.out.println("PUSH is running");
                END();
//                int reg1 = Integer.parseInt(soi[1], 16);
                //  gpr[reg1] = (short) (gpr[reg1] + 1);
//                stck.push(gpr[reg1]);
                //printValues();
        }

        private void POP() {
                System.out.println("POP is running");
                END();
                //  int reg1 = Integer.parseInt(soi[1], 16);
                //  gpr[reg1] = (short) (gpr[reg1] + 1);
//                stck.pop();
                //printValues();

        }

        private void RETURN() {
        }

        //no operation
        private void NOOP() {
                incrementPC();
                System.out.println("No operation performed");
        }

        //end operation
        private void END() {
                //Print values
                System.out.println("End of Process");
                this.programCounter = (short) (this.codeCounter + 1);
                this.printValues();
        }

        //INSTRUCTIONS (based on Op-Code)
        //Register-Register Instructions
        public void RegRegInstr(String hex) {
                //Opcode
                rri[0] = hex;
                //Reg1
                loadPCtoIR();
                incrementPC();
                rri[1] = IRtoHex(instructionReg);
                //Reg2
                loadPCtoIR();
                incrementPC();
                rri[2] = IRtoHex(instructionReg);

                System.out.println("opcode: " + rri[0] + " reg#1: " + rri[1] + " reg#2 " + rri[2]);

        }

        public void MemInstr(String hex) {
                //Opcode
                mi[0] = hex;

                //Register
                loadPCtoIR();
                incrementPC();
                mi[1] = IRtoHex(instructionReg);

                //Immediate
                loadPCtoIR();
                incrementPC();
                mi[2] = IRtoHex(instructionReg);

                loadPCtoIR();
                incrementPC();
                mi[3] = IRtoHex(instructionReg);

                System.out.println("opcode: " + mi[0] + " reg: " + mi[1] + " immediate: " + mi[2] + " " + mi[3]);

        }

        public void RegImInstr(String hex) {
                //Opcode
                rii[0] = hex;

                //Register
                loadPCtoIR();
                incrementPC();
                rii[1] = IRtoHex(instructionReg);

                //Immediate
                loadPCtoIR();
                incrementPC();
                rii[2] = IRtoHex(instructionReg);

                loadPCtoIR();
                incrementPC();
                rii[3] = IRtoHex(instructionReg);

                System.out.println("opcode: " + rii[0] + " reg: " + rii[1] + " immediate: " + rii[2] + " " + rii[3]);
        }

        //Single-Operand Instruction
        public void SinOpInstr(String hex) {
                //Opcode
                soi[0] = hex;
                //Reg1
                loadPCtoIR();
                incrementPC();
                soi[1] = IRtoHex(instructionReg);

                System.out.println("opcode: " + rri[0] + " reg#1: " + rri[1]);
        }

        //No Operand Instructions
        public void NoOpInstr(String hex) {
                noi[0] = hex;
        }

        private void printValues() {
                //printing updated registers
                System.out.print("General Purpose Reg: [");
                for (int i = 0; i < gpr.length; i++) {
                        System.out.print(gpr[i] + ", ");
                }
                System.out.println("]");
                System.out.print("Special Purpose Reg: [");
                for (int i = 0; i < spr.length; i++) {
                        System.out.print(spr[i] + ", ");
                }
                System.out.println("]");
                System.out.print("Flag Reg: [");
                for (int i = 0; i < flagreg.length; i++) {
                        if (flagreg[i]) {
                                System.out.print("1, ");
                        } else {
                                System.out.print("0, ");
                        }
                }
                System.out.println("]");

        }

}
