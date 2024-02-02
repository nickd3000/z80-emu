package com.physmo.z80;

public class MEM {

    public int[] RAM = new int[0x10000]; // 64k
    public int[] PORTS = new int[0x10000]; // 64k
    CPU cpu = null;
    int count = 0;
    int[] keyRowStates = new int[]{0xFD, 0xFD, 0xFD, 0xFD, 0xFD, 0xFD, 0xFD, 0xFD};

    public MEM(CPU cpu) {
        this.cpu = cpu;
        for (int i = 0; i < 0xffff; i++) {
            PORTS[i] = 0x00;
        }
    }

    public void poke(int addr, int val) {
        if (val > 0xff) {
            System.out.println(cpu.lastDecompile);
            throw new RuntimeException("(poke) Byte at " + Utils.toHex4(addr) + " is " + Utils.toHex2(val));
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
        if (RAM[addr] > 0xff) {
            throw new RuntimeException("(peek) Byte at " + Utils.toHex4(addr) + " is " + Utils.toHex2(RAM[addr]));
        }
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
        if ((portAddress & 0xFF) == 0xFE) {
            return getKeyboardPort(portAddress);
        }
        return PORTS[portAddress & 0xFFFF] & 0xFF;
    }

    // Keyboard ports are more complicated that expected.
    public int getKeyboardPort(int portAddress) {
        int highByte = (portAddress & 0xFF00) >> 8;
        int val = 0xFF;

        for (int i = 0; i < 8; i++) {
            if ((highByte & (1 << i)) == 0) {
                val &= keyRowStates[i];
            }

        }
        return val;
    }


    public void setKeyRowState(int row, int val) {
        keyRowStates[row] = val;
    }

    public int getKeyRowState(int row) {
        return keyRowStates[row];
    }
}
