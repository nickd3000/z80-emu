package com.physmo.z80;


import com.physmo.z80.microcode.CodeTable;
import com.physmo.z80.microcode.CodeTableManager;
import com.physmo.z80.microcode.MicroOp;

import java.text.MessageFormat;

public class CPU {
    // CPU Flags
    public static final int FLAG_ZERO = 0b0100_0000;
    public static final int FLAG_ADDSUB = 0b0000_0010;
    public static final int FLAG_HALFCARRY = 0b0001_0000;
    public static final int FLAG_CARRY = 0b0000_0001;
    public static final int FLAG_PARITY_OVERFLOW = 0b0000_0100; // P/V - Parity or Overflow


    String name = "Zilog Z80";

    CodeTableManager codeTableManager = new CodeTableManager();
    CodeTable microcode = codeTableManager.codeTableMain;


    // Registers.
    int A, B, C, D, E, H, L, I;
    int A_, B_, C_, D_, E_, H_, L_, I_;

    // Bus
    int dataBus = 0;
    int addrBus = 0;
    int PC = 0; // Program counter
    int SP = 0xdff0; // Stack pointer
    int FL = 0; // Flags
    int IX = 0; // Index register
    int IY = 0; // Index register
    int pendingEnableInterrupt = 0;
    int pendingDisableInterrupt = 0;
    int interruptEnabled = 0;
    int halt = 0;

    MEM mem = null;
    String lastDecompile = "";

    public void init() {
        A = 0xff;
        FL = 0xff;
        setBC(0xffff);
        setDE(0xffff);
        setHL(0xffff);
        IX = 0xffff;
        IY = 0xffff;
        B_ = 0xff;
        C_ = 0xff;
        D_ = 0xff;
        E_ = 0xff;
        H_ = 0xff;
        L_ = 0xff;
        I = 0;

    }

    public String dump() {
        String str = "";
        str += MessageFormat.format("A:{0} B:{1} C:{2} D:{3} E:{4} H:{5} L:{6} I:{7}",
                Utils.toHex2(A),
                Utils.toHex2(B),
                Utils.toHex2(C),
                Utils.toHex2(D),
                Utils.toHex2(E),
                Utils.toHex2(H),
                Utils.toHex2(L),
                Utils.toHex2(I));
        str += MessageFormat.format(" PC:{0} SP:{1} FL:{2} ",
                Utils.toHex4(PC),
                Utils.toHex4(SP),
                Utils.toHex4(FL));

        str += "   " + lastDecompile;
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
        int currentInstruction = mem.peek(PC);

        lastDecompile = decompile(microcode, PC, currentInstruction);

        PC++;

        MicroOp[] ops = microcode.getInstructionCode(currentInstruction);

        // Handle ED prefix instructions.
        if (ops[0] == MicroOp.PREFIX_ED) {
            currentInstruction = mem.peek(PC);
            lastDecompile = "(Prefix ED) " + decompile(codeTableManager.codeTableED, PC, currentInstruction);
            PC++;
            ops = codeTableManager.codeTableED.getInstructionCode(currentInstruction);
        } else
            // Handle FD prefix instructions.
            if (ops[0] == MicroOp.PREFIX_FD) {
                currentInstruction = mem.peek(PC);
                lastDecompile = "(Prefix FD) " + decompile(codeTableManager.codeTableFD, PC, currentInstruction);
                PC++;
                ops = codeTableManager.codeTableFD.getInstructionCode(currentInstruction);


        }

        processMicroOps(ops);
    }

