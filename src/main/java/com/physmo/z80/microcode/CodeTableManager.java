package com.physmo.z80.microcode;

import java.util.ArrayList;
import java.util.List;

public class CodeTableManager {
    public CodeTable codeTableMain;
    public CodeTable codeTableED; // Misc Instructions - prefix ED
    public CodeTable codeTableFD; // IY Instructions (FD)
    public CodeTable codeTableDD; // IX Instructions (DD)
    public CodeTable codeTableFDCB; // IY Bit Instructions (FDCB)
    public CodeTable codeTableCB; // Bit instructions

    public CodeTableManager() {
        codeTableMain = new CodeTable("Main");
        codeTableED = new CodeTable("ED");
        codeTableFD = new CodeTable("FD");
        codeTableDD = new CodeTable("DD");
        codeTableFDCB = new CodeTable("FDCB");
        codeTableCB = new CodeTable("CB");

        initCodeTableMain();
        initCodeTableED();
        initCodeTableFD();
        initCodeTableFDCB();
        initCodeTableCB();
        initCodeTableDD();
    }

    List<MicroOp> cbFetch = List.of(MicroOp.FETCH_B, MicroOp.FETCH_C, MicroOp.FETCH_D, MicroOp.FETCH_E, MicroOp.FETCH_H, MicroOp.FETCH_L, MicroOp.FETCH_pHL, MicroOp.FETCH_A);
    List<MicroOp> cbStore = List.of(MicroOp.STORE_B, MicroOp.STORE_C, MicroOp.STORE_D, MicroOp.STORE_E, MicroOp.STORE_H, MicroOp.STORE_L, MicroOp.STORE_pHL, MicroOp.STORE_A);
    List<String> cbNames = List.of("B", "C", "D", "E", "H", "L", "(hl)", "A");

    // Addressing wildcard is @
    public void createCBInstructions(int startId, MicroOp operation, String name) {
        for (int i = 0; i < 8; i++) {
            String newName = name.replace("@", cbNames.get(i));
            codeTableCB.define(startId + i, newName, cbFetch.get(i), operation, cbStore.get(i));
        }
    }

    //List<MicroOp> fdcbFetch = List.of(MicroOp.FETCH_B, MicroOp.FETCH_C, MicroOp.FETCH_D, MicroOp.FETCH_E, MicroOp.FETCH_H, MicroOp.FETCH_L, MicroOp.FETCH_pHL, MicroOp.FETCH_A);
    List<MicroOp> fdcbStore = List.of(MicroOp.STORE_B, MicroOp.STORE_C, MicroOp.STORE_D, MicroOp.STORE_E, MicroOp.STORE_H, MicroOp.STORE_L, MicroOp.STORE_pIY_D, MicroOp.STORE_A);
    List<String> fdcbNames = List.of("B", "C", "D", "E", "H", "L", "", "A");

    // Addressing wildcard is @
    public void createFDCBInstructions(int startId, MicroOp operation, String name) {
        for (int i = 0; i < 8; i++) {
            String newName = name.replace("@", fdcbNames.get(i));
            codeTableFDCB.define(startId + i, newName, MicroOp.FETCH_pIY_D, operation, fdcbStore.get(i), MicroOp.STORE_pIY_D);
        }
    }

    // Variation with no store for BIT commands
    public void createFDCBInstructionsBit(int startId, MicroOp operation, String name) {
        for (int i = 0; i < 8; i++) {
            codeTableFDCB.define(startId + i, name, MicroOp.FETCH_pIY_D, operation);
        }
    }

    public void initCodeTableCB() {

        createCBInstructions(0x00, MicroOp.RLC, "rlc @");
        createCBInstructions(0x08, MicroOp.RRC, "rrc @");
        createCBInstructions(0x10, MicroOp.RL, "rl @");
        createCBInstructions(0x18, MicroOp.RR, "rr @");
        createCBInstructions(0x20, MicroOp.SLA, "SLA @");
        createCBInstructions(0x28, MicroOp.SRA, "SRA @");
        createCBInstructions(0x30, MicroOp.SLL, "SLL @");
        createCBInstructions(0x38, MicroOp.SRL, "SRL @");
        createCBInstructions(0x40, MicroOp.BIT0, "BIT 0,@");
        createCBInstructions(0x48, MicroOp.BIT1, "BIT 1,@");
        createCBInstructions(0x50, MicroOp.BIT2, "BIT 2,@");
        createCBInstructions(0x58, MicroOp.BIT3, "BIT 3,@");
        createCBInstructions(0x60, MicroOp.BIT4, "BIT 4,@");
        createCBInstructions(0x68, MicroOp.BIT5, "BIT 5,@");
        createCBInstructions(0x70, MicroOp.BIT6, "BIT 6,@");
        createCBInstructions(0x78, MicroOp.BIT7, "BIT 7,@");
        createCBInstructions(0x80, MicroOp.RES0, "RES 0,@");
        createCBInstructions(0x88, MicroOp.RES1, "RES 1,@");
        createCBInstructions(0x90, MicroOp.RES2, "RES 2,@");
        createCBInstructions(0x98, MicroOp.RES3, "RES 3,@");
        createCBInstructions(0xA0, MicroOp.RES4, "RES 4,@");
        createCBInstructions(0xA8, MicroOp.RES5, "RES 5,@");
        createCBInstructions(0xB0, MicroOp.RES6, "RES 6,@");
        createCBInstructions(0xB8, MicroOp.RES7, "RES 7,@");
        createCBInstructions(0xC0, MicroOp.SET0, "SET 0,@");
        createCBInstructions(0xC8, MicroOp.SET1, "SET 1,@");
        createCBInstructions(0xD0, MicroOp.SET2, "SET 2,@");
        createCBInstructions(0xD8, MicroOp.SET3, "SET 3,@");
        createCBInstructions(0xE0, MicroOp.SET4, "SET 4,@");
        createCBInstructions(0xE8, MicroOp.SET5, "SET 5,@");
        createCBInstructions(0xF0, MicroOp.SET6, "SET 6,@");
        createCBInstructions(0xF8, MicroOp.SET7, "SET 7,@");


    }

