package com.physmo.z80;


import com.physmo.z80.microcode.CodeTable;
import com.physmo.z80.microcode.CodeTableManager;
import com.physmo.z80.microcode.MicroOp;

import java.text.MessageFormat;

public class CPU {

    // CPU Flags
    public static final int FLAG_S = 0b1000_0000; // Sign
    public static final int FLAG_Z = 0b0100_0000; // Zero
    public static final int FLAG_5 = 0b0010_0000; // 5
    public static final int FLAG_H = 0b0001_0000; // Half carry
    public static final int FLAG_3 = 0b0000_1000; // 3
    public static final int FLAG_PV = 0b0000_0100; // P/V - Parity or Overflow
    public static final int FLAG_N = 0b0000_0010; // N / SUB - Set if last operation was a subtraction
    public static final int FLAG_C = 0b0000_0001; // Carry

    String name = "Zilog Z80";
    CodeTableManager codeTableManager = new CodeTableManager();
    CodeTable microcode = codeTableManager.codeTableMain;
    // Registers.
    int A, B, C, D, E, H, L, I, R;
    int A_, B_, C_, D_, E_, H_, L_;
    // Bus
    int dataBus = 0;
    int addrBus = 0;
    int PC = 0; // Program counter
    int SP = 0xdff0; // Stack pointer
    int FL = 0; // Flags
    int FL_ = 0;
    int IX = 0; // Index register
    int IY = 0; // Index register
    int pendingEnableInterrupt = 0;
    int pendingDisableInterrupt = 0;
    int interruptEnabled = 0;
    int interruptMode = 0;
    boolean halted = false;
    int displacement = 0; // FDCB prefix store the data before the last part of the operator (byte 3 of 4) so we store it here.
    MEM mem = null;
    String lastDecompile = "";
    String lastData = "";
    boolean enableDecompile = false;
    int tickCounter = 0;
    int iff1 = 0;
    int iff2 = 0;
    boolean[] parityTable = new boolean[0xFF + 1];

    public String getFlagsString() {
        // -Z---V--
        String str = "FL:" + Utils.toHex2(FL) + "[";
        char[] flagChars = {'-', '-', '-', '-', '-', '-', '-', '-'};
        if (testFlag(FLAG_S)) flagChars[0] = 'S';
        if (testFlag(FLAG_Z)) flagChars[1] = 'Z';
        if (testFlag(FLAG_5)) flagChars[2] = '5';
        if (testFlag(FLAG_H)) flagChars[3] = 'H';
        if (testFlag(FLAG_3)) flagChars[4] = '3';
        if (testFlag(FLAG_PV)) flagChars[5] = 'V';
        if (testFlag(FLAG_N)) flagChars[6] = 'N';
        if (testFlag(FLAG_C)) flagChars[7] = 'C';


        for (char flagChar : flagChars) {
            str += flagChar;
        }
        return str + "]";

    }

    public void init() {
        A = 0xFF;
        FL = 0xFF;
        setBC(0xffff);
        setDE(0xffff);
        setHL(0xffff);
        IX = 0xffff;
        IY = 0xffff;
        B_ = 0xFF;
        C_ = 0xFF;
        D_ = 0xFF;
        E_ = 0xFF;
        H_ = 0xFF;
        L_ = 0xFF;
        I = 0;
        SP = 0xafff;

        for (int i = 0; i < 200; i++) {
            mem.getRAM()[0x4000 + i] = i & 0xff;
        }

        buildParityTable();
    }

    public String dump() {
        String str = "";
        str += MessageFormat.format("{0} A:{1} BC:{2}{3} DE:{4}{5} HL:{6}{7} I:{8} IX:{9} IY:{10} IY+d:{11} dis:{12}",
                Utils.padToLength("t" + tickCounter, 8),
                Utils.toHex2(A),
                Utils.toHex2(B),
                Utils.toHex2(C),
                Utils.toHex2(D),
                Utils.toHex2(E),
                Utils.toHex2(H),
                Utils.toHex2(L),
                Utils.toHex2(I),
                Utils.toHex4(IX),
                Utils.toHex4(IY),
                Utils.toHex4(IY + displacement),
                Utils.toHex2(displacement));
        str += MessageFormat.format(" PC:{0} SP:{1} {2} ",
                Utils.toHex4(PC),
                Utils.toHex4(SP),
                getFlagsString());

        str += "   " + lastDecompile + lastData;
        return str;
    }

    public void tick(int n) {
        for (int i = 0; i < n; i++) {
            tick();
        }
    }

    public void attachHardware(MEM mem) {
        this.mem = mem;
    }

    public void tick() {

        if (halted) return;

        R = (R + 1) & 0xff; // memory refresh register

        tickCounter++;

        if (pendingEnableInterrupt == 1) {
            interruptEnabled = 1;
            pendingEnableInterrupt = 0;
        }
        if (pendingDisableInterrupt == 1) {
            interruptEnabled = 0;
            pendingDisableInterrupt = 0;
        }


        int currentInstruction = mem.peek((PC & 0xFFFF)) & 0xff;

        if (enableDecompile) {
            lastDecompile = decompile(microcode, PC, currentInstruction);
            lastData = "";
        }

        PC++;

        MicroOp[] ops = microcode.getInstructionCode(currentInstruction);

        if (ops[0] == MicroOp.PREFIX_CB) {
            // Handle ED prefix instructions.
            currentInstruction = mem.peek(PC);
            if (enableDecompile)
                lastDecompile = "(Prefix CB) " + decompile(codeTableManager.codeTableCB, PC, currentInstruction);
            PC++;
            ops = codeTableManager.codeTableCB.getInstructionCode(currentInstruction);
        }
        if (ops[0] == MicroOp.PREFIX_DD) {
            // Handle ED prefix instructions.
            currentInstruction = mem.peek(PC);
            if (enableDecompile)
                lastDecompile = "(Prefix DD) " + decompile(codeTableManager.codeTableDD, PC, currentInstruction);
            PC++;
            ops = codeTableManager.codeTableDD.getInstructionCode(currentInstruction);

            if (ops[0] == MicroOp.PREFIX_DD_CB) {
                displacement = mem.peek(PC++); // Read D first
                currentInstruction = mem.peek(PC);
                if (enableDecompile)
                    lastDecompile = "(Prefix DDCB) " + decompile(codeTableManager.codeTableDDCB, PC, currentInstruction) + " d=" + Utils.toHex2(displacement);
                PC++;
                ops = codeTableManager.codeTableDDCB.getInstructionCode(currentInstruction);
            }
        } else if (ops[0] == MicroOp.PREFIX_ED) {
            // Handle ED prefix instructions.
            currentInstruction = mem.peek(PC);
            if (enableDecompile)
                lastDecompile = "(Prefix ED) " + decompile(codeTableManager.codeTableED, PC, currentInstruction);
            PC++;
            ops = codeTableManager.codeTableED.getInstructionCode(currentInstruction);
        } else if (ops[0] == MicroOp.PREFIX_FD) { // Handle FD prefix instructions.
            currentInstruction = mem.peek(PC);
            if (enableDecompile)
                lastDecompile = "(Prefix FD) " + decompile(codeTableManager.codeTableFD, PC, currentInstruction);
            PC++;
            ops = codeTableManager.codeTableFD.getInstructionCode(currentInstruction);

            if (ops[0] == MicroOp.PREFIX_FD_CB) {
                displacement = mem.peek(PC++); // Read D first
                currentInstruction = mem.peek(PC);
                if (enableDecompile)
                    lastDecompile = "(Prefix FDCB) " + decompile(codeTableManager.codeTableFDCB, PC, currentInstruction) + " d=" + Utils.toHex2(displacement);
                PC++;
                ops = codeTableManager.codeTableFDCB.getInstructionCode(currentInstruction);
            }
        }

        processMicroOps(ops);

        if (SP < 0) {
            System.out.println(lastDecompile);
            throw new RuntimeException("Stack pointer went negative");
        }
    }

