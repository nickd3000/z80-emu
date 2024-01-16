package com.physmo.z80;

import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

import java.util.ArrayList;
import java.util.List;

public class Emulator {
    private static BasicDisplay basicDisplay = null;
    private final String biosPath = "/Users/nick/dev/emulatorsupportfiles/spectrum/bios/";
    private final String testPath = "/Users/nick/dev/emulatorsupportfiles/z80test-1.2a/";
    private final int displayScale = 2;
    List<Integer> breakpoints = new ArrayList<>();
    Render render = new Render();
    private CPU cpu = null;
    private MEM mem = null;

    private Emulator() {
        initBreakpoints();

        cpu = new CPU();
        mem = new MEM(cpu);

        cpu.attachHardware(mem);

        Utils.ReadFileBytesToMemoryLocation(biosPath + "48.rom", mem.getRAM(), 0, false);
        Utils.ReadFileBytesToMemoryLocation(testPath + "z80doc.tap", mem.getRAM(), 0x8000, true);

        basicDisplay = new BasicDisplayAwt((256 + 10) * displayScale, (192 + 10) * displayScale);
        basicDisplay.setTitle("z80-emu");
    }

    public static void main(String[] args) {
        Emulator emulator = new Emulator();
        emulator.run();
    }

    public void initBreakpoints() {
//        breakpoints.add(0x11DA); // RAM-CHECK
//        breakpoints.add(0x11E2); // RAM-CHECK mid
//        breakpoints.add(0x11EF); // RAM-DONE
//        breakpoints.add(0x1219); // RAM-SET
//        breakpoints.add(0x11CB); // START-NEW
//        breakpoints.add(0x11F1); //
//        breakpoints.add(0x12A2); // MAIN-EXEC

//        breakpoints.add(0x121C); // NMI_VECT

//        breakpoints.add(0x1234); // End of NMI_VECT

//        breakpoints.add(0x1222); // peek word

//        breakpoints.add(0x0DAF); // CL-ALL

        breakpoints.add(0x1285); // problem?

//        breakpoints.add(0x0D89); // CLS-2

//        breakpoints.add(0x0C0A); // PO-MSG - Message print


    }

    private void run() {
        cpu.init();
        cpu.PC = 0;//0x8000;

        boolean breakPointHit = false;

        int iterations = 3600000 * 5;// 639031 + 10;
        for (int i = 0; i < iterations; i++) {

            if (testBreakpoint(cpu.PC) && !breakPointHit) {
                iterations = i + 15;
                breakPointHit = true;
                System.out.println("breakpoint hit");
            }

            cpu.tick(1);
            System.out.println(cpu.dump());

            if (i % 1000 == 0) {
                render.render(basicDisplay, cpu, 2);
                render.renderRegisters(basicDisplay, cpu, 2);
                basicDisplay.repaint();
                //System.out.println(cpu.dump());
            }
        }
//        dumpScreen();
    }

    public boolean testBreakpoint(int pc) {
        if (breakpoints.size() == 0) return false;
        for (Integer breakpoint : breakpoints) {
            if (pc == breakpoint) return true;
        }
        return false;
    }

    private void dumpScreen() {
        int offset = 0x4000;
        int length = 0x57ff - 0x4000;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int b = mem.RAM[i + offset];
            //stringBuilder.append((char) (b));
            stringBuilder.append(Utils.toHex2(b) + " ");
            if (i % 32 == 0) stringBuilder.append(System.lineSeparator());
        }
        System.out.println(stringBuilder);
    }
}
