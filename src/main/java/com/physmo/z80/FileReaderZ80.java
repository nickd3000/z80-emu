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
        int[] header = new int[100];
        int headerPos = 0;
        for (int i = 0; i < 30; i++) {
            header[headerPos++] = in.read();
        }
        System.out.println("Read header ");
        System.out.println("Info byte: " + Utils.toBinary(header[12]));


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
        if (header[27] != 0) cpu.interruptEnabled = 1;
        else cpu.interruptEnabled = 0;
        int info2 = header[29];
        cpu.interruptMode = info2 & 3;

        int fileVersion = 0;
        if (cpu.PC != 0) {
            System.out.println("File type = 1");
            System.out.println("PC = " + Utils.toHex4(cpu.PC));
            fileVersion = 1;
        }

        // Detect Version 2/3 file (PC will be 0)
        if (makeWord(header[6], header[7]) == 0) {
            // Read version type
            header[headerPos++] = in.read();
            header[headerPos++] = in.read();

            int extraHeaderMode = makeWord(header[30], header[31]);

            if (extraHeaderMode == 23) {
                System.out.println("File type = 2");
                fileVersion = 2;
                while (headerPos <= 54) {
                    header[headerPos++] = in.read();
                }
            } else if (extraHeaderMode == 54) {
                System.out.println("File type = 3 A");
                fileVersion = 3;
                while (headerPos <= 85) {
                    header[headerPos++] = in.read();
                }
            } else if (extraHeaderMode == 55) {
                System.out.println("File type = 3 B");
                fileVersion = 3;
                while (headerPos <= 86) {
                    header[headerPos++] = in.read();
                }
            } else {
                System.out.println("Unrecognised header type");
            }


            cpu.PC = makeWord(header[32], header[33]);
            System.out.println("PC=" + Utils.toHex4(cpu.PC));
        }


        // Read compressed data to buffer.
        int[] compressed = new int[0xffff];
        int c = 0, i = 0;
        while ((c = in.read()) != -1) {
            compressed[i++] = c;
        }

        int compressedSize = i - 1;

        // Decompress.
        if (fileVersion == 1) {
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
                    if (writeHead > 0xffff) {
                        System.out.println("File expand overflow. writehead:" + writeHead + ", readHead:" + readHead + "   compressedSize:" + compressedSize);
                    }

                    cpu.mem.poke(writeHead++, compressed[readHead++]);
                }
            }
        }

        if (fileVersion > 1) {
            int readHead = 0;
            int writeHead = 0;
            while (readHead < compressedSize) {
                int blockLength = makeWord(compressed[readHead++], compressed[readHead++]);
                if (blockLength == 0) break;
                int pageNumber = compressed[readHead++];

                System.out.println("Block length: " + blockLength);
                System.out.println("page number:" + pageNumber);

                if (pageNumber == 4) writeHead = 0x8000;
                if (pageNumber == 5) writeHead = 0xc000;
                if (pageNumber == 8) writeHead = 0x4000;

                int blockEnd = readHead + blockLength;

                // skip
                while (readHead < blockEnd) {
                    if (compressed[readHead] == 0xED && compressed[readHead + 1] == 0xED) {
                        int repeat = compressed[readHead + 2];
                        int value = compressed[readHead + 3];
                        for (int j = 0; j < repeat; j++) {
                            cpu.mem.poke(writeHead++, value);
                        }
                        readHead += 4;
                    } else {
                        if (writeHead > 0xffff) {
                            System.out.println("File expand overflow. writehead:" + writeHead + ", readHead:" + readHead + "   compressedSize:" + compressedSize);
                        }

                        cpu.mem.poke(writeHead++, compressed[readHead++]);
                    }
                }
            }

        }


    }

    public int makeWord(int a, int b) {
        return (((b & 0xff) << 8)) | (a & 0xff);
    }
}