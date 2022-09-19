/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osvm;

/**
 *
 * @author naqui
 */
public class PCB {
    int pid;//process id
    int ppriority;//priority
    short psize;//program size
    String pname;//process name
    OSVM osvm;//all the registers and flags
    PageTable data_pagetable;
    PageTable code_pagetable;
    
    PCB(){
        osvm = new OSVM();
        data_pagetable = new PageTable();
        code_pagetable = new PageTable();
    }

        public int getPid() {
                return pid;
        }

        public void setPid(int pid) {
                this.pid = pid;
        }

        public int getPpriority() {
                return ppriority;
        }

        public void setPpriority(int ppriority) {
                this.ppriority = ppriority;
        }

        public short getPsize() {
                return psize;
        }

        public void setPsize(short psize) {
                this.psize = psize;
        }

        public String getPname() {
                return pname;
        }

        public void setPname(String pname) {
                this.pname = pname;
        }

        public OSVM getOsvm() {
                return osvm;
        }

        public void setOsvm(OSVM osvm) {
                this.osvm = osvm;
        }

        public PageTable getData_pagetable() {
                return data_pagetable;
        }

        public void setData_pagetable(PageTable data_pagetable) {
                this.data_pagetable = data_pagetable;
        }

        public PageTable getCode_pagetable() {
                return code_pagetable;
        }

        public void setCode_pagetable(PageTable code_pagetable) {
                this.code_pagetable = code_pagetable;
        }

        @Override
        public String toString() {
                return "PCB{" + "pid=" + pid + ", ppriority=" + ppriority + ", psize=" + psize + ", pname=" + pname + ", osvm=" + osvm + ", data_pagetable=" + data_pagetable + ", code_pagetable=" + code_pagetable + '}';
        }

    
}
