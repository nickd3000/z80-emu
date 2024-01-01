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

    public int peek(int addr) {
        return RAM[addr];
    }

    public int[] getRAM() {
        return RAM;
    }

}
