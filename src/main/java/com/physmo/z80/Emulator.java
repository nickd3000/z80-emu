package com.physmo.z80;

public class Emulator {
    private final String biosPath = "/Users/nick/dev/emulatorsupportfiles/spectrum/bios/";
    private final String testPath = "/Users/nick/dev/emulatorsupportfiles/z80test-1.2a/";

    private CPU cpu = null;
    private MEM mem = null;

    private Emulator() {
        cpu = new CPU();
        mem = new MEM(cpu);

        cpu.attachHardware(mem);

        Utils.ReadFileBytesToMemoryLocation(biosPath + "48.rom", mem.getRAM(), 0, false);
        Utils.ReadFileBytesToMemoryLocation(testPath + "z80doc.tap", mem.getRAM(), 0x8000, true);


    }

    public static void main(String[] args) {
        Emulator emulator = new Emulator();
        emulator.run();
    }

    private void run() {
        cpu.init();
        cpu.PC = 0; //0x8000;
        for (int i = 0; i < 1600000; i++) {

            if (cpu.PC == 0x11ef)
                System.out.println("0x110f");

            System.out.println(cpu.dump());
            cpu.tick(1);

        }
        dumpScreen();
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
