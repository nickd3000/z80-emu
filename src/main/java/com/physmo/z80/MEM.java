package com.physmo.z80;

public class MEM {

    public int[] RAM = new int[0x10000]; // 64k

    public void poke(int addr, int val) {
        RAM[addr] = val;
    }

    public int peek(int addr) {
        return RAM[addr];
    }
}