    public void processMicroOps(MicroOp[] ops) {
        if (ops.length > 0 && ops[0] != MicroOp.TODO) {
            for (MicroOp op : ops) {
                doMicroOp(op);
            }
        }
    }

    public String decompile(CodeTable codeTable, int address, int instruction) {
        String str = "[" + Utils.toHex4(address) + "/" + Utils.toHex2(instruction) + "] ";
        str += codeTable.getInstructionName(instruction);
        return str;
    }

    private void doMicroOp(MicroOp op) {

        int wrk = 0;
        boolean bak_s, bak_z, bak_p;

        switch (op) {
            case HALT:
                halted = true;
                break;
            case NOP:
//                System.out.println("NOP");
                break;
            case FETCH_A:
                dataBus = A;
                break;
            case FETCH_B:
                dataBus = B;
                break;
            case FETCH_C:
                dataBus = C;
                break;
            case FETCH_D:
                dataBus = D;
                break;
            case FETCH_E:
                dataBus = E;
                break;
            case FETCH_H:
                dataBus = H;
                break;
            case FETCH_L:
                dataBus = L;
                break;
            case FETCH_R:
                dataBus = R;
                break;
            case FETCH_AF:
                dataBus = combineBytes(A, FL);
                break;
            case FETCH_BC:
                dataBus = combineBytes(B, C);
                break;
            case FETCH_DE:
                dataBus = combineBytes(D, E);
                break;
            case FETCH_HL:
                dataBus = combineBytes(H, L);
                break;
            case FETCH_SP:
                dataBus = SP;
                break;
            case FETCH_I:
                dataBus = I;
                break;
            case FETCH_IY:
                dataBus = IY;
                break;
            case FETCH_IX:
                dataBus = IX;
                break;
            case FETCH_IX_L:
                dataBus = IX & 0xFF;
                break;
            case FETCH_IY_L:
                dataBus = IY & 0xFF;
                break;
            case FETCH_IX_H:
                dataBus = (IX >> 8) & 0xFF;
                break;
            case FETCH_IY_H:
                dataBus = (IY >> 8) & 0xFF;
                break;
            case FETCH_0:
                dataBus = 0;
                break;
            case FETCH_8:
                dataBus = getNextByte();
                if (enableDecompile) lastData = "  (" + Utils.toHex2(dataBus) + ")";
                break;
            case FETCH_16:
                dataBus = getNextWord();
                if (enableDecompile) lastData = "  (" + Utils.toHex4(dataBus) + ")";
                break;
            case FETCH_8_ADDRESS:
                addrBus = getNextByte();
                if (enableDecompile) lastData = "  *(" + Utils.toHex2(addrBus) + ")";
                break;
            case FETCH_16_ADDRESS:
                addrBus = getNextWord();
                if (enableDecompile) lastData = "  *(" + Utils.toHex4(addrBus) + ")";
                break;
            case FETCH_BYTE_TO_DISPLACEMENT:
                displacement = getNextByte();
                if (enableDecompile) lastData = "  d(" + Utils.toHex2(displacement) + ")";
                break;
            case SET_ADDR_FROM_A:
                addrBus = A;
                break;
            case SET_ADDR_FROM_HL:
                addrBus = getHL();
                break;
            case SET_ADDR_FROM_HL_INC:
                addrBus = getHL();
                setHL(getHL() + 1);
                break;
            case SET_ADDR_FROM_HL_DEC:
                addrBus = getHL();
                setHL(getHL() - 1);
                break;
            case SET_ADDR_FROM_BC:
                addrBus = getBC();
                break;
            case SET_ADDR_FROM_DE:
                addrBus = getDE();
                break;
            case FETCH_BYTE_FROM_ADDR:
                dataBus = mem.peek(addrBus);
                break;
            case FETCH_WORD_FROM_ADDR:
                dataBus = mem.peekWord(addrBus);
                break;
            case STORE_BYTE_AT_ADDRESS:
                mem.poke(addrBus, dataBus);
                if (enableDecompile) lastData += "  *(" + Utils.toHex4(addrBus) + ")";
                break;
            case STORE_WORD_AT_ADDRESS:
                mem.pokeWord(addrBus, dataBus);
                if (enableDecompile) lastData += "  *(" + Utils.toHex4(addrBus) + ")";
                break;
            case FETCH_pIY_D:
                dataBus = mem.peek(IY + convertSignedByte(displacement));
                if (enableDecompile) lastData += "  *(" + Utils.toHex4(IY + displacement) + ")";
                break;
            case FETCH_pIX_D:
                dataBus = mem.peek(IX + convertSignedByte(displacement));
                if (enableDecompile) lastData += "  *(" + Utils.toHex4(IX + displacement) + ")";
                break;
            case FETCH_pHL:
                dataBus = mem.peek(getHL());
                break;
            case STORE_A:
                A = dataBus;
                break;
            case STORE_B:
                B = dataBus;
                break;
            case STORE_C:
                C = dataBus;
                break;
            case STORE_D:
                D = dataBus;
                break;
            case STORE_E:
                E = dataBus;
                break;
            case STORE_H:
                H = dataBus;
                break;
            case STORE_L:
                L = dataBus;
                break;
            case STORE_I:
                I = dataBus;
                break;
            case STORE_BC:
                setBC(dataBus);
                break;
            case STORE_DE:
                setDE(dataBus);
                break;
            case STORE_HL:
                setHL(dataBus);
                break;
            case STORE_AF:
                setAF(dataBus);
                break;
            case STORE_SP:
                SP = dataBus;
                break;
            case STORE_PC:
                PC = dataBus;
                break;
            case STORE_IY:
                IY = dataBus;
                break;
            case STORE_IX:
                IX = dataBus;
                break;
            case STORE_IX_H:
                IX = (IX & 0xff) | ((dataBus & 0xff) << 8);
                break;
            case STORE_IX_L:
                IX = (IX & 0xff00) | ((dataBus & 0xff));
                break;
            case STORE_IY_H:
                IY = (IY & 0xff) | ((dataBus & 0xff) << 8);
                break;
            case STORE_IY_L:
                IY = (IY & 0xff00) | ((dataBus & 0xff));
                break;
            case LD_A_I:
                A = I;
                setFlag(FLAG_S, (A & 0x80) > 0);
                handleZeroFlag(A);
                unsetFlag(FLAG_H);
                unsetFlag(FLAG_N);
                setFlag(FLAG_PV, iff2 > 0);
                handle53Flag(A);
                break;
            case STORE_R:
                R = dataBus;
                break;
            case STORE_p16WORD:
                mem.poke(addrBus, getLowByte(dataBus));
                mem.poke(addrBus + 1, getHighByte(dataBus));
                break;
            case STORE_pIY_D:
                mem.poke(IY + convertSignedByte(displacement), dataBus);
                break;
            case STORE_pIX_D:
                mem.poke(IX + convertSignedByte(displacement), dataBus);
                break;
            case STORE_pHL:
                mem.poke(getHL(), dataBus);
                break;
            case INC_8:
                dataBus = doInc(dataBus);
                break;
            case INC_16:
                dataBus = (dataBus + 1) & 0xffff;
                break;
            case DEC_8:
                dataBus = doDec(dataBus);

                break;
            case DEC_16:
                dataBus = dataBus - 1;
                if (dataBus < 0)
                    dataBus = 0xffff;
                break;
            case ADD_HL:
                wrk = getHL() + dataBus;

                setFlag(FLAG_C, wrk > 0xFFFF);

                unsetFlag(FLAG_N);

                if ((((getHL() & 0x0FFF) + (dataBus & 0xFFF)) & 0x1000) > 0)
                    setFlag(FLAG_H);
                else
                    unsetFlag(FLAG_H);

                handle53Flag(H);

                dataBus = wrk & 0xFFFF;
                break;
            case ADD_IY:
                wrk = IY + dataBus;

                if (wrk > 0xffff)
                    setFlag(FLAG_C);
                else
                    unsetFlag(FLAG_C);

                unsetFlag(FLAG_N);

                //if ((((getHL() & 0xFFF) + (ac2.val & 0xFFF)) & 0x1000) > 0)
                if ((((IY & 0x0FFF) + (dataBus & 0x0FFF)) & 0x1000) > 0)
                    setFlag(FLAG_H);
                else
                    unsetFlag(FLAG_H);

                dataBus = wrk & 0xFFFF;
                break;
            case ADD_IX:
                wrk = IX + dataBus;

                if (wrk > 0xffff)
                    setFlag(FLAG_C);
                else
                    unsetFlag(FLAG_C);

                unsetFlag(FLAG_N);

                //if ((((getHL() & 0xFFF) + (ac2.val & 0xFFF)) & 0x1000) > 0)
                if ((((IX & 0x0FFF) + (dataBus & 0x0FFF)) & 0x1000) > 0)
                    setFlag(FLAG_H);
                else
                    unsetFlag(FLAG_H);

                dataBus = wrk & 0xFFFF;
                break;
            case ADD:
                if ((dataBus & 0xff00) > 0) throw new RuntimeException("8 bit instruction overflow");

                wrk = A + dataBus;

                setFlag(FLAG_C, wrk > 0xFF);
                handleZeroFlag(wrk & 0xff);
                handleSignFlag(wrk & 0xff);
                unsetFlag(FLAG_N);
                setFlag(FLAG_PV, ((A & 0x80) == (dataBus & 0x80)) && ((A & 0x80) != (wrk & 0x80)));

                if ((((A & 0xF) + (dataBus & 0xF)) & 0x10) > 0)
                    setFlag(FLAG_H);
                else
                    unsetFlag(FLAG_H);

                handle53Flag(wrk);

                A = wrk & 0xff;

                break;
            case ADC:
                if ((dataBus & 0xff00) > 0) throw new RuntimeException("8 bit instruction overflow");

                wrk = A + dataBus + (testFlag(FLAG_C) ? 1 : 0);

                handleZeroFlag(wrk & 0xff);
                handleSignFlag(wrk & 0xff);
                if (wrk > 0xff)
                    setFlag(FLAG_C);
                else
                    unsetFlag(FLAG_C);

                unsetFlag(FLAG_N);

                if (((A ^ dataBus ^ wrk) & 0x10) > 0)
                    setFlag(FLAG_H);
                else
                    unsetFlag(FLAG_H);

                setFlag(FLAG_PV, ((A & 0x80) == (dataBus & 0x80)) && ((A & 0x80) != (wrk & 0x80)));

                handle53Flag(wrk);

                A = wrk & 0xff;

                break;
            case SUB:
                if ((dataBus & 0xff00) > 0) throw new RuntimeException("8 bit instruction overflow");

                A = doSub(dataBus);


                break;
            case SBC:
                if ((dataBus & 0xff00) > 0) throw new RuntimeException("8 bit instruction overflow");

                wrk = A - (dataBus & 0xff) - (testFlag(FLAG_C) ? 1 : 0);


                setFlag(FLAG_S, ((wrk & 0x80) > 0));
                setFlag(FLAG_Z, ((wrk & 0xff) == 0));
                setFlag(FLAG_H, ((A ^ dataBus ^ wrk) & 0x10) != 0);

                if ((wrk & 0x100) > 0) setFlag(FLAG_C);
                else unsetFlag(FLAG_C);

                setFlag(FLAG_PV, ((A & 0x80) != (dataBus & 0x80)) && ((A & 0x80) != (wrk & 0x80)));

                setFlag(FLAG_N);

                handle53Flag(wrk);

                A = wrk & 0xff;
                break;
            case SBC_HL:
                dataBus += (testFlag(FLAG_C) ? 1 : 0);

                wrk = getHL() - dataBus;

                setFlag(FLAG_S, (wrk & 0x8000) > 0);
                setFlag(FLAG_Z, (wrk & 0xffff) == 0);

                setFlag(FLAG_H, (((getHL() & 0x0FFF) - (dataBus & 0x0FFF)) & 0x1000) > 0);
                setFlag(FLAG_PV, (((getHL() & 0x8000) != (dataBus & 0x8000))) &&
                        ((wrk & 0x8000) != (getHL() & 0x8000)));

                setFlag(FLAG_N);
                setFlag(FLAG_C, (wrk & 0x10000) > 0);

                setHL(wrk);
                break;
            case ADC_HL:
                dataBus += (testFlag(FLAG_C) ? 1 : 0);

                wrk = getHL() + dataBus;

                setFlag(FLAG_S, (wrk & 0x8000) > 0);
                setFlag(FLAG_Z, (wrk & 0xffff) == 0);

                setFlag(FLAG_H, (((getHL() & 0x0FFF) + (dataBus & 0x0FFF)) & 0x1000) > 0);
                setFlag(FLAG_PV, (((getHL() & 0x8000) == (dataBus & 0x8000))) &&
                        ((wrk & 0x8000) != (getHL() & 0x8000)));

                unsetFlag(FLAG_N);
                setFlag(FLAG_C, (wrk & 0x10000) > 0);

                setHL(wrk);
                break;
            case AND:
                if ((dataBus & 0xff00) > 0) throw new RuntimeException("8 bit instruction overflow");

                wrk = A & (dataBus & 0xff);
                A = wrk;

                handleZeroFlag(wrk);
                handleSignFlag(wrk & 0xff);
                handleParityFlag(wrk);
                unsetFlag(FLAG_N);
                unsetFlag(FLAG_C);
                setFlag(FLAG_H);

                handle53Flag(wrk);

                break;
            case XOR:
                if ((dataBus & 0xff00) > 0) throw new RuntimeException("8 bit instruction overflow");

                wrk = (A ^ dataBus) & 0xff;
                handleZeroFlag(wrk);
                handleSignFlag(wrk);
                handleParityFlag(wrk);
                unsetFlag(FLAG_N);
                unsetFlag(FLAG_C);
                unsetFlag(FLAG_H);
                A = wrk & 0xff;
                handle53Flag(wrk);
                break;
            case OR:
                if ((dataBus & 0xff00) > 0) throw new RuntimeException("8 bit instruction overflow");

                wrk = (A | dataBus) & 0xFF;
                handleZeroFlag(wrk);
                handleSignFlag(wrk);
                handleParityFlag(wrk);
                unsetFlag(FLAG_N);
                unsetFlag(FLAG_C);
                unsetFlag(FLAG_H);
                A = wrk;
                handle53Flag(wrk);
                break;
            case CP:
                if ((dataBus & 0xff00) > 0) throw new RuntimeException("(CP) 8 bit instruction overflow");

                wrk = A;
                doSub(dataBus);
                A = wrk; // Reinstate A since we don't want to change it

                break;

            case RLC: // Rotate left with carry
                dataBus = doRLC(dataBus);
                break;
            case RRC:
                dataBus = doRRC(dataBus);
                break;
            case RL:
                dataBus = doRL(dataBus);
                break;
            case RR:
                dataBus = doRR(dataBus);
                break;

            case RLCA:
                // "A" Specific version that affects fewer flags
                // TODO: make function to store and restore these flags.
                bak_s = testFlag(FLAG_S);
                bak_z = testFlag(FLAG_Z);
                bak_p = testFlag(FLAG_PV);
                dataBus = doRLC(dataBus);
                setFlag(FLAG_S, bak_s);
                setFlag(FLAG_Z, bak_z);
                setFlag(FLAG_PV, bak_p);
                break;
            case RRCA:
                // "A" Specific version that affects fewer flags
                bak_s = testFlag(FLAG_S);
                bak_z = testFlag(FLAG_Z);
                bak_p = testFlag(FLAG_PV);
                dataBus = doRRC(dataBus);
                setFlag(FLAG_S, bak_s);
                setFlag(FLAG_Z, bak_z);
                setFlag(FLAG_PV, bak_p);
                break;
            case RLA:
                // "A" Specific version that affects fewer flags
                bak_s = testFlag(FLAG_S);
                bak_z = testFlag(FLAG_Z);
                bak_p = testFlag(FLAG_PV);
                dataBus = doRL(dataBus);
                setFlag(FLAG_S, bak_s);
                setFlag(FLAG_Z, bak_z);
                setFlag(FLAG_PV, bak_p);
                break;
            case RRA:
                // "A" Specific version that affects fewer flags
                bak_s = testFlag(FLAG_S);
                bak_z = testFlag(FLAG_Z);
                bak_p = testFlag(FLAG_PV);
                dataBus = doRR(dataBus);
                setFlag(FLAG_S, bak_s);
                setFlag(FLAG_Z, bak_z);
                setFlag(FLAG_PV, bak_p);
                break;

            case SLA:
                // The contents of the memory location pointed to by IY plus d are shifted left one bit position.
                // The contents of bit 7 are copied to the carry flag and a zero is put into bit 0.

                setFlag(FLAG_C, (dataBus & 0x80) > 0);
                unsetFlag(FLAG_N);
                unsetFlag(FLAG_H);

                wrk = (dataBus << 1) & 0xFF;
                setFlag(FLAG_Z, wrk == 0);
                setFlag(FLAG_S, (wrk & 0x80) > 0);
                handleParityFlag(wrk);
                dataBus = wrk;

                handle53Flag(wrk);

                break;
            case SRA:
                // The contents of the memory location pointed to by IY plus d are shifted right one bit position.
                // The contents of bit 0 are copied to the carry flag and the previous contents of bit 7 are unchanged.

                setFlag(FLAG_C, (dataBus & 1) > 0);

                unsetFlag(FLAG_N);
                unsetFlag(FLAG_H);

                wrk = ((dataBus >> 1) & 0x7F) | (dataBus & 0x80);

                handleZeroFlag(wrk);
                handleParityFlag(wrk);
                setFlag(FLAG_S, (wrk & 0x80) > 0);
                handle53Flag(wrk);

                dataBus = wrk;

                break;
            case SLL:
                // The contents of the memory location pointed to by IY plus d are shifted left one bit position.
                // The contents of bit 7 are put into the carry flag and a one is put into bit 0.

                setFlag(FLAG_C, (dataBus & 0x80) > 0);

                unsetFlag(FLAG_N);
                unsetFlag(FLAG_H);

                wrk = ((dataBus << 1) & 0xFF) | 1;

                handleZeroFlag(wrk);
                setFlag(FLAG_S, (wrk & 0x80) > 0);
                handleParityFlag(wrk);
                handle53Flag(wrk);

                dataBus = wrk;

                break;
            case SRL:
                // data is shifted right one bit position.
                // The contents of bit 0 are copied to the carry flag and a zero is put into bit 7.
                setFlag(FLAG_C, (dataBus & 1) > 0);

                unsetFlag(FLAG_N);
                unsetFlag(FLAG_H);

                wrk = ((dataBus >> 1)) & 0x7F;

                handleZeroFlag(wrk);
                unsetFlag(FLAG_S);
                handleParityFlag(wrk);
                handle53Flag(wrk);

                dataBus = wrk;

                break;
            case NEG:
                if (A != 0x80) {
                    A = convertSignedByte(A);
                    A = (-A) & 0xFF;
                }

                setFlag(FLAG_S, (A & 0x80) > 0);
                setFlag(FLAG_Z, A == 0);
                setFlag(FLAG_H, ((-A) & 0x0F) > 0);
                setFlag(FLAG_PV, (A == 0x80));
                setFlag(FLAG_N);
                setFlag(FLAG_C, A != 0);
                handle53Flag(A);
                break;
            case MLT_BC:
                setBC((B * C) & 0xFFFF);
                break;
            case MLT_DE:
                setDE((D * E) & 0xFFFF);
                break;
            case MLT_HL:
                setHL((H * L) & 0xFFFF);
                break;
            case MLT_SP:
                SP = (((SP & 0xFF) * ((SP >> 8) & 0xFF)) & 0xFFFF);
                break;
            case JRNZ:
                conditionalJumpRelative(dataBus, !testFlag(FLAG_Z));
                break;
            case JRZ:
                conditionalJumpRelative(dataBus, testFlag(FLAG_Z));
                break;
            case JRNC:
                conditionalJumpRelative(dataBus, !testFlag(FLAG_C));
                break;
            case JRC:
                conditionalJumpRelative(dataBus, testFlag(FLAG_C));
                break;
            case JR:
                conditionalJumpRelative(dataBus, true);
                break;
            case JPZ:
                conditionalJump(addrBus, testFlag(FLAG_Z));
                break;
            case JPNZ:
                conditionalJump(addrBus, !testFlag(FLAG_Z));
                break;
            case JPNC:
                conditionalJump(addrBus, !testFlag(FLAG_C));
                break;
            case JP_PO:
                conditionalJump(addrBus, !testFlag(FLAG_PV));
                break;
            case JP_PE:
                conditionalJump(addrBus, testFlag(FLAG_PV));
                break;
            case JP_P:
                conditionalJump(addrBus, !testFlag(FLAG_S));
                break;
            case JP_M:
                conditionalJump(addrBus, testFlag(FLAG_S));
                break;
            case DJNZ:
                // The B register is decremented, and if not zero, the signed value d is added to PC.
                // The jump is measured from the start of the instruction opcode.
                B = (B - 1) & 0xff;
                conditionalJumpRelative(dataBus, B != 0);
                break;
            case JPC:
                conditionalJump(addrBus, testFlag(FLAG_C));
                break;
            case JP:
                PC = addrBus;
                break;
            case RET:
                PC = popW();
                break;
            case RETI:
                wrk = popW();
                PC = wrk;
                enableInterrupts();
                break;
            case RETZ:
                conditionalReturn(testFlag(FLAG_Z));
                break;
            case RETNZ:
                conditionalReturn(!testFlag(FLAG_Z));
                break;
            case RETNC:
                conditionalReturn(!testFlag(FLAG_C));
                break;
            case RETC:
                conditionalReturn(testFlag(FLAG_C));
                break;
            case RET_PO:
                conditionalReturn(!testFlag(FLAG_PV));
                break;
            case RET_P:
                conditionalReturn(!testFlag(FLAG_S));
                break;
            case RET_PE:
                conditionalReturn(testFlag(FLAG_PV));
                break;
            case RET_M:
                conditionalReturn(testFlag(FLAG_S));
                break;
            case RETN:
                PC = popW();
                iff1 = iff2;
                break;
            case OUT:
                mem.setPort(addrBus, dataBus);
                break;
            case OUT_PORT_C:
                mem.setPort(getBC() & 0xFFFF, dataBus);
                break;
            case IN: // Used for IN A, (n)
                if (enableDecompile) lastData += "port(" + Utils.toHex4(((A & 0xff) << 8) | (dataBus & 0xff)) + ")";

                dataBus = doIn(((A & 0xff) << 8) | (dataBus & 0xff));

//                System.out.println(this.lastDecompile);
                break;
            case OUT0:

                mem.setPort(addrBus & 0xFF, dataBus & 0xFF);
                break;
            case IN0:
                dataBus = doIn(addrBus);
                break;
            case FETCH_PORT_C:

                dataBus = doIn(getBC() & 0xFFFF);
                break;
            case DAA: // Decimal Adjust Accumulator to get a correct BCD representation after an arithmetic instruction

                int temp = A;
                boolean flagN = testFlag(FLAG_N);
                boolean flagH = testFlag(FLAG_H);
                boolean flagC = testFlag(FLAG_C);

                if (!flagN) {
                    if (flagH || ((A & 0x0f) > 9))
                        temp += 0x06;
                    if (flagC || (A > 0x99))
                        temp += 0x60;
                } else {
                    if (flagH || ((A & 0x0f) > 9))
                        temp -= 0x06;
                    if (flagC || (A > 0x99))
                        temp -= 0x60;
                }

                setFlag(FLAG_S, (temp & 0x80) > 0);
                setFlag(FLAG_Z, (temp & 0xFF) == 0);
                setFlag(FLAG_H, ((A & 0x10) ^ (temp & 0x10)) > 0);
                handleParityFlag(temp & 0xff);
                setFlag(FLAG_C, flagC || (A > 0x99));
                handle53Flag(temp & 0xff);

                A = temp & 0xFF;

                break;
            case CPL: // ComPLement accumulator (A = ~A).
                dataBus = (~(A)) & 0xFF;
                setFlag(FLAG_N);
                setFlag(FLAG_H);
                A = dataBus & 0xff;
                handle53Flag(A);
                break;
            case SCF: // Set carry flag
                setFlag(FLAG_C);
                unsetFlag(FLAG_N);
                unsetFlag(FLAG_H);
                handle53Flag(A);
                break;
            case CCF: // Clear carry flag
                unsetFlag(FLAG_N);
                setFlag(FLAG_H, testFlag(FLAG_C));

                if (testFlag(FLAG_C)) {
                    unsetFlag(FLAG_C);
                } else {
                    setFlag(FLAG_C);
                }
                break;
            case PUSHW:
                pushW(dataBus);
                break;
            case POPW:
                dataBus = popW();
                break;
            case CALL:
                call(addrBus);
                break;
            case CALLNZ:
                conditionalCall(addrBus, !testFlag(FLAG_Z));
                break;
            case CALLZ:
                conditionalCall(addrBus, testFlag(FLAG_Z));
                break;
            case CALLC:
                conditionalCall(addrBus, testFlag(FLAG_C));
                break;
            case CALLNC:
                conditionalCall(addrBus, !testFlag(FLAG_C));
                break;
            case CALLPO:
                conditionalCall(addrBus, !testFlag(FLAG_PV));
                break;
            case CALLP:
                conditionalCall(addrBus, !testFlag(FLAG_S));
                break;
            case CALLM:
                conditionalCall(addrBus, testFlag(FLAG_S));
                break;
            case CALLPE:
                conditionalCall(addrBus, testFlag(FLAG_PV));
                break;

            case RST_18H:
                jumpToInterrupt(0x0018);
                break;
            case RST_10H:
                jumpToInterrupt(0x0010);
                break;
            case RST_20H:
                jumpToInterrupt(0x0020);
                break;
            case RST_30H:
                jumpToInterrupt(0x0030);
                break;
            case RST_38H:
                jumpToInterrupt(0x0038);
                break;
            case RST_08H:
                jumpToInterrupt(0x0008);
                break;
            case RST_28H:
                jumpToInterrupt(0x0028);
                break;
            case RST_00H:
                jumpToInterrupt(0x0000);
                break;

            case SET0:
                dataBus = setBit(dataBus, 0);
                break;
            case SET1:
                dataBus = setBit(dataBus, 1);
                break;
            case SET2:
                dataBus = setBit(dataBus, 2);
                break;
            case SET3:
                dataBus = setBit(dataBus, 3);
                break;
            case SET4:
                dataBus = setBit(dataBus, 4);
                break;
            case SET5:
                dataBus = setBit(dataBus, 5);
                break;
            case SET6:
                dataBus = setBit(dataBus, 6);
                break;
            case SET7:
                dataBus = setBit(dataBus, 7);
                break;

            case RES0:
                dataBus = resetBit(dataBus, 0);
                break;
            case RES1:
                dataBus = resetBit(dataBus, 1);
                break;
            case RES2:
                dataBus = resetBit(dataBus, 2);
                break;
            case RES3:
                dataBus = resetBit(dataBus, 3);
                break;
            case RES4:
                dataBus = resetBit(dataBus, 4);
                break;
            case RES5:
                dataBus = resetBit(dataBus, 5);
                break;
            case RES6:
                dataBus = resetBit(dataBus, 6);
                break;
            case RES7:
                dataBus = resetBit(dataBus, 7);
                break;

            case BIT0:
                testBit(dataBus, 0);
                break;
            case BIT1:
                testBit(dataBus, 1);
                break;
            case BIT2:
                testBit(dataBus, 2);
                break;
            case BIT3:
                testBit(dataBus, 3);
                break;
            case BIT4:
                testBit(dataBus, 4);
                break;
            case BIT5:
                testBit(dataBus, 5);
                break;
            case BIT6:
                testBit(dataBus, 6);
                break;
            case BIT7:
                testBit(dataBus, 7);
                break;

            case ADDSPNN:
                wrk = SP + convertSignedByte(dataBus & 0xff);

                if (((SP ^ convertSignedByte(dataBus & 0xff) ^ wrk) & 0x10) > 0)
                    setFlag(FLAG_H);
                else
                    unsetFlag(FLAG_H);

                if (((SP ^ convertSignedByte(dataBus & 0xff) ^ wrk) & 0x100) > 0)
                    setFlag(FLAG_C);
                else
                    unsetFlag(FLAG_C);

                unsetFlag(FLAG_Z);
                unsetFlag(FLAG_N);

                SP = wrk;

                break;
            case DI:
                disableInterrupts();
                break;
            case EI:
                enableInterrupts();
                break;
            case LDHLSPN:
                int signedByte = convertSignedByte(dataBus & 0xff);
                int ptr = SP + signedByte;

                wrk = ptr & 0xffff;

                unsetFlag(FLAG_N);
                unsetFlag(FLAG_Z);

                if (((SP ^ signedByte ^ wrk) & 0x100) == 0x100)
                    setFlag(FLAG_C);
                else
                    unsetFlag(FLAG_C);

                if (((SP ^ signedByte ^ wrk) & 0x10) == 0x10)
                    setFlag(FLAG_H);
                else
                    unsetFlag(FLAG_H);

                setHL(wrk);

                break;
            case EXX:
                exx();
                break;
            case EX_DE_HL:
                wrk = getDE();
                setDE(getHL());
                setHL(wrk);
                break;
            case EX_AF_AF:
                wrk = getAF();
                setAF(getAF_());
                setAF_(wrk);
                break;
            case EX_SP_HL:
                // ex (sp),hl
                // Exchanges (SP) with L, and (SP+1) with H.

                wrk = L;
                L = mem.peek(SP);
                mem.poke(SP, wrk);

                wrk = H;
                H = mem.peek(SP + 1);
                mem.poke(SP + 1, wrk);

                break;
            case EX_SP_IY:
                doExSPIy();
                break;
            case EX_SP_IX:
                doExSPIx();
                break;
            case RLD:
                doRLD();
                break;
            case RRD:
                doRRD();
                break;
            case LDI:
                doLDI();
                break;
            case LDIR:

                doLDI();

                if (getBC() != 0) {
                    PC = (PC - 2 & 0xFFFF);
                }

                break;
            case CPI:
                doCPI();
                break;
            case CPIR:
                doCPI();
                if (getBC() != 0 && !testFlag(FLAG_Z)) {
                    PC = (PC - 2 & 0xFFFF);
                }
                break;
            case LDD:
                doLDD();
                break;
            case LDDR:
                doLDD();

                if (getBC() != 0) {
                    PC = (PC - 2 & 0xFFFF);
                }
                break;
            case CPD:
                doCPD();
                break;
            case CPDR:
                doCPD();
                if (getBC() != 0) {
                    PC = (PC - 2 & 0xFFFF);
                }
                break;
            case OUTI:
                doOUTI();
                break;
            case OTIR:
                doOUTI();
                if (B != 0) {
                    PC = (PC - 2 & 0xFFFF);
                }
                break;
            case OUTD:
                doOUTD();
                break;
            case OTDR:
                doOUTD();
                if (B != 0) {
                    PC = (PC - 2 & 0xFFFF);
                }
                break;
            case IM_0:
                setInterruptMode(0);
                break;
            case IM_1:
                setInterruptMode(1);
                break;
            case IM_2:
                setInterruptMode(2);
                break;

            default:
                System.out.println("Unsupported micro op: " + op.name());
        }
    }