    public void processMicroOps(MicroOp[] ops) {
        if (ops.length > 0 && ops[0] != MicroOp.TODO) {
            //printNewOpUse(currentInstruction);
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
        switch (op) {
//            case HALT:
//                if (interruptEnabled == 1)
//                    halt = 1;
//                else {
//                    PC++;
//                }
//                break;
            case NOP:
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
            case FETCH_IY:
                dataBus = IY;
                break;
            case FETCH_IX:
                dataBus = IX;
                break;
            case FETCH_8:
                dataBus = getNextByte();
                break;
            case FETCH_16:
                dataBus = getNextWord();
                break;
            case FETCH_8_ADDRESS:
                addrBus = getNextByte();
                break;
            case FETCH_16_ADDRESS:
                addrBus = getNextWord();
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
            case STORE_BYTE_AT_ADDRESS:
                mem.poke(addrBus, dataBus);
                break;
            case FETCH_pIY_D:
                dataBus = mem.peek(IY + D);
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
            case STORE_IY:
                IY = dataBus;
                break;
            case STORE_IX:
                IX = dataBus;
                break;

            case STORE_p16WORD:
                mem.poke(addrBus, getLowByte(dataBus));
                mem.poke(addrBus + 1, getHighByte(dataBus));
                break;
            case STORE_pIY_D:
                mem.poke(IY + D, dataBus);
                break;
            case INC_8:
                dataBus = dataBus + 1;
                if (dataBus > 0xff)
                    dataBus = 0;

                handleZeroFlag(dataBus & 0xff);
                unsetFlag(FLAG_ADDSUB);

                if ((dataBus & 0xF) + 1 > 0xF)
                    setFlag(FLAG_HALFCARRY);
                else
                    unsetFlag(FLAG_HALFCARRY);

                break;
            case INC_16:
                dataBus = dataBus + 1;
                if (dataBus > 0xffff)
                    dataBus = 0;
                break;
            case DEC_8:
                dataBus = dataBus - 1;
                if (dataBus < 0)
                    dataBus = 0xff;

                handleZeroFlag(dataBus);
                setFlag(FLAG_ADDSUB);
                if ((dataBus & 0xF) - 1 < 0)
                    setFlag(FLAG_HALFCARRY);
                else
                    unsetFlag(FLAG_HALFCARRY);

                break;
            case DEC_16:
                dataBus = dataBus - 1;
                if (dataBus < 0)
                    dataBus = 0xffff;
                break;
            case ADD_HL:
                dataBus = getHL() + dataBus;

                if (dataBus > 0xffff)
                    setFlag(FLAG_CARRY);
                else
                    unsetFlag(FLAG_CARRY);

                unsetFlag(FLAG_ADDSUB);

                //if ((((getHL() & 0xFFF) + (ac2.val & 0xFFF)) & 0x1000) > 0)
                if ((((getHL() & 0xFFF) + (dataBus & 0xFFF)) & 0x1000) > 0)
                    setFlag(FLAG_HALFCARRY);
                else
                    unsetFlag(FLAG_HALFCARRY);

                break;
            case ADD:
                int wrk = A + dataBus;

                if (wrk > 0xff)
                    setFlag(FLAG_CARRY);
                else
                    unsetFlag(FLAG_CARRY);
                handleZeroFlag(wrk & 0xff);

                unsetFlag(FLAG_ADDSUB);

                if (((A & 0xF) + (dataBus & 0xF)) > 0xF)
                    setFlag(FLAG_HALFCARRY);
                else
                    unsetFlag(FLAG_HALFCARRY);

                A = wrk & 0xff;

                break;
            case ADC:
                wrk = A + dataBus + (testFlag(FLAG_CARRY) ? 1 : 0);

                handleZeroFlag(wrk & 0xff);

                if (wrk > 0xff)
                    setFlag(FLAG_CARRY);
                else
                    unsetFlag(FLAG_CARRY);

                unsetFlag(FLAG_ADDSUB);

                if (((A ^ dataBus ^ wrk) & 0x10) > 0)
                    setFlag(FLAG_HALFCARRY);
                else
                    unsetFlag(FLAG_HALFCARRY);

                A = wrk & 0xff;

                break;
            case SUB:
                wrk = A - dataBus;

                if (dataBus > A)
                    setFlag(FLAG_CARRY);
                else
                    unsetFlag(FLAG_CARRY);

                handleZeroFlag(wrk & 0xff);
                setFlag(FLAG_ADDSUB);

                if (((A ^ dataBus ^ wrk) & 0x10) > 0)
                    setFlag(FLAG_HALFCARRY);
                else
                    unsetFlag(FLAG_HALFCARRY);

                A = wrk & 0xff;

                break;
            case SBC:
                wrk = A - ((dataBus & 0xff) + (testFlag(FLAG_CARRY) ? 1 : 0));

                if ((wrk & 0xFF) > 0) unsetFlag(FLAG_ZERO);
                else setFlag(FLAG_ZERO);
                if ((wrk & 0x100) > 0) setFlag(FLAG_CARRY);
                else unsetFlag(FLAG_CARRY);
                if (((A ^ dataBus ^ wrk) & 0x10) != 0) setFlag(FLAG_HALFCARRY);
                else unsetFlag(FLAG_HALFCARRY);

                setFlag(FLAG_ADDSUB);

                A = wrk & 0xff;
                break;
            case SBC_HL:
                // TODO: this was invented, check it properly
                wrk = getHL() - ((dataBus & 0xffff) + (testFlag(FLAG_CARRY) ? 1 : 0));
                if ((wrk & 0xffff) == 0) setFlag(FLAG_ZERO);
                else unsetFlag(FLAG_ZERO);
                setHL(wrk);
                break;
            case AND:
                wrk = A & dataBus;
                A = wrk;

                handleZeroFlag(wrk);
                unsetFlag(FLAG_ADDSUB);
                unsetFlag(FLAG_CARRY);
                setFlag(FLAG_HALFCARRY);
//                if (displayInstruction)
//                    System.out.println("and val:" + Utils.toHex2(dataBus));
                break;
            case XOR:
                wrk = A ^ dataBus;
                handleZeroFlag(wrk & 0xff);
                unsetFlag(FLAG_ADDSUB);
                unsetFlag(FLAG_CARRY);
                unsetFlag(FLAG_HALFCARRY);
                A = wrk & 0xff;
                break;
            case OR:
                wrk = A | dataBus;
                handleZeroFlag(wrk & 0xff);
                unsetFlag(FLAG_ADDSUB);
                unsetFlag(FLAG_CARRY);
                unsetFlag(FLAG_HALFCARRY);
                A = wrk & 0xff;
                break;
            case CP:
                wrk = A - dataBus;

                handleZeroFlag(wrk & 0xff);

                setFlag(FLAG_ADDSUB);
                if (A < dataBus)
                    setFlag(FLAG_CARRY);
                else
                    unsetFlag(FLAG_CARRY);

                if ((A & 0xF) < (dataBus & 0xF))
                    setFlag(FLAG_HALFCARRY);
                else
                    unsetFlag(FLAG_HALFCARRY);

                break;
//            case CPL:
//                wrk = A ^ 0xFF;
//                setFlag(FLAG_ADDSUB);
//                setFlag(FLAG_HALFCARRY);
//                A = wrk & 0xff;
//                break;
            case RLCA: // Rotate left with carry for A
                int carryOut = ((A & 0x80) > 0) ? 1 : 0;

                if (carryOut == 1) setFlag(FLAG_CARRY);
                else unsetFlag(FLAG_CARRY);

                dataBus = (A << 1) + carryOut;
                unsetFlag(FLAG_ZERO);
                unsetFlag(FLAG_ADDSUB);
                unsetFlag(FLAG_HALFCARRY);
                A = dataBus & 0xff;

                break;
            case RRCA:
                carryOut = ((A & 0x01) > 0) ? 1 : 0;
                if (carryOut == 1) setFlag(FLAG_CARRY);
                else unsetFlag(FLAG_CARRY);
                dataBus = (A >> 1) + (carryOut << 7);
                unsetFlag(FLAG_ZERO);
                unsetFlag(FLAG_ADDSUB);
                unsetFlag(FLAG_HALFCARRY);
                A = dataBus;
                break;
            case RL:
                dataBus = dataBus << 1;
                if (testFlag(FLAG_CARRY))
                    dataBus |= 1;
                if (dataBus > 0xff)
                    setFlag(FLAG_CARRY);
                else
                    unsetFlag(FLAG_CARRY);
                dataBus = dataBus & 0xff;

                unsetFlag(FLAG_ZERO);
                unsetFlag(FLAG_ADDSUB);
                unsetFlag(FLAG_HALFCARRY);

                break;
            case RRA:

                int carryIn = testFlag(FLAG_CARRY) ? 1 : 0;
                carryOut = ((A & 0x01) > 0) ? 1 : 0;

                A = (A >> 1) + (carryIn << 7);

                if (carryOut == 1)
                    setFlag(FLAG_CARRY);
                else
                    unsetFlag(FLAG_CARRY);

                unsetFlag(FLAG_ADDSUB);
                unsetFlag(FLAG_HALFCARRY);
                unsetFlag(FLAG_ZERO);

                break;
            case JRNZ:
                if (!testFlag(FLAG_ZERO)) {
                    jumpRelative(dataBus);
                }
                break;
            case JRZ:
                if (testFlag(FLAG_ZERO)) {
                    jumpRelative(dataBus);
                }
                break;
            case JRNC:
                if (!testFlag(FLAG_CARRY)) {
                    jumpRelative(dataBus);
                }
                break;
            case JRC:
                if (testFlag(FLAG_CARRY)) {
                    jumpRelative(dataBus);
                }
                break;
            case JR:
                jumpRelative(dataBus);

                break;
            case JPZ:
                if (testFlag(FLAG_ZERO)) {
                    PC = addrBus;
                }
                break;
            case JPNZ:
                if (!testFlag(FLAG_ZERO)) {
                    PC = addrBus;
                }
                break;
            case JPNC:
                if (!testFlag(FLAG_CARRY)) {
                    PC = addrBus;
                }
                break;
            case JPC:
                if (testFlag(FLAG_CARRY)) {
                    PC = addrBus;
                }
                break;
            case JP:
                PC = addrBus;
                break;
            case RET:
                wrk = popW();
                PC = wrk;
                break;
            case RETI:
                wrk = popW();
                PC = wrk;
                enableInterrupts();
                break;
            case RETZ:
                if (testFlag(FLAG_ZERO)) {
                    wrk = popW();
                    PC = wrk;
                }
                break;
            case RETNZ:
                if (!testFlag(FLAG_ZERO)) {
                    wrk = popW();
                    PC = wrk;
                }
                break;
            case RETNC:
                if (!testFlag(FLAG_CARRY)) {
                    PC = popW();
                }
                break;
            case RETC:
                if (testFlag(FLAG_CARRY)) {
                    PC = popW();
                }
                break;
            case OUT:
                // TODO the port stuff needs to be implemented
                break;
            case IN:
                // TODO the port stuff needs to be implemented
                dataBus = 0; // DUMMY
                break;
            case DAA: // Decimal Adjust Accumulator to get a correct BCD representation after an arithmetic instruction

                int correction = 0;
                boolean flagN = testFlag(FLAG_ADDSUB);
                boolean flagH = testFlag(FLAG_HALFCARRY);
                boolean flagC = testFlag(FLAG_CARRY);

                if (flagH || (!flagN && (A & 0xF) > 9))
                    correction = 6;

                if (flagC || (!flagN && A > 0x99)) {
                    correction |= 0x60;
                    setFlag(FLAG_CARRY);
                }

                dataBus = A;
                dataBus += flagN ? -correction : correction;
                dataBus &= 0xFF;

                unsetFlag(FLAG_HALFCARRY);

                handleZeroFlag(dataBus);

                A = dataBus;

                break;
            case CPL: // ComPLement accumulator (A = ~A).
                dataBus = A ^ 0xFF;
                setFlag(FLAG_ADDSUB);
                setFlag(FLAG_HALFCARRY);
                A = dataBus & 0xff;
                break;
            case SCF: // Set carry flag
                setFlag(FLAG_CARRY);
                unsetFlag(FLAG_ADDSUB);
                unsetFlag(FLAG_HALFCARRY);
                break;
            case CCF: // Clear carry flag
                unsetFlag(FLAG_ADDSUB);
                unsetFlag(FLAG_HALFCARRY);
                if ((FL & 0x10) > 0)
                    unsetFlag(FLAG_CARRY);
                else
                    setFlag(FLAG_CARRY);
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
                if (!testFlag(FLAG_ZERO)) {
                    call(addrBus);
                }
                break;
            case CALLZ:
                if (testFlag(FLAG_ZERO)) {
                    call(addrBus);
                }
                break;
            case CALLC:
                if (testFlag(FLAG_CARRY)) {
                    call(addrBus);
                }
                break;
            case CALLNC:
                if (!testFlag(FLAG_CARRY)) {
                    call(addrBus);
                }
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
            case LDZPGA: // load zero page from A
                mem.poke(0xff00 + (dataBus & 0xff), A & 0xff);
                // mem.RAM[0xff00+(ac1.val&0xff)] = A&0xff;
                break;
            case FETCH_ZPG: // load zero page from A
                dataBus = mem.peek(0xff00 + (dataBus & 0xff));
                // mem.RAM[0xff00+(ac1.val&0xff)] = A&0xff;
                break;
            case ADDSPNN:

                wrk = SP + convertSignedByte(dataBus & 0xff);

                if (((SP ^ convertSignedByte(dataBus & 0xff) ^ wrk) & 0x10) > 0)
                    setFlag(FLAG_HALFCARRY);
                else
                    unsetFlag(FLAG_HALFCARRY);

                if (((SP ^ convertSignedByte(dataBus & 0xff) ^ wrk) & 0x100) > 0)
                    setFlag(FLAG_CARRY);
                else
                    unsetFlag(FLAG_CARRY);

                unsetFlag(FLAG_ZERO);
                unsetFlag(FLAG_ADDSUB);

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

                unsetFlag(FLAG_ADDSUB);
                unsetFlag(FLAG_ZERO);

                if (((SP ^ signedByte ^ wrk) & 0x100) == 0x100)
                    setFlag(FLAG_CARRY);
                else
                    unsetFlag(FLAG_CARRY);

                if (((SP ^ signedByte ^ wrk) & 0x10) == 0x10)
                    setFlag(FLAG_HALFCARRY);
                else
                    unsetFlag(FLAG_HALFCARRY);

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
            case LDI:
                ed_ldi();
                break;
            case LDIR:
                do {
                    ed_ldi();
                } while (!testFlag(FLAG_PARITY_OVERFLOW));
                break;
            case LDDR:
                do {
                    mem.poke(getDE(), mem.peek(getHL()));
                    setDE(getDE() - 1);
                    setHL(getHL() - 1);
                    setBC(getBC() - 1);
                    System.out.println("LDDR " + getBC());
                } while (getBC() != 0);
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
            case PREFIX_CB:
                //CPUPrefixInstructions.processPrefixCommand(this, dataBus);
                break;
            case PREFIX_ED: {
                int subInstruction = mem.peek(PC++);
                MicroOp[] ops = codeTableManager.codeTableED.getInstructionCode(subInstruction);

                if (ops.length > 0 && ops[0] != MicroOp.TODO) {
                    //printNewOpUse(currentInstruction);
                    for (MicroOp _op : ops) {
                        doMicroOp(_op);
                    }
                }
            }
            break;
            default:
                System.out.println("Unsupported micro op: " + op.name());
        }
    }

    private void ed_ldi() {
        // Transfers a byte of data from the memory location pointed to by HL to the memory
        // location pointed to by DE. Then HL and DE are incremented and BC is decremented.
        // p/v is reset if BC becomes zero and set otherwise.
        int data = mem.peek(getHL());
        mem.poke(getDE(), data);
        incHL();
        incDE();
        decBC();
        if (getBC() == 0) unsetFlag(FLAG_PARITY_OVERFLOW);
        else setFlag(FLAG_PARITY_OVERFLOW);
    }

    private void ed_cpi() {
    }

    private void ed_ini() {
    }

    private void ed_outi() {
    }

    private void ed_ldd() {
    }

    private void ed_cpd() {
    }

    private void ed_ind() {
    }

    private void ed_outd() {
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

    private void setInterruptMode(int i) {
        // TODO: finish this
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

        int tc = convertSignedByte(val); // & 0xff);

        int move = val;
        if (move > 127) move = -((~move + 1) & 0xFF);

        if (tc != move) {
            for (int i = 0; i < 1000; i++) {
                System.out.println("FUCK");
            }
        }

//        if (displayInstruction)
//            System.out.println("Jump relative by " + tc + " to " + Utils.toHex4(PC + tc));

        PC = (PC + move) & 0xffff;

    }

    public int convertSignedByte(int val) {
        if ((val & 0b1000_0000) > 0) {
            return -1 - ((~val) & 0xff);
        }
        return val;
    }

    public int getAF() {
        return combineBytes(A, FL & 0xF0);
    }

    public void setAF(int val) {
        this.A = getHighByte(val);
        this.FL = getLowByte(val) & 0xF0;
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
            setFlag(FLAG_ZERO);
        else
            unsetFlag(FLAG_ZERO);
    }

    // Get word at PC and move PC on.
    public int getNextWord() {
        int oprnd = mem.peek(PC++) & 0xff;
        oprnd = (oprnd) + ((mem.peek(PC++) & 0xff) << 8);
        return oprnd;
    }

    // Get word at PC and move PC on.
    public int getNextByte() {
        return mem.peek(PC++) & 0xff;
    }

    public void setFlag(int flag) {
        FL |= flag;
    }

    public void unsetFlag(int flag) {
        FL &= ~(flag);
    }

    public boolean testFlag(int flag) {
        return (FL & flag) > 0;
    }

    // STACK
    public void pushW(int val) {
        mem.poke((--SP) & 0xffff, getHighByte(val));
        mem.poke((--SP) & 0xffff, getLowByte(val));
    }

    public void jumpToInterrupt(int addr) {
        pushW(PC);
        PC = addr;
    }

    public void call(int addr) {
        pushW(PC);
        PC = addr;
    }

    public void ret() {
        PC = popW();
    }
}
