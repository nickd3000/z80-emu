package com.physmo.z80;

import com.physmo.minvio.BasicDisplay;

import java.awt.event.KeyEvent;

public class KeyboardProcessor {

    BasicDisplay bd;
    CPU cpu;

    int[] KeysFEFE = new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V};
    int[] KeysFDFE = new int[]{KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F, KeyEvent.VK_G};
    int[] KeysFBFE = new int[]{KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_T};
    int[] KeysF7FE = new int[]{KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5};
    int[] KeysEFFE = new int[]{KeyEvent.VK_0, KeyEvent.VK_9, KeyEvent.VK_8, KeyEvent.VK_7, KeyEvent.VK_6};
    int[] KeysDFFE = new int[]{KeyEvent.VK_P, KeyEvent.VK_O, KeyEvent.VK_I, KeyEvent.VK_U, KeyEvent.VK_Y};
    int[] KeysBFFE = new int[]{KeyEvent.VK_ENTER, KeyEvent.VK_L, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_H};
    int[] Keys7FFE = new int[]{KeyEvent.VK_SPACE, KeyEvent.VK_TAB, KeyEvent.VK_M, KeyEvent.VK_N, KeyEvent.VK_B};

    public KeyboardProcessor(BasicDisplay bd, CPU cpu) {
        this.bd = bd;
        this.cpu = cpu;
    }

    public int processRow(int[] rowKeys, int[] keyState) {
        int result = 0xff; //0xbf; //0xbf; //0xff; //0x1f;//
        for (int i = 0; i < 5; i++) {
            if (keyState[rowKeys[i]] > 0) {
                result = cpu.resetBit(result, i);
            }
        }
        return result;
    }

    public void handleInput() {

        int[] keyState = bd.getKeyState();

        if (keyState[KeyEvent.VK_CONTROL] > 0) return;

        cpu.mem.setKeyRowState(0, processRow(KeysFEFE, keyState)); // 0
        cpu.mem.setKeyRowState(1, processRow(KeysFDFE, keyState)); // 1
        cpu.mem.setKeyRowState(2, processRow(KeysFBFE, keyState)); // 2
        cpu.mem.setKeyRowState(3, processRow(KeysF7FE, keyState)); // 3
        cpu.mem.setKeyRowState(4, processRow(KeysEFFE, keyState)); // 4
        cpu.mem.setKeyRowState(5, processRow(KeysDFFE, keyState)); // 5
        cpu.mem.setKeyRowState(6, processRow(KeysBFFE, keyState)); // 6
        cpu.mem.setKeyRowState(7, processRow(Keys7FFE, keyState)); // 7
    }

}
