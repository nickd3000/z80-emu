package com.physmo.z80;

public class MEM {

    public int[] RAM = new int[0x10000]; // 64k
    public int[] PORTS = new int[0x10000]; // 64k
    CPU cpu = null;

    public MEM(CPU cpu) {
        this.cpu = cpu;
    }

    int count = 0;

    public void poke(int addr, int val) {
        if (addr == 0xa151) {
            count++;
            System.out.println("PC=" + Utils.toHex4(cpu.PC));
            if (count == 20) throw new RuntimeException();
        }

        if (addr > 0xffff) {
            System.out.println("out of bounds");
        }
        if (addr > 0x3FFF) RAM[addr] = val;
    }

    public void pokeWord(int addr, int val) {
        int b1 = val & 0xff;
        int b2 = (val >> 8) & 0xff;

        // Verified correct order on 2024.01.13
        if (addr > 0x3FFF) {
            RAM[addr] = b1;
            //System.out.println(Utils.toHex4(addr) + " set to " + Utils.toHex2(b1));
            RAM[addr + 1] = b2;
            //System.out.println(Utils.toHex4(addr + 1) + " set to " + Utils.toHex2(b2));
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

    public void setPort(int portAddress, int value) {
        PORTS[portAddress & 0xFFFF] = value & 0xff;
    }

    public int getPort(int portAddress) {
        return PORTS[portAddress & 0xFFFF] & 0xFF;
    }
}
