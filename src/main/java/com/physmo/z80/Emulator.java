package com.physmo.z80;

import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Emulator {
    private static BasicDisplay basicDisplay = null;
    private final String biosPath = "/Users/nick/dev/emulatorsupportfiles/spectrum/bios/";
    private final String romPath = "/Users/nick/dev/emulatorsupportfiles/spectrum/roms/";
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
//        Utils.ReadFileBytesToMemoryLocation(testPath + "z80doc.tap", mem.getRAM(), 0x8000, true);

        basicDisplay = new BasicDisplayAwt((256 + 10) * displayScale, (192 + 10) * displayScale);
        basicDisplay.setTitle("z80-emu");


    }

    public static void main(String[] args) {
        Emulator emulator = new Emulator();
        emulator.run();
    }

    public void loadZ80() {
        FileReaderZ80 fileReaderZ80 = new FileReaderZ80();
        fileReaderZ80.readFile(romPath + "FindersKeepers.z80", cpu);
//        fileReaderZ80.readFile(romPath+"DONKKONG.Z80", cpu);
//        fileReaderZ80.readFile(romPath+"MAGICCAS.Z80", cpu); // bad
//        fileReaderZ80.readFile(romPath+"JETSETW1.Z80", cpu);
    }

    public void initBreakpoints() {
//        breakpoints.add(0x0000); // START
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

//        breakpoints.add(0x1228); // STACK POINTER gets set here

//        breakpoints.add(0x0DE2); // flags seem wrong here?

//        breakpoints.add(0x0D89); // CLS-2

//        breakpoints.add(0x0C0A); // PO-MSG - Message print

//        breakpoints.add(0x0E9B); // CL-ADDR

//        breakpoints.add(0x0DD9); // CL-SET

//        breakpoints.add(0x0DD9); // misc

//        breakpoints.add(0x028E); // KEY-SCAN

//                breakpoints.add(0x0038); // KEY-SCAN
    }

    public void interrupt() {
        if (cpu.interruptEnabled == 1) {
            if (cpu.interruptMode == 0) {
//                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> Interrupt (Mode 0)");
            } else if (cpu.interruptMode == 1) {
                cpu.halted = false;
                cpu.pushW(cpu.PC);
                cpu.PC = 0x0038;
//                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> Interrupt (Mode 1)");
            } else if (cpu.interruptMode == 2) {
                cpu.pushW(cpu.PC);
                int address = (cpu.I << 8) | cpu.dataBus;
                cpu.PC = mem.peek(address) | ((mem.peek(address + 1) & 0xffff) << 8);
//                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> Interrupt (Mode 2)");
            }
        }
    }

    private void run() {
        cpu.init();
        cpu.PC = 0;//0x8000;

        loadZ80();

        boolean breakPointHit = false;

        int iterations = 3600000 * 5;// 639031 + 10;

        for (int i = 0; i < iterations; i++) {
            if (i % 2000 == 0) interrupt();

            if (testBreakpoint(cpu.PC) && !breakPointHit) {
                iterations = i + 15;
                breakPointHit = true;
                System.out.println("************* Breakpoint ****************");
            }

            cpu.tick(1);
            System.out.println(cpu.dump());

            if ((i & 5000) == 0) handleInput();

            if (i % 5000 == 0) {
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

    public void handleInput() {
        basicDisplay.tickInput();
        int[] keyState = basicDisplay.getKeyState();

        int F7FE = 0x1f;
        if (keyState[KeyEvent.VK_1] > 0) {
            F7FE = cpu.resetBit(F7FE, 0);
        }

        int FDFE = 0x1f;
        if (keyState[KeyEvent.VK_A] > 0) {
            FDFE = cpu.resetBit(FDFE, 0);
        }
        if (keyState[KeyEvent.VK_D] > 0) {
            FDFE = cpu.resetBit(FDFE, 2);
        }

        mem.setPort(0xFEFE, 0x1F);
        mem.setPort(0xFDFE, FDFE);
        mem.setPort(0xFBFE, 0x1F);
        mem.setPort(0xF7FE, F7FE);
        mem.setPort(0xEFFE, 0x1F);
        mem.setPort(0xDFFE, 0x1F);
        mem.setPort(0xBFFE, 0x1F);
        mem.setPort(0x7FFE, 0x1F);

    }
}