    public int doIn(int val) {

        int wrk = mem.getPort(val & 0xffff);

        handle53Flag(dataBus);
        unsetFlag(FLAG_H);
        unsetFlag(FLAG_N);
        setFlag(FLAG_Z, dataBus == 0);
        handleSignFlag(dataBus);
        handleZeroFlag(dataBus);
        handleParityFlag(dataBus);

        return wrk;
    }

    private void doRLD() {
        // The contents of the low-order nibble of (HL) are copied to the
        // high-order nibble of (HL).
        // The previous contents are copied to the low-order nibble of A.
        // The previous contents are copied to the low-order nibble of (HL).
        int HlVal = mem.peek(getHL());
        int temp1 = HlVal & 0xF0;
        int temp2 = A & 0x0F;

        HlVal = ((HlVal & 0x0F) << 4) | temp2;
        A = (A & 0xF0) | (temp1 >> 4);

        mem.poke(getHL(), HlVal);


        handleZeroFlag(A);
        handleSignFlag(A);
        unsetFlag(FLAG_H);
        handleParityFlag(A);
        unsetFlag(FLAG_N);
        handle53Flag(A);
    }

    private void doRRD() {
        // The contents of the low-order nibble of (HL) are copied to the
        // high-order nibble of (HL).
        // The previous contents are copied to the low-order nibble of A.
        // The previous contents are copied to the low-order nibble of (HL).
        int HlVal = mem.peek(getHL());
        int temp1 = HlVal & 0x0F;
        int temp2 = A & 0x0F;

        HlVal = ((HlVal & 0xF0) >> 4) | (temp2 << 4);
        A = (A & 0xF0) | (temp1);

        mem.poke(getHL(), HlVal);

        handleZeroFlag(A);
        handleSignFlag(A);
        unsetFlag(FLAG_H);
        handleParityFlag(A);
        unsetFlag(FLAG_N);
        handle53Flag(A);
    }

