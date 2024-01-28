package com.physmo.z80.microcode;

import org.junit.Assert;
import org.junit.Test;

// Test algorithmically generated instructions
public class TestMicrocode {
    CodeTableManager codeTableManager = new CodeTableManager();

    @Test
    public void testCB_0x00() {
        int instruction = 0x00;
        String name = codeTableManager.codeTableCB.getInstructionName(instruction);
        MicroOp[] codes = codeTableManager.codeTableCB.getInstructionCode(instruction);
        System.out.println(name);

        Assert.assertEquals("rlc B", name);
        Assert.assertEquals(MicroOp.FETCH_B, codes[0]);
        Assert.assertEquals(MicroOp.RLC, codes[1]);
        Assert.assertEquals(MicroOp.STORE_B, codes[2]);
    }

    @Test
    public void testCB_0x40() {
        int instruction = 0x40;
        String name = codeTableManager.codeTableCB.getInstructionName(instruction);
        MicroOp[] codes = codeTableManager.codeTableCB.getInstructionCode(instruction);
        System.out.println(name);
        Assert.assertEquals("BIT 0,B", name);
        Assert.assertEquals(MicroOp.FETCH_B, codes[0]);
        Assert.assertEquals(MicroOp.BIT0, codes[1]);
        Assert.assertEquals(MicroOp.STORE_B, codes[2]);
    }


    @Test
    public void testFDCB_0x00() {
        int instruction = 0x00;
        String name = codeTableManager.codeTableFDCB.getInstructionName(instruction);
        MicroOp[] codes = codeTableManager.codeTableFDCB.getInstructionCode(instruction);
        System.out.println(name);
        Assert.assertEquals("RLC (iy+d),B", name);
        Assert.assertEquals(MicroOp.FETCH_pIY_D, codes[0]);
        Assert.assertEquals(MicroOp.RLC, codes[1]);
        Assert.assertEquals(MicroOp.STORE_B, codes[2]);
        Assert.assertEquals(MicroOp.STORE_pIY_D, codes[3]);
    }

    @Test
    public void testFDCB_0x40() {
        int instruction = 0x40;
        String name = codeTableManager.codeTableFDCB.getInstructionName(instruction);
        MicroOp[] codes = codeTableManager.codeTableFDCB.getInstructionCode(instruction);
        System.out.println(name);
        Assert.assertEquals("BIT 0,(iy+d)", name);
        Assert.assertEquals(MicroOp.FETCH_pIY_D, codes[0]);
        Assert.assertEquals(MicroOp.BIT0, codes[1]);
    }

    // createFDCBInstructions(0xC0, MicroOp.SET0, "SET 0,(iy+d),@");
    @Test
    public void testFDCB_0xC0() {
        int instruction = 0xC0;
        String name = codeTableManager.codeTableFDCB.getInstructionName(instruction);
        MicroOp[] codes = codeTableManager.codeTableFDCB.getInstructionCode(instruction);
        System.out.println(name);
        Assert.assertEquals("SET 0,(iy+d),B", name);
        Assert.assertEquals(MicroOp.FETCH_pIY_D, codes[0]);
        Assert.assertEquals(MicroOp.SET0, codes[1]);
        Assert.assertEquals(MicroOp.STORE_B, codes[2]);
        Assert.assertEquals(MicroOp.STORE_pIY_D, codes[3]);
    }
}
