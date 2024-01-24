package com.physmo.z80;

import java.io.FileInputStream;
import java.io.IOException;

public class FileReaderZ80 {

    public void readFile(String fileName, CPU cpu) {

        try (FileInputStream in = new FileInputStream(fileName)) {

            decodeFile(in, cpu);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }

    public void decodeFile(FileInputStream in, CPU cpu) throws IOException {
        int[] header = new int[30];
        for (int i = 0; i < 30; i++) {
            header[i] = in.read();
        }
        System.out.println("Read header");

        cpu.A = header[0];
        cpu.FL = header[1];
        cpu.setBC(makeWord(header[2], header[3]));
        cpu.setHL(makeWord(header[4], header[5]));
        cpu.PC = makeWord(header[6], header[7]);
        cpu.SP = makeWord(header[8], header[9]);
        cpu.I = header[10];
        // refresh register todo
        // info stuff
        cpu.setDE(makeWord(header[13], header[14]));
        cpu.B_ = header[15];
        cpu.C_ = header[16];
        cpu.D_ = header[17];
        cpu.E_ = header[18];
        cpu.H_ = header[19];
        cpu.L_ = header[20];
        cpu.A_ = header[21];
        //cpu.F_ = header[22];
        cpu.IY = makeWord(header[23], header[24]);
        cpu.IX = makeWord(header[25], header[26]);

        int info2 = header[29];
        cpu.interruptMode = info2 & 3;

        System.out.println("PC set to 0x" + Utils.toHex4(cpu.PC));

        // Read compressed data to buffer.
        int[] compressed = new int[0xffff];
        int c = 0, i = 0;
        while ((c = in.read()) != -1) {
            compressed[i++] = c;
        }

        int compressedSize = i - 1;

        // Decompress.
        int writeHead = 0x4000;
        int readHead = 0;
        while (readHead < compressedSize - 4) {
            if (compressed[readHead] == 0xED && compressed[readHead + 1] == 0xED) {
                int repeat = compressed[readHead + 2];
                int value = compressed[readHead + 3];
                for (int j = 0; j < repeat; j++) {
                    cpu.mem.poke(writeHead++, value);
                }
                readHead += 4;
            } else {
                cpu.mem.poke(writeHead++, compressed[readHead++]);
            }
        }

    }

    public int makeWord(int a, int b) {
        return (((b & 0xff) << 8)) | (a & 0xff);
    }
}