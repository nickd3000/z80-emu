package com.physmo.z80;

import com.physmo.z80.microcode.CodeTable;
import com.physmo.z80.microcode.CodeTableManager;
import org.junit.Assert;
import org.junit.Test;

public class ScratchTest {
    CodeTableManager codeTableManager = new CodeTableManager();
    CodeTable microcode = codeTableManager.codeTableMain;

    @Test
    public void t1() {
        CPU cpu = new CPU();
        MEM mem = new MEM(cpu);
        cpu.attachHardware(mem);

        int addr = 0;
        cpu.mem.RAM[addr++] = microcode.getOpcode("LD A,d8");
        cpu.mem.RAM[addr++] = 123; // "LD A,d8"
        cpu.mem.RAM[addr++] = microcode.getOpcode("LD B,d8");
        cpu.mem.RAM[addr++] = 123; // "LD A,d8"
        cpu.mem.RAM[addr++] = microcode.getOpcode("INC B");

        cpu.tick(6);

        System.out.println("End");
    }


    @Test
    public void testOpCodeLookup() {
        Assert.assertEquals(microcode.getOpcode("LD A,d8"), 0x3E);
    }
}