    private void doLDI() {
        // Transfers a byte of data from the memory location pointed to by HL to the memory
        // location pointed to by DE. Then HL and DE are incremented and BC is decremented.
        // p/v is reset if BC becomes zero and set otherwise.
        int data = mem.peek(getHL());
        mem.poke(getDE(), data);
        incHL();
        incDE();
        decBC();

        setFlag(FLAG_PV, getBC() != 0);

        unsetFlag(FLAG_N);
        unsetFlag(FLAG_H);

        //System.out.println("LDI " + (char) data + "  " + data);
    }

    private void doOUTD() {
        // B is decremented.
        // A byte from the memory location pointed to by HL is written to port C.
        // Then HL is decremented.


        int data = mem.peek(getHL());
        mem.setPort(getBC() & 0xFFFF, data);
        decHL();
        B = doDec(B);
    }

    private void doOUTI() {
        // B is decremented.
        // A byte from the memory location pointed to by HL is written to port C.
        // Then HL is incremented.

        int data = mem.peek(getHL());
        mem.setPort(getBC() & 0xFFFF, data);
        incHL();
        B = doDec(B);
    }

    private void compare(int value) {

        int tmp = A;
        doSub(value);
        A = tmp;
    }

    private void doCPI() {
        // Compares the value of the memory location pointed to by HL with A.
        // Then HL is incremented and BC is decremented.
        // p/v is reset if BC becomes zero and set otherwise.
        int data = mem.peek(getHL());

        boolean tempCarry = testFlag(FLAG_C);
        compare(data);
        setFlag(FLAG_C, tempCarry);

        incHL();
        decBC();

        setFlag(FLAG_PV, getBC() != 0);

    }