    public void initCodeTableFDCB() {
        createFDCBInstructions(0x00, MicroOp.RLC, "RLC (iy+d),@");
        createFDCBInstructions(0x08, MicroOp.RRC, "RRC (iy+d),@");

        createFDCBInstructions(0x10, MicroOp.RL, "RL (iy+d),@");
        createFDCBInstructions(0x18, MicroOp.RR, "RR (iy+d),@");

        createFDCBInstructions(0x20, MicroOp.SLA, "SLA (iy+d),@");
        createFDCBInstructions(0x28, MicroOp.SRA, "SRA (iy+d),@");

        createFDCBInstructions(0x30, MicroOp.SLL, "SLL (iy+d),@");
        createFDCBInstructions(0x38, MicroOp.SRL, "SRL (iy+d),@");

        createFDCBInstructionsBit(0x40, MicroOp.BIT0, "BIT 0,(iy+d)");
        createFDCBInstructionsBit(0x48, MicroOp.BIT1, "BIT 1,(iy+d)");
        createFDCBInstructionsBit(0x50, MicroOp.BIT2, "BIT 2,(iy+d)");
        createFDCBInstructionsBit(0x58, MicroOp.BIT3, "BIT 3,(iy+d)");
        createFDCBInstructionsBit(0x60, MicroOp.BIT4, "BIT 4,(iy+d)");
        createFDCBInstructionsBit(0x68, MicroOp.BIT5, "BIT 5,(iy+d)");
        createFDCBInstructionsBit(0x70, MicroOp.BIT6, "BIT 6,(iy+d)");
        createFDCBInstructionsBit(0x78, MicroOp.BIT7, "BIT 7,(iy+d)");

        createFDCBInstructions(0x80, MicroOp.RES0, "RES 0,(iy+d),@");
        createFDCBInstructions(0x88, MicroOp.RES1, "RES 1,(iy+d),@");
        createFDCBInstructions(0x90, MicroOp.RES2, "RES 2,(iy+d),@");
        createFDCBInstructions(0x98, MicroOp.RES3, "RES 3,(iy+d),@");
        createFDCBInstructions(0xA0, MicroOp.RES4, "RES 4,(iy+d),@");
        createFDCBInstructions(0xA8, MicroOp.RES5, "RES 5,(iy+d),@");
        createFDCBInstructions(0xB0, MicroOp.RES6, "RES 6,(iy+d),@");
        createFDCBInstructions(0xB8, MicroOp.RES7, "RES 7,(iy+d),@");

        createFDCBInstructions(0xC0, MicroOp.SET0, "SET 0,(iy+d),@");
        createFDCBInstructions(0xC8, MicroOp.SET1, "SET 1,(iy+d),@");
        createFDCBInstructions(0xD0, MicroOp.SET2, "SET 2,(iy+d),@");
        createFDCBInstructions(0xD8, MicroOp.SET3, "SET 3,(iy+d),@");
        createFDCBInstructions(0xE0, MicroOp.SET4, "SET 4,(iy+d),@");
        createFDCBInstructions(0xE8, MicroOp.SET5, "SET 5,(iy+d),@");
        createFDCBInstructions(0xF0, MicroOp.SET6, "SET 6,(iy+d),@");
        createFDCBInstructions(0xF8, MicroOp.SET7, "SET 7,(iy+d),@");

    }

