package com.physmo.z80;

import com.physmo.minvio.BasicDisplay;

import java.awt.Color;

public class Render {

    Color[] colors = new Color[16];
    Color fg = null;
    Color bg = null;

    public Render() {
        colors[0] = new Color(0x000000);
        colors[1] = new Color(0x0100CE);
        colors[2] = new Color(0xCF0100);
        colors[3] = new Color(0xCF01CE);
        colors[4] = new Color(0x00CF15);
        colors[5] = new Color(0x01CFCF);
        colors[6] = new Color(0xCFCF15);
        colors[7] = new Color(0xCFCFCF);
        colors[8] = new Color(0x000000);
        colors[9] = new Color(0x0200FD);
        colors[10] = new Color(0xFF0201);
        colors[11] = new Color(0xFF02FD);
        colors[12] = new Color(0x00FF1C);
        colors[13] = new Color(0x02FFFF);
        colors[14] = new Color(0xFFFF1D);
        colors[15] = new Color(0xFFFFFF);
        fg = colors[11];
        bg = colors[14];
    }

    public void render(BasicDisplay bd, CPU cpu, int scale) {
        int[] ram = cpu.mem.getRAM();

        int numBytes = 192 * 32;
        int readHead = 0x4000;
        int x = 0;
        int y = 0;
        for (int j = 0; j < 192; j++) {
            for (int i = 0; i < 32; i++) {

                int address = i & 0b00011111; // Add x part.
                address |= ((j & 0b00111000) << 2); // Add upper 3 bits of y
                address |= ((j & 0b00000111) << 8); // Add lower 3 bits of y
                address |= ((j & 0b11000000) << 5); // Add top 2 bits of y
                address |= 0b01000000_00000000;

                int chunk = ram[address];
                y = j;
                x = i * 8;

                int attribute = ram[0x5800 + (32 * (j / 8)) + i];
                setAttributes(attribute);

                // Decode pixels in byte;
                for (int b = 0; b < 8; b++) {
                    if ((chunk & (1 << (7 - b))) > 0) bd.setDrawColor(fg);
                    else bd.setDrawColor(bg);
                    bd.drawFilledRect((x + b) * scale, y * scale, scale, scale);
                }

            }
        }
    }

    public void setAttributes(int value) {
        int ink = value & 0b0000_0111;
        int paper = (value & 0b0011_1000) >> 3;
        int brightness = (value & 0b0100_0000) >> 6;

        if (brightness > 0) {
            ink += 8;
            paper += 8;
        }

        bg = colors[paper];
        fg = colors[ink];
    }


    public void renderRegisters(BasicDisplay bd, CPU cpu, int scale) {

        bd.setDrawColor(Color.BLUE);
        bd.drawFilledRect(0, cpu.A, 2 * scale, 2 * scale);
        bd.setDrawColor(Color.RED);
        bd.drawFilledRect(2, cpu.B, 2 * scale, 2 * scale);
        bd.setDrawColor(Color.GRAY);
        bd.drawFilledRect(4, cpu.C, 2 * scale, 2 * scale);
        bd.setDrawColor(Color.GREEN);
        bd.drawFilledRect(8, cpu.D, 2 * scale, 2 * scale);
        bd.setDrawColor(Color.ORANGE);
        bd.drawFilledRect(10, cpu.H, 2 * scale, 2 * scale);
        bd.setDrawColor(Color.MAGENTA);
        bd.drawFilledRect(12, cpu.L, 2 * scale, 2 * scale);

    }

}


