package com.physmo.z80;

import com.physmo.minvio.BasicDisplay;

import java.awt.Color;

public class Render {

    public void render(BasicDisplay bd, CPU cpu, int scale) {
        int[] ram = cpu.mem.getRAM();
        Color c1 = new Color(0, 0, 0);
        Color c2 = new Color(0xff, 0xff, 0x1f);

        int numBytes = 192 * 32;
        int readHead = 0x4000;
        int x = 0;
        int y = 0;
        for (int j = 0; j < 192; j++) {
            for (int i = 0; i < 32; i++) {
                int chunk = ram[readHead++];
                y = j;
                x = i * 8;

                // Decode pixels in byte;
                for (int b = 0; b < 8; b++) {
                    if ((chunk & (1 << b)) > 0) bd.setDrawColor(c1);
                    else bd.setDrawColor(c2);
                    bd.drawFilledRect((x + b) * scale, y * scale, scale, scale);
                }

            }
        }
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