    // IY Instructions (FD)
    public void initCodeTableFD() {


        codeTableFD.define(0x09, "add iy,bc", MicroOp.FETCH_BC, MicroOp.ADD_IY, MicroOp.STORE_IY);
        codeTableFD.define(0x19, "add iy,de", MicroOp.FETCH_DE, MicroOp.ADD_IY, MicroOp.STORE_IY);

        codeTableFD.define(0x21, "ld iy,nn", MicroOp.FETCH_16, MicroOp.STORE_IY);
        codeTableFD.define(0x23, "inc iy", MicroOp.FETCH_IY, MicroOp.INC_16, MicroOp.STORE_IY);
        codeTableFD.define(0x2A, "ld iy,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_IY);
        codeTableFD.define(0x34, "inc (iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.INC_8, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x35, "dec (iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.DEC_8, MicroOp.STORE_pIY_D);

        codeTableFD.define(0x36, "ld (iy+d),n", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_8, MicroOp.STORE_pIY_D);

        codeTableFD.define(0x46, "ld b,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_B);

        codeTableFD.define(0x4E, "ld c,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_C);
        codeTableFD.define(0x56, "ld d,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_D);
        codeTableFD.define(0x5E, "ld e,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_E);

        codeTableFD.define(0x66, "ld h,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_H);
        codeTableFD.define(0x6E, "ld l,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_L);

        codeTableFD.define(0x70, "ld (iy+d),b", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_B, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x71, "ld (iy+d),c", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_C, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x72, "ld (iy+d),d", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_D, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x73, "ld (iy+d),e", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_E, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x74, "ld (iy+d),h", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_H, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x75, "ld (iy+d),l", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_L, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x77, "ld (iy+d),a", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_A, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x7B, "ld a,e", MicroOp.FETCH_E, MicroOp.STORE_A);

        codeTableFD.define(0x7E, "ld a,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_A);

        codeTableFD.define(0x86, "add a,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.ADD);

        codeTableFD.define(0x96, "sub (iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.SUB);


        codeTableFD.define(0xE1, "pop iy", MicroOp.POPW, MicroOp.STORE_IY);
        codeTableFD.define(0xE5, "push iy", MicroOp.FETCH_IY, MicroOp.PUSHW);

        codeTableFD.define(0xA6, "AND (iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.AND);


        codeTableFD.define(0xBE, "cp (iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.CP);

        codeTableFD.define(0xCB, "IY bit", MicroOp.PREFIX_FD_CB);
    }

    // IX Instructions (DD)
    public void initCodeTableDD() {


        codeTableDD.define(0x09, "add ix,bc", MicroOp.FETCH_BC, MicroOp.ADD_IX, MicroOp.STORE_IX);
        codeTableDD.define(0x19, "add ix,de", MicroOp.FETCH_DE, MicroOp.ADD_IX, MicroOp.STORE_IX);
        codeTableDD.define(0x21, "ld ix,nn", MicroOp.FETCH_16, MicroOp.STORE_IX);
        codeTableDD.define(0x23, "inc ix", MicroOp.FETCH_IX, MicroOp.INC_16, MicroOp.STORE_IX);

        codeTableDD.define(0x26, "ld ixh,n", MicroOp.FETCH_8, MicroOp.STORE_IX_H);

        codeTableDD.define(0x2A, "ld ix,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_IX);

        codeTableDD.define(0x34, "inc (ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.INC_8, MicroOp.STORE_pIX_D);


        codeTableDD.define(0x35, "dec (ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.DEC_8, MicroOp.STORE_pIX_D);


        codeTableDD.define(0x36, "ld (ix+d),n", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_8, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x46, "ld b,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_B);
        codeTableDD.define(0x4E, "ld c,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_C);
        codeTableDD.define(0x56, "ld d,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_D);
        codeTableDD.define(0x5E, "ld e,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_E);
        codeTableDD.define(0x6E, "ld l,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_L);
        codeTableDD.define(0x6F, "ld ixl,a", MicroOp.FETCH_A, MicroOp.STORE_IX_L);

        codeTableDD.define(0x66, "ld h,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_H);

        codeTableDD.define(0x70, "ld (ix+d),b", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_B, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x71, "ld (ix+d),c", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_C, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x72, "ld (ix+d),d", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_D, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x73, "ld (ix+d),e", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_E, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x74, "ld (ix+d),h", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_H, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x75, "ld (ix+d),l", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_L, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x77, "ld (ix+d),a", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_A, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x7B, "ld a,e", MicroOp.FETCH_E, MicroOp.STORE_A);

        codeTableDD.define(0x7E, "ld a,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_A);

        codeTableDD.define(0x86, "add a,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.ADD);

        codeTableDD.define(0x96, "sub (ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.SUB);


        codeTableDD.define(0xE1, "pop ix", MicroOp.POPW, MicroOp.STORE_IX);
        codeTableDD.define(0xE5, "push ix", MicroOp.FETCH_IX, MicroOp.PUSHW);

        codeTableDD.define(0xE9, "jp (ix)", MicroOp.FETCH_IX, MicroOp.STORE_PC);

        codeTableDD.define(0xBE, "cp (ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.CP);

        codeTableDD.define(0xCB, "IX bit", MicroOp.NOP);
    }

    public void initCodeTableED() {
        codeTableED.define(0x42, "sbc hl,bc", MicroOp.FETCH_BC, MicroOp.SBC_HL);
        codeTableED.define(0x43, "ld (nn),bc", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_BC, MicroOp.STORE_WORD_AT_ADDRESS);

        codeTableED.define(0x44, "tst h", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_H); // TODO

        codeTableED.define(0x47, "LD i,a", MicroOp.FETCH_A, MicroOp.STORE_I);


        codeTableED.define(0x4B, "ld bc,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_BC);

        codeTableED.define(0x4F, "LD r,a", MicroOp.FETCH_A, MicroOp.STORE_R);

        codeTableED.define(0x52, "sbc hl,de", MicroOp.FETCH_DE, MicroOp.SBC_HL);
        codeTableED.define(0x53, "ld (nn),de", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_DE, MicroOp.STORE_WORD_AT_ADDRESS);

        codeTableED.define(0x58, "in e,(c)", MicroOp.FETCH_PORT_C, MicroOp.STORE_E);

        codeTableED.define(0x5B, "ld de,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_DE);

        codeTableED.define(0x62, "sbc hl,hl", MicroOp.FETCH_HL, MicroOp.SBC_HL);
        codeTableED.define(0x6B, "ld hl,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_HL);

        codeTableED.define(0x72, "sbc hl,sp", MicroOp.FETCH_SP, MicroOp.SBC_HL);

        codeTableED.define(0x73, "ld (nn),sp", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_SP, MicroOp.STORE_WORD_AT_ADDRESS);

        codeTableED.define(0x78, "in a,(c)", MicroOp.FETCH_PORT_C, MicroOp.STORE_A);
        codeTableED.define(0x79, "out (c),a", MicroOp.NOP);


        codeTableED.define(0x7B, "ld sp,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_SP);


        codeTableED.define(0xA0, "LDI", MicroOp.LDI);
        codeTableED.define(0xA1, "CPI", MicroOp.CPI);
        codeTableED.define(0xA2, "INI", MicroOp.INI);
        codeTableED.define(0xA3, "OUTI", MicroOp.OUTI);
        codeTableED.define(0xA8, "LDD", MicroOp.LDD);
        codeTableED.define(0xA9, "CPD", MicroOp.CPD);
        codeTableED.define(0xAA, "IND", MicroOp.IND);
        codeTableED.define(0xAB, "OUTD", MicroOp.OUTD);

        codeTableED.define(0xB0, "LDIR", MicroOp.LDIR);
        codeTableED.define(0xB1, "CPIR", MicroOp.LDIR);

        codeTableED.define(0xB8, "LDDR", MicroOp.LDDR);

        codeTableED.define(0x46, "IM 0", MicroOp.IM_0);
        codeTableED.define(0x56, "IM 1", MicroOp.IM_1);
        codeTableED.define(0x5E, "IM 2", MicroOp.IM_2);
    }

    public void initCodeTableMain() {
        codeTableMain.define(0x00, "NOP", MicroOp.NOP);
        codeTableMain.define(0x01, "LD BC,d16", MicroOp.FETCH_16, MicroOp.STORE_BC);
        codeTableMain.define(0x02, "LD (BC),A", MicroOp.FETCH_A, MicroOp.SET_ADDR_FROM_BC, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x03, "INC BC", MicroOp.FETCH_BC, MicroOp.INC_16, MicroOp.STORE_BC);
        codeTableMain.define(0x04, "INC B", MicroOp.FETCH_B, MicroOp.INC_8, MicroOp.STORE_B);
        codeTableMain.define(0x05, "DEC B", MicroOp.FETCH_B, MicroOp.DEC_8, MicroOp.STORE_B);
        codeTableMain.define(0x06, "LD B,d8", MicroOp.FETCH_8, MicroOp.STORE_B);
        codeTableMain.define(0x07, "RLCA", MicroOp.FETCH_A, MicroOp.RLCA, MicroOp.STORE_A); // 8-bit rotation left
        codeTableMain.define(0x08, "EX AF,AF'", MicroOp.EX_AF_AF);
        codeTableMain.define(0x09, "ADD HL,BC", MicroOp.FETCH_BC, MicroOp.ADD_HL, MicroOp.STORE_HL);
        codeTableMain.define(0x0A, "LD A,(BC)", MicroOp.SET_ADDR_FROM_BC, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
        codeTableMain.define(0x0B, "DEC BC", MicroOp.FETCH_BC, MicroOp.DEC_16, MicroOp.STORE_BC);
        codeTableMain.define(0x0C, "INC C", MicroOp.FETCH_C, MicroOp.INC_8, MicroOp.STORE_C);
        codeTableMain.define(0x0D, "DEC C", MicroOp.FETCH_C, MicroOp.DEC_8, MicroOp.STORE_C);
        codeTableMain.define(0x0E, "LD C,d8", MicroOp.FETCH_8, MicroOp.STORE_C);
        codeTableMain.define(0x0F, "RRCA", MicroOp.FETCH_A, MicroOp.RRCA, MicroOp.STORE_A);


        codeTableMain.define(0x10, "DJNZ d", MicroOp.FETCH_8, MicroOp.DJNZ);
        codeTableMain.define(0x11, "LD DE,d16", MicroOp.FETCH_16, MicroOp.STORE_DE);
        codeTableMain.define(0x12, "LD (DE),A", MicroOp.FETCH_A, MicroOp.SET_ADDR_FROM_DE, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x13, "INC DE", MicroOp.FETCH_DE, MicroOp.INC_16, MicroOp.STORE_DE);
        codeTableMain.define(0x14, "INC D", MicroOp.FETCH_D, MicroOp.INC_8, MicroOp.STORE_D);
        codeTableMain.define(0x15, "DEC D", MicroOp.FETCH_D, MicroOp.DEC_8, MicroOp.STORE_D);
        codeTableMain.define(0x16, "LD D,d8", MicroOp.FETCH_8, MicroOp.STORE_D);
        codeTableMain.define(0x17, "RLA", MicroOp.FETCH_A, MicroOp.RLA, MicroOp.STORE_A);
        codeTableMain.define(0x18, "JR r8", MicroOp.FETCH_8, MicroOp.JR);
        codeTableMain.define(0x19, "ADD HL,DE", MicroOp.FETCH_DE, MicroOp.ADD_HL, MicroOp.STORE_HL);
        codeTableMain.define(0x1A, "LD A,(DE)", MicroOp.SET_ADDR_FROM_DE, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
        codeTableMain.define(0x1B, "DEC DE", MicroOp.FETCH_DE, MicroOp.DEC_16, MicroOp.STORE_DE);
        codeTableMain.define(0x1C, "INC E", MicroOp.FETCH_E, MicroOp.INC_8, MicroOp.STORE_E);
        codeTableMain.define(0x1D, "DEC E", MicroOp.FETCH_E, MicroOp.DEC_8, MicroOp.STORE_E);
        codeTableMain.define(0x1E, "LD E,d8", MicroOp.FETCH_8, MicroOp.STORE_E);
        codeTableMain.define(0x1F, "RRA", MicroOp.FETCH_A, MicroOp.RRA, MicroOp.STORE_A);

        codeTableMain.define(0x20, "JR NZ,r8", MicroOp.FETCH_8, MicroOp.JRNZ);
        codeTableMain.define(0x21, "LD HL,d16", MicroOp.FETCH_16, MicroOp.STORE_HL);
        codeTableMain.define(0x22, "ld (nn),hl", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_HL, MicroOp.STORE_WORD_AT_ADDRESS);
        codeTableMain.define(0x23, "INC HL", MicroOp.FETCH_HL, MicroOp.INC_16, MicroOp.STORE_HL);
        codeTableMain.define(0x24, "INC H", MicroOp.FETCH_H, MicroOp.INC_8, MicroOp.STORE_H);
        codeTableMain.define(0x25, "DEC H", MicroOp.FETCH_H, MicroOp.DEC_8, MicroOp.STORE_H);
        codeTableMain.define(0x26, "LD H,d8", MicroOp.FETCH_8, MicroOp.STORE_H);
        codeTableMain.define(0x27, "DAA", MicroOp.DAA);
        codeTableMain.define(0x28, "JR Z,r8", MicroOp.FETCH_8, MicroOp.JRZ);
        codeTableMain.define(0x29, "ADD HL,HL", MicroOp.FETCH_HL, MicroOp.ADD_HL, MicroOp.STORE_HL);
        codeTableMain.define(0x2A, "LD hl,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_HL);
        codeTableMain.define(0x2B, "DEC HL", MicroOp.FETCH_HL, MicroOp.DEC_16, MicroOp.STORE_HL);
        codeTableMain.define(0x2C, "INC L", MicroOp.FETCH_L, MicroOp.INC_8, MicroOp.STORE_L);
        codeTableMain.define(0x2D, "DEC L", MicroOp.FETCH_L, MicroOp.DEC_8, MicroOp.STORE_L);
        codeTableMain.define(0x2E, "LD L,d8", MicroOp.FETCH_8, MicroOp.STORE_L);
        codeTableMain.define(0x2F, "CPL", MicroOp.CPL);

        codeTableMain.define(0x30, "JR NC,r8", MicroOp.FETCH_8, MicroOp.JRNC);
        codeTableMain.define(0x31, "LD SP,d16", MicroOp.FETCH_16, MicroOp.STORE_SP);
        codeTableMain.define(0x32, "LD (nn),a", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_A, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x33, "INC SP", MicroOp.FETCH_SP, MicroOp.INC_16, MicroOp.STORE_SP);
        codeTableMain.define(0x34, "INC (HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.INC_8, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x35, "DEC (HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.DEC_8, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x36, "LD (HL),d8", MicroOp.FETCH_8, MicroOp.SET_ADDR_FROM_HL, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x37, "SCF", MicroOp.SCF);
        codeTableMain.define(0x38, "JR C,r8", MicroOp.FETCH_8, MicroOp.JRC);
        codeTableMain.define(0x39, "ADD HL,SP", MicroOp.FETCH_SP, MicroOp.ADD_HL, MicroOp.STORE_HL);
        codeTableMain.define(0x3A, "LD a,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
        codeTableMain.define(0x3B, "DEC SP", MicroOp.FETCH_SP, MicroOp.DEC_16, MicroOp.STORE_SP);
        codeTableMain.define(0x3C, "INC A", MicroOp.FETCH_A, MicroOp.INC_8, MicroOp.STORE_A);
        codeTableMain.define(0x3D, "DEC A", MicroOp.FETCH_A, MicroOp.DEC_8, MicroOp.STORE_A);
        codeTableMain.define(0x3E, "LD A,d8", MicroOp.FETCH_8, MicroOp.STORE_A);
        codeTableMain.define(0x3F, "CCF", MicroOp.CCF);


        codeTableMain.define(0x40, "LD B,B", MicroOp.FETCH_B, MicroOp.STORE_B);
        codeTableMain.define(0x41, "LD B,C", MicroOp.FETCH_C, MicroOp.STORE_B);
        codeTableMain.define(0x42, "LD B,D", MicroOp.FETCH_D, MicroOp.STORE_B);
        codeTableMain.define(0x43, "LD B,E", MicroOp.FETCH_E, MicroOp.STORE_B);
        codeTableMain.define(0x44, "LD B,H", MicroOp.FETCH_H, MicroOp.STORE_B);
        codeTableMain.define(0x45, "LD B,L", MicroOp.FETCH_L, MicroOp.STORE_B);
        codeTableMain.define(0x46, "LD B,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_B);
        codeTableMain.define(0x47, "LD B,A", MicroOp.FETCH_A, MicroOp.STORE_B);
        codeTableMain.define(0x48, "LD C,B", MicroOp.FETCH_B, MicroOp.STORE_C);
        codeTableMain.define(0x49, "LD C,C", MicroOp.FETCH_C, MicroOp.STORE_C);
        codeTableMain.define(0x4A, "LD C,D", MicroOp.FETCH_D, MicroOp.STORE_C);
        codeTableMain.define(0x4B, "LD C,E", MicroOp.FETCH_E, MicroOp.STORE_C);
        codeTableMain.define(0x4C, "LD C,H", MicroOp.FETCH_H, MicroOp.STORE_C);
        codeTableMain.define(0x4D, "LD C,L", MicroOp.FETCH_L, MicroOp.STORE_C);
        codeTableMain.define(0x4E, "LD C,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_C);
        codeTableMain.define(0x4F, "LD C,A", MicroOp.FETCH_A, MicroOp.STORE_C);

        codeTableMain.define(0x50, "LD D,B", MicroOp.FETCH_B, MicroOp.STORE_D);
        codeTableMain.define(0x51, "LD D,C", MicroOp.FETCH_C, MicroOp.STORE_D);
        codeTableMain.define(0x52, "LD D,D", MicroOp.FETCH_D, MicroOp.STORE_D);
        codeTableMain.define(0x53, "LD D,E", MicroOp.FETCH_E, MicroOp.STORE_D);
        codeTableMain.define(0x54, "LD D,H", MicroOp.FETCH_H, MicroOp.STORE_D);
        codeTableMain.define(0x55, "LD D,L", MicroOp.FETCH_L, MicroOp.STORE_D);
        codeTableMain.define(0x56, "LD D,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_D);
        codeTableMain.define(0x57, "LD D,A", MicroOp.FETCH_A, MicroOp.STORE_D);
        codeTableMain.define(0x58, "LD E,B", MicroOp.FETCH_B, MicroOp.STORE_E);
        codeTableMain.define(0x59, "LD E,C", MicroOp.FETCH_C, MicroOp.STORE_E);
        codeTableMain.define(0x5A, "LD E,D", MicroOp.FETCH_D, MicroOp.STORE_E);
        codeTableMain.define(0x5B, "LD E,E", MicroOp.FETCH_E, MicroOp.STORE_E);
        codeTableMain.define(0x5C, "LD E,H", MicroOp.FETCH_H, MicroOp.STORE_E);
        codeTableMain.define(0x5D, "LD E,L", MicroOp.FETCH_L, MicroOp.STORE_E);
        codeTableMain.define(0x5E, "LD E,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_E);
        codeTableMain.define(0x5F, "LD E,A", MicroOp.FETCH_A, MicroOp.STORE_E);

        codeTableMain.define(0x60, "LD H,B", MicroOp.FETCH_B, MicroOp.STORE_H);
        codeTableMain.define(0x61, "LD H,C", MicroOp.FETCH_C, MicroOp.STORE_H);
        codeTableMain.define(0x62, "LD H,D", MicroOp.FETCH_D, MicroOp.STORE_H);
        codeTableMain.define(0x63, "LD H,E", MicroOp.FETCH_E, MicroOp.STORE_H);
        codeTableMain.define(0x64, "LD H,H", MicroOp.FETCH_H, MicroOp.STORE_H);
        codeTableMain.define(0x65, "LD H,L", MicroOp.FETCH_L, MicroOp.STORE_H);
        codeTableMain.define(0x66, "LD H,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_H);
        codeTableMain.define(0x67, "LD H,A", MicroOp.FETCH_A, MicroOp.STORE_H);
        codeTableMain.define(0x68, "LD L,B", MicroOp.FETCH_B, MicroOp.STORE_L);
        codeTableMain.define(0x69, "LD L,C", MicroOp.FETCH_C, MicroOp.STORE_L);
        codeTableMain.define(0x6A, "LD L,D", MicroOp.FETCH_D, MicroOp.STORE_L);
        codeTableMain.define(0x6B, "LD L,E", MicroOp.FETCH_E, MicroOp.STORE_L);
        codeTableMain.define(0x6C, "LD L,H", MicroOp.FETCH_H, MicroOp.STORE_L);
        codeTableMain.define(0x6D, "LD L,L", MicroOp.FETCH_L, MicroOp.STORE_L);
        codeTableMain.define(0x6E, "LD L,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_L);
        codeTableMain.define(0x6F, "LD L,A", MicroOp.FETCH_A, MicroOp.STORE_L);

        codeTableMain.define(0x70, "LD (HL),B", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_B, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x71, "LD (HL),C", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_C, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x72, "LD (HL),D", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_D, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x73, "LD (HL),E", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_E, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x74, "LD (HL),H", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_H, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x75, "LD (HL),L", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_L, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x76, "HALT", MicroOp.HALT);
        codeTableMain.define(0x77, "LD (HL),A", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_A, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x78, "LD A,B", MicroOp.FETCH_B, MicroOp.STORE_A);
        codeTableMain.define(0x79, "LD A,C", MicroOp.FETCH_C, MicroOp.STORE_A);
        codeTableMain.define(0x7A, "LD A,D", MicroOp.FETCH_D, MicroOp.STORE_A);
        codeTableMain.define(0x7B, "LD A,E", MicroOp.FETCH_E, MicroOp.STORE_A);
        codeTableMain.define(0x7C, "LD A,H", MicroOp.FETCH_H, MicroOp.STORE_A);
        codeTableMain.define(0x7D, "LD A,L", MicroOp.FETCH_L, MicroOp.STORE_A);
        codeTableMain.define(0x7E, "LD A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
        codeTableMain.define(0x7F, "LD A,A", MicroOp.FETCH_A, MicroOp.STORE_A);

        codeTableMain.define(0x80, "ADD A,B", MicroOp.FETCH_B, MicroOp.ADD);
        codeTableMain.define(0x81, "ADD A,C", MicroOp.FETCH_C, MicroOp.ADD);
        codeTableMain.define(0x82, "ADD A,D", MicroOp.FETCH_D, MicroOp.ADD);
        codeTableMain.define(0x83, "ADD A,E", MicroOp.FETCH_E, MicroOp.ADD);
        codeTableMain.define(0x84, "ADD A,H", MicroOp.FETCH_H, MicroOp.ADD);
        codeTableMain.define(0x85, "ADD A,L", MicroOp.FETCH_L, MicroOp.ADD);
        codeTableMain.define(0x86, "ADD A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.ADD);
        codeTableMain.define(0x87, "ADD A,A", MicroOp.FETCH_A, MicroOp.ADD);
        codeTableMain.define(0x88, "ADC A,B", MicroOp.FETCH_B, MicroOp.ADC);
        codeTableMain.define(0x89, "ADC A,C", MicroOp.FETCH_C, MicroOp.ADC);
        codeTableMain.define(0x8A, "ADC A,D", MicroOp.FETCH_D, MicroOp.ADC);
        codeTableMain.define(0x8B, "ADC A,E", MicroOp.FETCH_E, MicroOp.ADC);
        codeTableMain.define(0x8C, "ADC A,H", MicroOp.FETCH_H, MicroOp.ADC);
        codeTableMain.define(0x8D, "ADC A,L", MicroOp.FETCH_L, MicroOp.ADC);
        codeTableMain.define(0x8E, "ADC A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.ADC);
        codeTableMain.define(0x8F, "ADC A,A", MicroOp.FETCH_A, MicroOp.ADC);

        codeTableMain.define(0x90, "SUB A,B", MicroOp.FETCH_B, MicroOp.SUB);
        codeTableMain.define(0x91, "SUB A,C", MicroOp.FETCH_C, MicroOp.SUB);
        codeTableMain.define(0x92, "SUB A,D", MicroOp.FETCH_D, MicroOp.SUB);
        codeTableMain.define(0x93, "SUB A,E", MicroOp.FETCH_E, MicroOp.SUB);
        codeTableMain.define(0x94, "SUB A,H", MicroOp.FETCH_H, MicroOp.SUB);
        codeTableMain.define(0x95, "SUB A,L", MicroOp.FETCH_L, MicroOp.SUB);
        codeTableMain.define(0x96, "SUB A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.SUB);
        codeTableMain.define(0x97, "SUB A,A", MicroOp.FETCH_A, MicroOp.SUB);
        codeTableMain.define(0x98, "SBC A,B", MicroOp.FETCH_B, MicroOp.SBC);
        codeTableMain.define(0x99, "SBC A,C", MicroOp.FETCH_C, MicroOp.SBC);
        codeTableMain.define(0x9A, "SBC A,D", MicroOp.FETCH_D, MicroOp.SBC);
        codeTableMain.define(0x9B, "SBC A,E", MicroOp.FETCH_E, MicroOp.SBC);
        codeTableMain.define(0x9C, "SBC A,H", MicroOp.FETCH_H, MicroOp.SBC);
        codeTableMain.define(0x9D, "SBC A,L", MicroOp.FETCH_L, MicroOp.SBC);
        codeTableMain.define(0x9E, "SBC A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.SBC);
        codeTableMain.define(0x9F, "SBC A,A", MicroOp.FETCH_A, MicroOp.SBC);


        codeTableMain.define(0xA0, "AND A,B", MicroOp.FETCH_B, MicroOp.AND);
        codeTableMain.define(0xA1, "AND A,C", MicroOp.FETCH_C, MicroOp.AND);
        codeTableMain.define(0xA2, "AND A,D", MicroOp.FETCH_D, MicroOp.AND);
        codeTableMain.define(0xA3, "AND A,E", MicroOp.FETCH_E, MicroOp.AND);
        codeTableMain.define(0xA4, "AND A,H", MicroOp.FETCH_H, MicroOp.AND);
        codeTableMain.define(0xA5, "AND A,L", MicroOp.FETCH_L, MicroOp.AND);
        codeTableMain.define(0xA6, "AND A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.AND);
        codeTableMain.define(0xA7, "AND A,A", MicroOp.FETCH_A, MicroOp.AND);
        codeTableMain.define(0xA8, "XOR A,B", MicroOp.FETCH_B, MicroOp.XOR);
        codeTableMain.define(0xA9, "XOR A,C", MicroOp.FETCH_C, MicroOp.XOR);
        codeTableMain.define(0xAA, "XOR A,D", MicroOp.FETCH_D, MicroOp.XOR);
        codeTableMain.define(0xAB, "XOR A,E", MicroOp.FETCH_E, MicroOp.XOR);
        codeTableMain.define(0xAC, "XOR A,H", MicroOp.FETCH_H, MicroOp.XOR);
        codeTableMain.define(0xAD, "XOR A,L", MicroOp.FETCH_L, MicroOp.XOR);
        codeTableMain.define(0xAE, "XOR A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.XOR);
        codeTableMain.define(0xAF, "XOR A,A", MicroOp.FETCH_A, MicroOp.XOR);

        codeTableMain.define(0xB0, "OR A,B", MicroOp.FETCH_B, MicroOp.OR);
        codeTableMain.define(0xB1, "OR A,C", MicroOp.FETCH_C, MicroOp.OR);
        codeTableMain.define(0xB2, "OR A,D", MicroOp.FETCH_D, MicroOp.OR);
        codeTableMain.define(0xB3, "OR A,E", MicroOp.FETCH_E, MicroOp.OR);
        codeTableMain.define(0xB4, "OR A,H", MicroOp.FETCH_H, MicroOp.OR);
        codeTableMain.define(0xB5, "OR A,L", MicroOp.FETCH_L, MicroOp.OR);
        codeTableMain.define(0xB6, "OR A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.OR);
        codeTableMain.define(0xB7, "OR A,A", MicroOp.FETCH_A, MicroOp.OR);
        codeTableMain.define(0xB8, "CP A,B", MicroOp.FETCH_B, MicroOp.CP);
        codeTableMain.define(0xB9, "CP A,C", MicroOp.FETCH_C, MicroOp.CP);
        codeTableMain.define(0xBA, "CP A,D", MicroOp.FETCH_D, MicroOp.CP);
        codeTableMain.define(0xBB, "CP A,E", MicroOp.FETCH_E, MicroOp.CP);
        codeTableMain.define(0xBC, "CP A,H", MicroOp.FETCH_H, MicroOp.CP);
        codeTableMain.define(0xBD, "CP A,L", MicroOp.FETCH_L, MicroOp.CP);
        codeTableMain.define(0xBE, "CP A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.CP);
        codeTableMain.define(0xBF, "CP A,A", MicroOp.FETCH_A, MicroOp.CP);

        // TODO verify below
        codeTableMain.define(0xC0, "RET NZ", MicroOp.RETNZ);
        codeTableMain.define(0xC1, "POP BC", MicroOp.POPW, MicroOp.STORE_BC);
        codeTableMain.define(0xC2, "JP NZ,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.JPNZ);
        codeTableMain.define(0xC3, "JP a16", MicroOp.FETCH_16, MicroOp.STORE_PC);
        codeTableMain.define(0xC4, "CALL NZ,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLNZ);
        codeTableMain.define(0xC5, "PUSH BC", MicroOp.FETCH_BC, MicroOp.PUSHW);
        codeTableMain.define(0xC6, "ADD A,d8", MicroOp.FETCH_8, MicroOp.ADD);
        codeTableMain.define(0xC7, "RST 00H", MicroOp.RST_00H);
        codeTableMain.define(0xC8, "RET Z", MicroOp.RETZ);
        codeTableMain.define(0xC9, "RET", MicroOp.RET);
        codeTableMain.define(0xCA, "JP Z,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.JPZ);
        codeTableMain.define(0xCB, "PREFIX CB", MicroOp.PREFIX_CB);
        codeTableMain.define(0xCC, "CALL Z,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLZ);
        codeTableMain.define(0xCD, "CALL a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALL);
        codeTableMain.define(0xCE, "ADC A,d8", MicroOp.FETCH_8, MicroOp.ADC);
        codeTableMain.define(0xCF, "RST 08H", MicroOp.RST_08H);

        codeTableMain.define(0xD0, "RET NC", MicroOp.RETNC);
        codeTableMain.define(0xD1, "POP DE", MicroOp.POPW, MicroOp.STORE_DE);
        codeTableMain.define(0xD2, "JP NC,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.JPNC);
        codeTableMain.define(0xD3, "OUT d8,A", MicroOp.FETCH_8_ADDRESS, MicroOp.FETCH_A, MicroOp.OUT);
        codeTableMain.define(0xD4, "CALL NC,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLNC);
        codeTableMain.define(0xD5, "PUSH DE", MicroOp.FETCH_DE, MicroOp.PUSHW);
        codeTableMain.define(0xD6, "SUB d8", MicroOp.FETCH_8, MicroOp.SUB);
        codeTableMain.define(0xD7, "RST 10H", MicroOp.RST_10H);
        codeTableMain.define(0xD8, "RET C", MicroOp.RETC);
        codeTableMain.define(0xD9, "EXX", MicroOp.EXX);
        codeTableMain.define(0xDA, "JP C,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.JPC);
        codeTableMain.define(0xDB, "in a,(n)", MicroOp.FETCH_8, MicroOp.IN, MicroOp.STORE_A);
        codeTableMain.define(0xDC, "CALL C,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLC);
        codeTableMain.define(0xDD, "Prefix DD", MicroOp.PREFIX_DD);
        codeTableMain.define(0xDE, "SBC A,d8", MicroOp.FETCH_8, MicroOp.SBC);
        codeTableMain.define(0xDF, "RST 18H", MicroOp.RST_18H);

        codeTableMain.define(0xE0, "RET PO", MicroOp.RET_PO);
        codeTableMain.define(0xE1, "POP HL", MicroOp.POPW, MicroOp.STORE_HL);
        codeTableMain.define(0xE2, "JP PO,NN", MicroOp.FETCH_16_ADDRESS, MicroOp.JP_PO);
        codeTableMain.define(0xE3, "ex (sp),hl", MicroOp.EX_SP_HL);
        codeTableMain.define(0xE4, "call po,nn", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLPO);
        codeTableMain.define(0xE5, "PUSH HL", MicroOp.FETCH_HL, MicroOp.PUSHW);
        codeTableMain.define(0xE6, "AND d8", MicroOp.FETCH_8, MicroOp.AND);
        codeTableMain.define(0xE7, "RST 20H", MicroOp.RST_20H);
        codeTableMain.define(0xE8, "ADD SP,r8", MicroOp.FETCH_8, MicroOp.ADDSPNN);
        codeTableMain.define(0xE9, "JP (HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.JP);
        codeTableMain.define(0xEA, "JP PE,NN", MicroOp.FETCH_16_ADDRESS, MicroOp.JP_PE);
        codeTableMain.define(0xEB, "EX DE, HL", MicroOp.EX_DE_HL);
        codeTableMain.define(0xEC, "call pe,nn", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLPE);
        codeTableMain.define(0xED, "Prefix ED", MicroOp.PREFIX_ED);
        codeTableMain.define(0xEE, "XOR d8", MicroOp.FETCH_8, MicroOp.XOR);
        codeTableMain.define(0xEF, "RST 28H", MicroOp.RST_28H);

        codeTableMain.define(0xF0, "RET P", MicroOp.RET_P);
        codeTableMain.define(0xF1, "POP AF", MicroOp.POPW, MicroOp.STORE_AF);
        codeTableMain.define(0xF2, "JP P,NN", MicroOp.FETCH_16_ADDRESS, MicroOp.JP_P);
        codeTableMain.define(0xF3, "DI", MicroOp.DI);
        codeTableMain.define(0xF4, "call p,nn", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLP);
        codeTableMain.define(0xF5, "PUSH AF", MicroOp.FETCH_AF, MicroOp.PUSHW);
        codeTableMain.define(0xF6, "OR d8", MicroOp.FETCH_8, MicroOp.OR);
        codeTableMain.define(0xF7, "RST 30H", MicroOp.RST_30H);
        codeTableMain.define(0xF8, "LD HL,SP+r8", MicroOp.FETCH_8, MicroOp.LDHLSPN);
        codeTableMain.define(0xF9, "LD SP,HL", MicroOp.FETCH_HL, MicroOp.STORE_SP);
        codeTableMain.define(0xFA, "jp m,nn", MicroOp.FETCH_16_ADDRESS, MicroOp.JP_M);
        codeTableMain.define(0xFB, "EI", MicroOp.EI);
        codeTableMain.define(0xFC, "call m,nn", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLM);
        codeTableMain.define(0xFD, "Prefix FD", MicroOp.PREFIX_FD);
        codeTableMain.define(0xFE, "CP d8", MicroOp.FETCH_8, MicroOp.CP);
        codeTableMain.define(0xFF, "RST 38H", MicroOp.RST_38H);
    }


}