    private void doCPD() {
        // Compares the value of the memory location pointed to by HL with A.
        // Then HL is incremented and BC is decremented.
        // p/v is reset if BC becomes zero and set otherwise.
        int data = mem.peek(getHL());

        boolean tempCarry = testFlag(FLAG_C);
        compare(data);
        setFlag(FLAG_C, tempCarry);

        decHL();
        decBC();

        setFlag(FLAG_PV, getBC() != 0);

    }

    private void doLDD() {
        // Transfers a byte of data from the memory location pointed
        // to by HL to the memory location pointed to by DE.
        // Then HL, DE, and BC are decremented.
        // p/v is reset if BC becomes zero and set otherwise.
        int data = mem.peek(getHL());
        mem.poke(getDE(), data);

        decHL();
        decDE();
        decBC();

        setFlag(FLAG_PV, getBC() != 0);
        setFlag(FLAG_N, false);
        setFlag(FLAG_H, false);
        setFlag(FLAG_5, ((A + data) & 0x08) > 0);
        setFlag(FLAG_3, ((A + data) & 0x02) > 0);

    }

    private void incBC() {
        setBC((getBC() + 1) & 0xffff);
    }

    private void incDE() {
        setDE((getDE() + 1) & 0xffff);
    }

    private void incHL() {
        setHL((getHL() + 1) & 0xffff);
    }

