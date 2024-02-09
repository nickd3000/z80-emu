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
    KeyboardProcessor keyboardProcessor;
    boolean showInstructions = true;
    int borderSize = 50;
    private CPU cpu = null;
    private MEM mem = null;

    private Emulator() {
        initBreakpoints();

        cpu = new CPU();
        mem = new MEM(cpu);

        cpu.attachHardware(mem);

        Utils.ReadFileBytesToMemoryLocation(biosPath + "48.rom", mem.getRAM(), 0, false);
        Utils.ReadFileBytesToMemoryLocation(testPath + "z80doc.tap", mem.getRAM(), 0x8000, true);

        basicDisplay = new BasicDisplayAwt((256 + 10) * displayScale + (borderSize * 2), (192 + 10) * displayScale + (borderSize * 2));
        basicDisplay.setTitle("z80-emu");

        keyboardProcessor = new KeyboardProcessor(basicDisplay, cpu);

    }

    public static void main(String[] args) {
        Emulator emulator = new Emulator();
        emulator.run();
    }


    public void loadZ80() {
        FileReaderZ80 fileReaderZ80 = new FileReaderZ80();
// Mostly working
//        fileReaderZ80.readFile(romPath+"Zzzz.z80", cpu);
//        fileReaderZ80.readFile(romPath+"ManicMiner.z80", cpu); // Info byte: 0b100000
//        fileReaderZ80.readFile(romPath+"JETSETW1.Z80", cpu);
//        fileReaderZ80.readFile(romPath+"HungryHorace.z80", cpu);
//        fileReaderZ80.readFile(romPath+"Myth.z80", cpu);
//        fileReaderZ80.readFile(romPath+"FindersKeepers.z80", cpu);
//        fileReaderZ80.readFile(romPath+"Arkanoid.z80", cpu); // type 2
//        fileReaderZ80.readFile(romPath+"ActionBiker.z80", cpu); // Type 2
//        fileReaderZ80.readFile(romPath+"Jetpac.z80", cpu);
//        fileReaderZ80.readFile(romPath+"GNG.z80", cpu);
//        fileReaderZ80.readFile(romPath+"SabreWulf.z80", cpu);
//        fileReaderZ80.readFile(romPath+"1942.z80", cpu); // Type 3A
//        fileReaderZ80.readFile(romPath+"Airwolf.z80", cpu); // Type 2
//        fileReaderZ80.readFile(romPath+"Spellbound.z80", cpu); //
//        fileReaderZ80.readFile(romPath+"BombJack.z80", cpu); // Type 1
//        fileReaderZ80.readFile(romPath+"ChuckieEgg.z80", cpu); // Type 2
//        fileReaderZ80.readFile(romPath+"FantasyWorldDizzy.z80", cpu); // Type 1
//        fileReaderZ80.readFile(romPath+"RType.z80", cpu); // type 3A
//        fileReaderZ80.readFile(romPath+"JacktheNipper.z80", cpu); // OOB
//        fileReaderZ80.readFile(romPath+"BountyBob.z80", cpu); //
//        fileReaderZ80.readFile(romPath+"Feud.z80", cpu); //

        // Does something (But crashes)
//        fileReaderZ80.readFile(romPath+"RainbowIslands.z80", cpu); // crashes on level Start
//        fileReaderZ80.readFile(romPath+"DONKKONG.Z80", cpu);
//        fileReaderZ80.readFile(romPath+"MAGICCAS.Z80", cpu); // OOB Info byte: 0b10
//        fileReaderZ80.readFile(romPath+"Zorro.z80", cpu);
//        fileReaderZ80.readFile(romPath+"Galaxian.z80", cpu); // type 1
//        fileReaderZ80.readFile(romPath+"Silkworm.z80", cpu); // type 3
//        fileReaderZ80.readFile(romPath+"BCBill.z80", cpu); //
//        fileReaderZ80.readFile(romPath+"SpyHunter.z80", cpu); //

//        fileReaderZ80.readFile(romPath+"Pedro.z80", cpu); //
//        fileReaderZ80.readFile(romPath+"CauldronII.z80", cpu); //
//        fileReaderZ80.readFile(romPath+"Cauldron.z80", cpu); //
//        fileReaderZ80.readFile(romPath+"BubbleBobble.z80", cpu); //


        // PC Starts at 0xFFFF
        fileReaderZ80.readFile(romPath + "HeadOverHeels.z80", cpu); // type 1


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
//        breakpoints.add(0x02ca); // misc
//        breakpoints.add(0x028E); // KEY-SCAN
//        breakpoints.add(0x02A1); // KEY-BITS
//        breakpoints.add(0x029D); // KEY DETECTED
//          breakpoints.add(0x03D6); // sound?
//                  breakpoints.add(0x03B5); // BEEPER
//        breakpoints.add(0x111D); // ED-COPY
//

        // Silkworm
//        breakpoints.add(0x1662); // real emu doesn't get here
//        breakpoints.add(0x1661);
//        breakpoints.add(0x6230);
//        breakpoints.add(0x622A);
//        breakpoints.add(0x1663);

        // head over heels
        breakpoints.add(0x11dc); // real emu doesn't get here
    }

    public void interrupt() {
        boolean verbose = false;
        if (cpu.interruptEnabled == 1) {
            if (cpu.interruptMode == 0) {
                cpu.halted = false;
                if (verbose) System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> Interrupt (Mode 0)");
            } else if (cpu.interruptMode == 1) {
                cpu.halted = false;
                cpu.pushW(cpu.PC);
                cpu.PC = 0x0038;
                if (verbose) System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> Interrupt (Mode 1)");
            } else if (cpu.interruptMode == 2) {
                cpu.halted = false;
                cpu.pushW(cpu.PC);
                int address = (cpu.I << 8) | (cpu.dataBus & 0xFF);
                cpu.PC = mem.peek(address) | ((mem.peek(address + 1) & 0xffff) << 8);
                if (verbose) System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> Interrupt (Mode 2)");
                if (verbose) System.out.println(cpu.lastDecompile);
                if (verbose) System.out.println("Databus contained: " + Utils.toHex2(cpu.dataBus));
            }
        }
    }

    private void run() {
        cpu.init();
        cpu.PC = 0; //0x8000;

        loadZ80();

        boolean breakPointHit = false;

        int iterations = 3600000 * 1280;// 639031 + 10;
        keyboardProcessor.handleInput();

        for (int i = 0; i < iterations; i++) {
            if (i % 100 == 0) {
                basicDisplay.tickInput();
                processGui();
            }

            if (testBreakpoint(cpu.PC) && !breakPointHit) {
                iterations = i + 128;
                breakPointHit = true;
                System.out.println("************* Breakpoint ****************");
            }

            cpu.tick(1);

            if (showInstructions /*&& i>600000*/) System.out.println(cpu.dump());

            if ((i & 5000) == 0) keyboardProcessor.handleInput();

            if (i % 5000 == 0 && i > 0) {

                render.render(basicDisplay, cpu, 2, borderSize);
                render.renderRegisters(basicDisplay, cpu, 2);
                basicDisplay.repaint();
                //System.out.println(cpu.dump());

                interrupt();
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

    // By GUI, i mean debug keys
    public void processGui() {

        int[] keyState = basicDisplay.getKeyState();
        boolean control = basicDisplay.getKeyState()[KeyEvent.VK_CONTROL] > 0;

        if (control && keyState[KeyEvent.VK_I] > 0) {
            showInstructions = true;
        }
        if (control && keyState[KeyEvent.VK_O] > 0) {
            showInstructions = false;
        }

        if (control && keyState[KeyEvent.VK_T] > 0) {
            loadAndRunTests();
        }

    }


    public void loadAndRunTests() {

        Utils.ReadFileBytesToMemoryLocation(testPath + "z80doc.tap", mem.getRAM(), 0x8000, true);
        cpu.PC = 0x8000;

    }
}
