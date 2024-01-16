package com.physmo.z80;

public class MEM {

    public int[] RAM = new int[0x10000]; // 64k
    CPU cpu = null;

    public MEM(CPU cpu) {
        this.cpu = cpu;
    }

    public void poke(int addr, int val) {
        if (addr > 0x3FFF) RAM[addr] = val;
    }

    public void pokeWord(int addr, int val) {
        int b1 = val & 0xff;
        int b2 = (val >> 8) & 0xff;

        // Verified correct order on 2024.01.13
        if (addr > 0x3FFF) {
            RAM[addr] = b1;
            System.out.println(Utils.toHex4(addr) + " set to " + Utils.toHex2(b1));
            RAM[addr + 1] = b2;
            System.out.println(Utils.toHex4(addr + 1) + " set to " + Utils.toHex2(b2));
        }
    }

    public int peek(int addr) {
        return RAM[addr];
    }


    public int peekWord(int addr) {
        int b1 = RAM[addr];
        int b2 = RAM[addr + 1];
        return ((b2 & 0xff) << 8) | (b1 & 0xff);
    }

    public int[] getRAM() {
        return RAM;
    }


}