    private void decBC() {
        setBC((getBC() - 1) & 0xffff);
    }

    private void decDE() {
        setDE((getDE() - 1) & 0xffff);
    }

    private void decHL() {
        setHL((getHL() - 1) & 0xffff);
    }

    public int doRLC(int val) {
        // The contents of data are rotated left one bit position.
        // The contents of bit 7 are copied to the carry flag and bit 0.

        int carryOut = ((val & 0x80) > 0) ? 1 : 0;

        if (carryOut == 1) setFlag(FLAG_C);
        else unsetFlag(FLAG_C);
        setFlag(FLAG_C, ((val & 0x80) > 0));

        int wrk = ((val << 1) | carryOut) & 0xFF;

        unsetFlag(FLAG_N);
        unsetFlag(FLAG_H);
        handleZeroFlag(wrk & 0xff);
        handleSignFlag(wrk & 0xff);
        handleParityFlag(wrk & 0xff);
        handle53Flag(wrk);

        return wrk;
    }

    public int doRRC(int val) {

        int carryOut = ((val & 0x01) > 0) ? 1 : 0;
        if (carryOut == 1) setFlag(FLAG_C);
        else unsetFlag(FLAG_C);

        int wrk = ((val >> 1) & 0x7F) | (carryOut << 7);

        unsetFlag(FLAG_N);
        unsetFlag(FLAG_H);
        handleZeroFlag(wrk & 0xFF);
        handleSignFlag(wrk & 0xFF);
        handleParityFlag(wrk & 0xFF);
        handle53Flag(wrk);

        return wrk;
    }

