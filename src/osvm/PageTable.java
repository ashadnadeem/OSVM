/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osvm;

import java.util.ArrayList;

/**
 *
 * @author naqui
 */
public class PageTable {

        ArrayList<Page> pagetable;

        public PageTable() {
                pagetable = new ArrayList<Page>();

        }

        public void AddEntry(Byte starting, Byte ending) {
                pagetable.add(new Page(starting, ending));
        }

        public void getFrame(int pagenumber) {

        }

}