    public int doRL(int val) {
        int wrk = val << 1;
        if (testFlag(FLAG_C))
            wrk |= 1;


        setFlag(FLAG_C, ((val & 0x80) >> 7) > 0);

        wrk = wrk & 0xff;

        unsetFlag(FLAG_N);
        unsetFlag(FLAG_H);

        handleZeroFlag(wrk);
        handle53Flag(wrk);
        handleParityFlag(wrk);
        handleSignFlag(wrk & 0xFF);

        return wrk;
    }

    public int doRR(int val) {
        int carryIn = testFlag(FLAG_C) ? 1 : 0;
        int carryOut = ((val & 0x01) > 0) ? 1 : 0;

        int wrk = (val >> 1) | (carryIn << 7);

        if (carryOut == 1)
            setFlag(FLAG_C);
        else
            unsetFlag(FLAG_C);

        unsetFlag(FLAG_N);
        unsetFlag(FLAG_H);

        handleZeroFlag(wrk);
        handle53Flag(wrk);
        handleParityFlag(wrk);
        handleSignFlag(wrk);

        return wrk;
    }

    public int doInc(int val) {

        int wrk = val + 1;

        handleZeroFlag(wrk & 0xff);
        handleSignFlag(wrk & 0xff);
        unsetFlag(FLAG_N);
        setFlag(FLAG_H, (val & 0x0F) == 0x0f);
        setFlag(FLAG_PV, val == 0x7F);
        wrk &= 0xFF;
        handle53Flag(wrk);

        return wrk;
    }

    public int doDec(int val) {
        int wrk = val - 1;

        handleZeroFlag(wrk);
        handleSignFlag(wrk & 0xff);
        setFlag(FLAG_N);
        setFlag(FLAG_H, (val & 0xF) == 0);
        setFlag(FLAG_PV, val == 0x80);

        wrk &= 0xFF;

        handle53Flag(wrk);

        return wrk;
    }

    public int doSub(int val) {

        int wrk = A - val;

        setFlag(FLAG_C, (wrk & 0x100) > 0);
        handleZeroFlag(wrk & 0xff);
        handleSignFlag(wrk & 0xff);
        setFlag(FLAG_N);

        setFlag(FLAG_H, (((A & 0x0F) - (val & 0x0F)) & 0x10) > 0);
        setFlag(FLAG_PV, ((A & 0x80) != (val & 0x80)) && ((A & 0x80) != (wrk & 0x80)));

        handle53Flag(wrk);

        return wrk & 0xff;
    }

    public void doExSPIy() {

        int a = mem.peek(SP);
        mem.poke(SP, IY & 0xff);


        int b = mem.peek(SP + 1);
        mem.poke(SP + 1, (IY >> 8) & 0xff);

        IY = (b << 8) | a;
    }

    public void doExSPIx() {

        int a = mem.peek(SP);
        mem.poke(SP, IX & 0xff);


        int b = mem.peek(SP + 1);
        mem.poke(SP + 1, (IX >> 8) & 0xff);

        IX = (b << 8) | a;
    }

    private void setInterruptMode(int i) {
        interruptMode = i;
    }

    // TODO: replace shifts here with constants for each bit.
    public int setBit(int val, int bit) {
        return val | (1 << bit);
    }

    public int resetBit(int val, int bit) {
        return val & ~(1 << bit);
    }

    public void testBit(int val, int bit) {

        if ((val & (1 << bit)) == 0) {
            setFlag(FLAG_Z);
            setFlag(FLAG_PV);
        } else {
            unsetFlag(FLAG_Z);
            unsetFlag(FLAG_PV);
        }

        unsetFlag(FLAG_N);
        setFlag(FLAG_H);
        setFlag(FLAG_S, (bit == 7 && !testFlag(FLAG_Z)));

    }

    // Combine two bytes into one 16 bit value.
    public int combineBytes(int h, int l) {
        return ((h & 0xff) << 8) | (l & 0xff);
    }

    // Set a flag on the interrupt register.
    public void requestInterrupt(int val) {
        this.mem.RAM[0xFF0F] |= val;
    }

    public void jumpRelative(int val) {
        int move = convertSignedByte(val & 0xFF);
        PC = (PC + move) & 0xffff;
    }

    public void conditionalJumpRelative(int val, boolean condition) {
        if (!condition) return;
        int move = convertSignedByte(val & 0xFF);
        PC = (PC + move) & 0xffff;
    }

    public void conditionalJump(int address, boolean condition) {
        if (!condition) return;
        PC = address;
    }

    public void conditionalReturn(boolean condition) {
        if (!condition) return;
        PC = popW();
    }

    public int convertSignedByte(int val) {
        if ((val & 0b1000_0000) > 0) {
            //return -1 - ((~val) & 0xff);
            return -((0xFF & (~val & 0xff)) + 1);
        }
        return val;
    }

    public int getAF() {
        return combineBytes(A, FL);
    }

    public void setAF(int val) {
        this.A = getHighByte(val);
        this.FL = getLowByte(val);
    }

    public int getAF_() {
        return combineBytes(A_, FL_);
    }

    public void setAF_(int val) {
        this.A_ = getHighByte(val);
        this.FL_ = getLowByte(val);
    }

    public int getHighByte(int val) {
        return (val >> 8) & 0xff;
    }

    public int getLowByte(int val) {
        return val & 0xff;
    }

    public int getBC() {
        return combineBytes(B, C);
    }

    public void setBC(int val) {
        val = 0xffff & val;
        this.B = getHighByte(val);
        this.C = getLowByte(val);
    }

    public int getDE() {
        return combineBytes(D, E);
    }

    public void setDE(int val) {
        this.D = getHighByte(val);
        this.E = getLowByte(val);
    }

    public int getHL() {
        return combineBytes(H, L);
    }

    public void setHL(int val) {
        this.H = getHighByte(val);
        this.L = getLowByte(val);
    }

    // Exchanges the 16-bit contents of BC, DE, and HL with BC', DE', and HL'.
    public void exx() {
        int[] vals = {B, C, D, E, H, L, B_, C_, D_, E_, H_, L_};
        B = vals[6];
        C = vals[7];
        D = vals[8];
        E = vals[9];
        H = vals[10];
        L = vals[11];
        B_ = vals[0];
        C_ = vals[1];
        D_ = vals[2];
        E_ = vals[3];
        H_ = vals[4];
        L_ = vals[5];
    }

    public void enableInterrupts() {
        pendingEnableInterrupt = 1;
    }

    public void disableInterrupts() {
        pendingDisableInterrupt = 1;
    }

    public int popW() {
        int lb = mem.peek((SP++) & 0xffff);
        int hb = mem.peek((SP++) & 0xffff);
        return combineBytes(hb, lb);
    }

    public void handleZeroFlag(int val) {
        if ((val & 0xffff) == 0)
            setFlag(FLAG_Z);
        else
            unsetFlag(FLAG_Z);
    }

    public void handleSignFlag(int val) {
        if ((val & 0x80) > 0)
            setFlag(FLAG_S);
        else
            unsetFlag(FLAG_S);
    }

    public void handle53Flag(int val) {
        if ((val & 0x20) > 0)
            setFlag(FLAG_5);
        else
            unsetFlag(FLAG_5);

        if ((val & 0x08) > 0)
            setFlag(FLAG_3);
        else
            unsetFlag(FLAG_3);
    }

    public void handleParityFlag(int val) {
        if (parityTable[val & 0xFF])
            setFlag(FLAG_PV);
        else
            unsetFlag(FLAG_PV);
    }

    public void buildParityTable() {
        int count;
        for (int i = 0; i < 0xFF; i++) {
            count = 0;
            if ((i & 0b0000_0001) > 0) count++;
            if ((i & 0b0000_0010) > 0) count++;
            if ((i & 0b0000_0100) > 0) count++;
            if ((i & 0b0000_1000) > 0) count++;
            if ((i & 0b0001_0000) > 0) count++;
            if ((i & 0b0010_0000) > 0) count++;
            if ((i & 0b0100_0000) > 0) count++;
            if ((i & 0b1000_0000) > 0) count++;

            parityTable[i] = ((count & 0x01) == 0);
        }
    }


    // Get word at PC and move PC on.
    public int getNextWord() {
        int oprnd = mem.peek((PC++) & 0xFFFF) & 0xff;
        oprnd = (oprnd) + ((mem.peek((PC++) & 0xFFFF) & 0xff) << 8);
        PC = PC & 0xFFFF;
        return oprnd;
    }

    // Get word at PC and move PC on.
    public int getNextByte() {
        int val = mem.peek(PC & 0xFFFF) & 0xff;
        PC = ((PC + 1) & 0xFFFF);
        return val;
    }

    public void setFlag(int flag) {
        FL |= flag;
    }

    public void setFlag(int flag, boolean target) {
        if (target)
            FL |= flag;
        else
            FL &= ~(flag);
    }

    public void unsetFlag(int flag) {
        FL &= ~(flag);
    }

    public boolean testFlag(int flag) {
        return (FL & flag) > 0;
    }

    // STACK
    public void pushW(int val) {
        SP = (SP - 1) & 0xffff;
        mem.poke(SP, getHighByte(val));
        SP = (SP - 1) & 0xffff;
        mem.poke(SP, getLowByte(val));

    }

    public void jumpToInterrupt(int addr) {
        pushW(PC);
        PC = addr;
    }

    public void call(int addr) {
        pushW(PC);
        PC = addr;
    }

    public void conditionalCall(int addr, boolean condition) {
        if (condition) {
            pushW(PC);
            PC = addr;
        }
    }

    public void ret() {
        PC = popW();
    }
}
