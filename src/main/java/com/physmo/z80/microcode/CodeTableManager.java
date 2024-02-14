package com.physmo.z80.microcode;

import java.util.List;

public class CodeTableManager {
    public CodeTable codeTableMain;
    public CodeTable codeTableED; // Misc Instructions - prefix ED
    public CodeTable codeTableFD; // IY Instructions (FD)
    public CodeTable codeTableDD; // IX Instructions (DD)
    public CodeTable codeTableFDCB; // IY Bit Instructions (FDCB)
    public CodeTable codeTableDDCB; // IX Bit Instructions (DDCB)
    public CodeTable codeTableCB; // Bit instructions
    List<MicroOp> cbFetch = List.of(MicroOp.FETCH_B, MicroOp.FETCH_C, MicroOp.FETCH_D, MicroOp.FETCH_E, MicroOp.FETCH_H, MicroOp.FETCH_L, MicroOp.FETCH_pHL, MicroOp.FETCH_A);
    List<MicroOp> cbStore = List.of(MicroOp.STORE_B, MicroOp.STORE_C, MicroOp.STORE_D, MicroOp.STORE_E, MicroOp.STORE_H, MicroOp.STORE_L, MicroOp.STORE_pHL, MicroOp.STORE_A);
    List<String> cbNames = List.of("B", "C", "D", "E", "H", "L", "(hl)", "A");
    //List<MicroOp> fdcbFetch = List.of(MicroOp.FETCH_B, MicroOp.FETCH_C, MicroOp.FETCH_D, MicroOp.FETCH_E, MicroOp.FETCH_H, MicroOp.FETCH_L, MicroOp.FETCH_pHL, MicroOp.FETCH_A);
    List<MicroOp> fdcbStore = List.of(MicroOp.STORE_B, MicroOp.STORE_C, MicroOp.STORE_D, MicroOp.STORE_E, MicroOp.STORE_H, MicroOp.STORE_L, MicroOp.STORE_pIY_D, MicroOp.STORE_A);
    List<MicroOp> ddcbStore = List.of(MicroOp.STORE_B, MicroOp.STORE_C, MicroOp.STORE_D, MicroOp.STORE_E, MicroOp.STORE_H, MicroOp.STORE_L, MicroOp.STORE_pIX_D, MicroOp.STORE_A);
    List<String> fdcbNames = List.of("B", "C", "D", "E", "H", "L", "", "A");

    public CodeTableManager() {
        codeTableMain = new CodeTable("Main");
        codeTableED = new CodeTable("ED");
        codeTableFD = new CodeTable("FD");
        codeTableDD = new CodeTable("DD");
        codeTableFDCB = new CodeTable("FDCB");
        codeTableDDCB = new CodeTable("DDCB");
        codeTableCB = new CodeTable("CB");

        initCodeTableMain();
        initCodeTableED();
        initCodeTableFD(); // IY
        initCodeTableFDCB(); // IY Bit
        initCodeTableCB();
        initCodeTableDD(); // IX
        initCodeTableDDCB(); // IX
    }

    // Addressing wildcard is @
    public void createCBInstructions(int startId, MicroOp operation, String name) {
        for (int i = 0; i < 8; i++) {
            String newName = name.replace("@", cbNames.get(i));
            codeTableCB.define(startId + i, newName, cbFetch.get(i), operation, cbStore.get(i));
        }
    }

    public void createCBInstructionsBit(int startId, MicroOp operation, String name) {
        for (int i = 0; i < 8; i++) {
            String newName = name.replace("@", cbNames.get(i));
            codeTableCB.define(startId + i, newName, cbFetch.get(i), operation);
        }
    }

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

    // Addressing wildcard is @
    public void createDDCBInstructions(int startId, MicroOp operation, String name) {
        for (int i = 0; i < 8; i++) {
            String newName = name.replace("@", fdcbNames.get(i));
            codeTableDDCB.define(startId + i, newName, MicroOp.FETCH_pIX_D, operation, ddcbStore.get(i), MicroOp.STORE_pIX_D);
        }
    }

    // Variation with no store for BIT commands
    public void createDDCBInstructionsBit(int startId, MicroOp operation, String name) {
        for (int i = 0; i < 8; i++) {
            codeTableDDCB.define(startId + i, name, MicroOp.FETCH_pIX_D, operation);
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
        createCBInstructionsBit(0x40, MicroOp.BIT0, "BIT 0,@");
        createCBInstructionsBit(0x48, MicroOp.BIT1, "BIT 1,@");
        createCBInstructionsBit(0x50, MicroOp.BIT2, "BIT 2,@");
        createCBInstructionsBit(0x58, MicroOp.BIT3, "BIT 3,@");
        createCBInstructionsBit(0x60, MicroOp.BIT4, "BIT 4,@");
        createCBInstructionsBit(0x68, MicroOp.BIT5, "BIT 5,@");
        createCBInstructionsBit(0x70, MicroOp.BIT6, "BIT 6,@");
        createCBInstructionsBit(0x78, MicroOp.BIT7, "BIT 7,@");
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

    // IY Bit instructions
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

    // IX Bit instructions
    public void initCodeTableDDCB() {
        createDDCBInstructions(0x00, MicroOp.RLC, "RLC (ix+d),@");
        createDDCBInstructions(0x08, MicroOp.RRC, "RRC (ix+d),@");
        createDDCBInstructions(0x10, MicroOp.RL, "RL (ix+d),@");
        createDDCBInstructions(0x18, MicroOp.RR, "RR (ix+d),@");
        createDDCBInstructions(0x20, MicroOp.SLA, "SLA (ix+d),@");
        createDDCBInstructions(0x28, MicroOp.SRA, "SRA (ix+d),@");
        createDDCBInstructions(0x30, MicroOp.SLL, "SLL (ix+d),@");
        createDDCBInstructions(0x38, MicroOp.SRL, "SRL (ix+d),@");

        createDDCBInstructionsBit(0x40, MicroOp.BIT0, "BIT 0,(ix+d)");
        createDDCBInstructionsBit(0x48, MicroOp.BIT1, "BIT 1,(ix+d)");
        createDDCBInstructionsBit(0x50, MicroOp.BIT2, "BIT 2,(ix+d)");
        createDDCBInstructionsBit(0x58, MicroOp.BIT3, "BIT 3,(ix+d)");
        createDDCBInstructionsBit(0x60, MicroOp.BIT4, "BIT 4,(ix+d)");
        createDDCBInstructionsBit(0x68, MicroOp.BIT5, "BIT 5,(ix+d)");
        createDDCBInstructionsBit(0x70, MicroOp.BIT6, "BIT 6,(ix+d)");
        createDDCBInstructionsBit(0x78, MicroOp.BIT7, "BIT 7,(ix+d)");

        createDDCBInstructions(0x80, MicroOp.RES0, "RES 0,(ix+d),@");
        createDDCBInstructions(0x88, MicroOp.RES1, "RES 1,(ix+d),@");
        createDDCBInstructions(0x90, MicroOp.RES2, "RES 2,(ix+d),@");
        createDDCBInstructions(0x98, MicroOp.RES3, "RES 3,(ix+d),@");
        createDDCBInstructions(0xA0, MicroOp.RES4, "RES 4,(ix+d),@");
        createDDCBInstructions(0xA8, MicroOp.RES5, "RES 5,(ix+d),@");
        createDDCBInstructions(0xB0, MicroOp.RES6, "RES 6,(ix+d),@");
        createDDCBInstructions(0xB8, MicroOp.RES7, "RES 7,(ix+d),@");

        createDDCBInstructions(0xC0, MicroOp.SET0, "SET 0,(ix+d),@");
        createDDCBInstructions(0xC8, MicroOp.SET1, "SET 1,(ix+d),@");
        createDDCBInstructions(0xD0, MicroOp.SET2, "SET 2,(ix+d),@");
        createDDCBInstructions(0xD8, MicroOp.SET3, "SET 3,(ix+d),@");
        createDDCBInstructions(0xE0, MicroOp.SET4, "SET 4,(ix+d),@");
        createDDCBInstructions(0xE8, MicroOp.SET5, "SET 5,(ix+d),@");
        createDDCBInstructions(0xF0, MicroOp.SET6, "SET 6,(ix+d),@");
        createDDCBInstructions(0xF8, MicroOp.SET7, "SET 7,(ix+d),@");

    }

    // IY Instructions (FD)
    // Master
    public void initCodeTableFD() {

        codeTableFD.define(0x04, "inc b", MicroOp.FETCH_B, MicroOp.INC_8, MicroOp.STORE_B);
        codeTableFD.define(0x05, "dec b", MicroOp.FETCH_B, MicroOp.DEC_8, MicroOp.STORE_B);
        codeTableFD.define(0x06, "ld b,n", MicroOp.FETCH_8, MicroOp.STORE_B);

        codeTableFD.define(0x09, "add iy,bc", MicroOp.FETCH_BC, MicroOp.ADD_IY, MicroOp.STORE_IY);

        codeTableFD.define(0x0c, "inc c", MicroOp.FETCH_C, MicroOp.INC_8, MicroOp.STORE_C);
        codeTableFD.define(0x0d, "dec c", MicroOp.FETCH_C, MicroOp.DEC_8, MicroOp.STORE_C);
        codeTableFD.define(0x0e, "ld c,n", MicroOp.FETCH_8, MicroOp.STORE_C);

        codeTableFD.define(0x14, "inc d", MicroOp.FETCH_D, MicroOp.INC_8, MicroOp.STORE_D);
        codeTableFD.define(0x15, "dec d", MicroOp.FETCH_D, MicroOp.DEC_8, MicroOp.STORE_D);
        codeTableFD.define(0x16, "ld d,n", MicroOp.FETCH_8, MicroOp.STORE_D);

        codeTableFD.define(0x19, "add iy,de", MicroOp.FETCH_DE, MicroOp.ADD_IY, MicroOp.STORE_IY);

        codeTableFD.define(0x1c, "inc e", MicroOp.FETCH_E, MicroOp.INC_8, MicroOp.STORE_E);
        codeTableFD.define(0x1d, "dec e", MicroOp.FETCH_E, MicroOp.DEC_8, MicroOp.STORE_E);
        codeTableFD.define(0x1e, "ld e,n", MicroOp.FETCH_8, MicroOp.STORE_E);

        codeTableFD.define(0x21, "ld iy,nn", MicroOp.FETCH_16, MicroOp.STORE_IY);
        codeTableFD.define(0x22, "ld (nn),iy", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_IY, MicroOp.STORE_WORD_AT_ADDRESS);
        codeTableFD.define(0x23, "inc iy", MicroOp.FETCH_IY, MicroOp.INC_16, MicroOp.STORE_IY);
        codeTableFD.define(0x24, "inc iyh", MicroOp.FETCH_IY_H, MicroOp.INC_8, MicroOp.STORE_IY_H);
        codeTableFD.define(0x25, "dec iyh", MicroOp.FETCH_IY_H, MicroOp.DEC_8, MicroOp.STORE_IY_H);
        codeTableFD.define(0x26, "ld iyh, n", MicroOp.FETCH_8, MicroOp.STORE_IY_H);

        codeTableFD.define(0x29, "add iy,iy", MicroOp.FETCH_IY, MicroOp.ADD_IY, MicroOp.STORE_IY);
        codeTableFD.define(0x2A, "ld iy,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_IY);
        codeTableFD.define(0x2B, "dec iy", MicroOp.FETCH_IY, MicroOp.DEC_16, MicroOp.STORE_IY);
        codeTableFD.define(0x2C, "inc iyl", MicroOp.FETCH_IY_L, MicroOp.INC_8, MicroOp.STORE_IY_L);
        codeTableFD.define(0x2D, "dec iyl", MicroOp.FETCH_IY_L, MicroOp.DEC_8, MicroOp.STORE_IY_L);
        codeTableFD.define(0x2E, "ld iyl,n", MicroOp.FETCH_8, MicroOp.STORE_IY_L);

        codeTableFD.define(0x34, "inc (iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.INC_8, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x35, "dec (iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.DEC_8, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x36, "ld (iy+d),n", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_8, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x39, "add iy,sp", MicroOp.FETCH_SP, MicroOp.ADD_IY, MicroOp.STORE_IY);

        codeTableFD.define(0x3c, "inc A", MicroOp.FETCH_A, MicroOp.INC_8, MicroOp.STORE_A);
        codeTableFD.define(0x3d, "dec A", MicroOp.FETCH_A, MicroOp.DEC_8, MicroOp.STORE_A);
        codeTableFD.define(0x3e, "ld A,n", MicroOp.FETCH_8, MicroOp.STORE_A);

        codeTableFD.define(0x40, "ld b,b", MicroOp.FETCH_B, MicroOp.STORE_B);
        codeTableFD.define(0x41, "ld b,c", MicroOp.FETCH_C, MicroOp.STORE_B);
        codeTableFD.define(0x42, "ld b,d", MicroOp.FETCH_D, MicroOp.STORE_B);
        codeTableFD.define(0x43, "ld b,e", MicroOp.FETCH_E, MicroOp.STORE_B);
        codeTableFD.define(0x44, "ld b,iyh", MicroOp.FETCH_IY_H, MicroOp.STORE_B);
        codeTableFD.define(0x45, "ld b,iyl", MicroOp.FETCH_IY_L, MicroOp.STORE_B);
        codeTableFD.define(0x46, "ld b,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_B);
        codeTableFD.define(0x47, "ld b,a", MicroOp.FETCH_A, MicroOp.STORE_B);
        codeTableFD.define(0x48, "ld c,b", MicroOp.FETCH_B, MicroOp.STORE_C);
        codeTableFD.define(0x49, "ld c,c", MicroOp.FETCH_C, MicroOp.STORE_C);
        codeTableFD.define(0x4A, "ld c,d", MicroOp.FETCH_D, MicroOp.STORE_C);
        codeTableFD.define(0x4B, "ld c,e", MicroOp.FETCH_E, MicroOp.STORE_C);
        codeTableFD.define(0x4C, "ld c,iyh", MicroOp.FETCH_IY_H, MicroOp.STORE_C);
        codeTableFD.define(0x4D, "ld c,iyl", MicroOp.FETCH_IY_L, MicroOp.STORE_C);
        codeTableFD.define(0x4E, "ld c,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_C);
        codeTableFD.define(0x4F, "ld c,a", MicroOp.FETCH_A, MicroOp.STORE_C);

        codeTableFD.define(0x50, "ld d,b", MicroOp.FETCH_B, MicroOp.STORE_D);
        codeTableFD.define(0x51, "ld d,c", MicroOp.FETCH_C, MicroOp.STORE_D);
        codeTableFD.define(0x52, "ld d,d", MicroOp.FETCH_D, MicroOp.STORE_D);
        codeTableFD.define(0x53, "ld d,e", MicroOp.FETCH_E, MicroOp.STORE_D);
        codeTableFD.define(0x54, "ld d,iyh", MicroOp.FETCH_IY_H, MicroOp.STORE_D);
        codeTableFD.define(0x55, "ld d,iyl", MicroOp.FETCH_IY_L, MicroOp.STORE_D);
        codeTableFD.define(0x56, "ld d,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_D);
        codeTableFD.define(0x57, "ld d,a", MicroOp.FETCH_A, MicroOp.STORE_D);
        codeTableFD.define(0x58, "ld e,b", MicroOp.FETCH_B, MicroOp.STORE_E);
        codeTableFD.define(0x59, "ld e,c", MicroOp.FETCH_C, MicroOp.STORE_E);
        codeTableFD.define(0x5A, "ld e,d", MicroOp.FETCH_D, MicroOp.STORE_E);
        codeTableFD.define(0x5B, "ld e,e", MicroOp.FETCH_E, MicroOp.STORE_E);
        codeTableFD.define(0x5C, "ld e,iyh", MicroOp.FETCH_IY_H, MicroOp.STORE_E);
        codeTableFD.define(0x5D, "ld e,iyl", MicroOp.FETCH_IY_L, MicroOp.STORE_E);
        codeTableFD.define(0x5E, "ld e,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_E);
        codeTableFD.define(0x5F, "ld e,a", MicroOp.FETCH_A, MicroOp.STORE_E);

        codeTableFD.define(0x60, "ld iyh,b", MicroOp.FETCH_B, MicroOp.STORE_IY_H);
        codeTableFD.define(0x61, "ld iyh,c", MicroOp.FETCH_C, MicroOp.STORE_IY_H);
        codeTableFD.define(0x62, "ld iyh,d", MicroOp.FETCH_D, MicroOp.STORE_IY_H);
        codeTableFD.define(0x63, "ld iyh,e", MicroOp.FETCH_E, MicroOp.STORE_IY_H);
        codeTableFD.define(0x64, "ld iyh,iyh", MicroOp.FETCH_IY_H, MicroOp.STORE_IY_H);
        codeTableFD.define(0x65, "ld iyh,iyl", MicroOp.FETCH_IY_H, MicroOp.STORE_IY_L);
        codeTableFD.define(0x66, "ld h,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_H);
        codeTableFD.define(0x67, "ld iyh,a", MicroOp.FETCH_A, MicroOp.STORE_IY_H);
        codeTableFD.define(0x68, "ld iyl,b", MicroOp.FETCH_B, MicroOp.STORE_IY_L);
        codeTableFD.define(0x69, "ld iyl,c", MicroOp.FETCH_C, MicroOp.STORE_IY_L);
        codeTableFD.define(0x6A, "ld iyl,d", MicroOp.FETCH_D, MicroOp.STORE_IY_L);
        codeTableFD.define(0x6B, "ld iyl,e", MicroOp.FETCH_E, MicroOp.STORE_IY_L);
        codeTableFD.define(0x6C, "ld iyl,iyh", MicroOp.FETCH_IY_H, MicroOp.STORE_IY_L);
        codeTableFD.define(0x6D, "ld iyl,iyl", MicroOp.FETCH_IY_L, MicroOp.STORE_IY_L);
        codeTableFD.define(0x6E, "ld l,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_L);
        codeTableFD.define(0x6F, "ld iyl,a", MicroOp.FETCH_A, MicroOp.STORE_IY_L);

        codeTableFD.define(0x70, "ld (iy+d),b", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_B, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x71, "ld (iy+d),c", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_C, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x72, "ld (iy+d),d", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_D, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x73, "ld (iy+d),e", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_E, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x74, "ld (iy+d),h", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_H, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x75, "ld (iy+d),l", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_L, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x77, "ld (iy+d),a", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_A, MicroOp.STORE_pIY_D);
        codeTableFD.define(0x78, "ld a,b", MicroOp.FETCH_B, MicroOp.STORE_A);
        codeTableFD.define(0x79, "ld a,c", MicroOp.FETCH_C, MicroOp.STORE_A);
        codeTableFD.define(0x7A, "ld a,d", MicroOp.FETCH_D, MicroOp.STORE_A);
        codeTableFD.define(0x7B, "ld a,e", MicroOp.FETCH_E, MicroOp.STORE_A);
        codeTableFD.define(0x7C, "ld a,iyh", MicroOp.FETCH_IY_H, MicroOp.STORE_A);
        codeTableFD.define(0x7D, "ld a,iyl", MicroOp.FETCH_IY_L, MicroOp.STORE_A);
        codeTableFD.define(0x7E, "ld a,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.STORE_A);
        codeTableFD.define(0x7F, "ld a,a", MicroOp.FETCH_A, MicroOp.STORE_A);

        codeTableFD.define(0x80, "add a,b", MicroOp.FETCH_B, MicroOp.ADD);
        codeTableFD.define(0x81, "add a,c", MicroOp.FETCH_C, MicroOp.ADD);
        codeTableFD.define(0x82, "add a,d", MicroOp.FETCH_D, MicroOp.ADD);
        codeTableFD.define(0x83, "add a,e", MicroOp.FETCH_E, MicroOp.ADD);
        codeTableFD.define(0x84, "add a,iyh", MicroOp.FETCH_IY_H, MicroOp.ADD);
        codeTableFD.define(0x85, "add a,iyl", MicroOp.FETCH_IY_L, MicroOp.ADD);
        codeTableFD.define(0x86, "add a,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.ADD);
        codeTableFD.define(0x87, "add a,a", MicroOp.FETCH_A, MicroOp.ADD);
        codeTableFD.define(0x88, "adc a,b", MicroOp.FETCH_B, MicroOp.ADC);
        codeTableFD.define(0x89, "adc a,c", MicroOp.FETCH_C, MicroOp.ADC);
        codeTableFD.define(0x8A, "adc a,d", MicroOp.FETCH_D, MicroOp.ADC);
        codeTableFD.define(0x8B, "adc a,e", MicroOp.FETCH_E, MicroOp.ADC);
        codeTableFD.define(0x8C, "adc a,iyh", MicroOp.FETCH_IY_H, MicroOp.ADC);
        codeTableFD.define(0x8D, "adc a,iyl", MicroOp.FETCH_IY_L, MicroOp.ADC);
        codeTableFD.define(0x8E, "adc a,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.ADC);
        codeTableFD.define(0x8F, "adc a,a", MicroOp.FETCH_A, MicroOp.ADC);

        codeTableFD.define(0x90, "sub a,b", MicroOp.FETCH_B, MicroOp.SUB);
        codeTableFD.define(0x91, "sub a,c", MicroOp.FETCH_C, MicroOp.SUB);
        codeTableFD.define(0x92, "sub a,d", MicroOp.FETCH_D, MicroOp.SUB);
        codeTableFD.define(0x93, "sub a,e", MicroOp.FETCH_E, MicroOp.SUB);
        codeTableFD.define(0x94, "sub a,iyh", MicroOp.FETCH_IY_H, MicroOp.SUB);
        codeTableFD.define(0x95, "sub a,iyl", MicroOp.FETCH_IY_L, MicroOp.SUB);
        codeTableFD.define(0x96, "sub a,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.SUB);
        codeTableFD.define(0x97, "sub a,a", MicroOp.FETCH_A, MicroOp.SUB);
        codeTableFD.define(0x98, "sbc a,b", MicroOp.FETCH_B, MicroOp.SBC);
        codeTableFD.define(0x99, "sbc a,c", MicroOp.FETCH_C, MicroOp.SBC);
        codeTableFD.define(0x9A, "sbc a,d", MicroOp.FETCH_D, MicroOp.SBC);
        codeTableFD.define(0x9B, "sbc a,e", MicroOp.FETCH_E, MicroOp.SBC);
        codeTableFD.define(0x9C, "sbc a,iyh", MicroOp.FETCH_IY_H, MicroOp.SBC);
        codeTableFD.define(0x9D, "sbc a,iyl", MicroOp.FETCH_IY_L, MicroOp.SBC);
        codeTableFD.define(0x9E, "sbc a,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.SBC);
        codeTableFD.define(0x9F, "sbc a,a", MicroOp.FETCH_A, MicroOp.SBC);

        codeTableFD.define(0xA0, "and b", MicroOp.FETCH_B, MicroOp.AND);
        codeTableFD.define(0xA1, "and c", MicroOp.FETCH_C, MicroOp.AND);
        codeTableFD.define(0xA2, "and d", MicroOp.FETCH_D, MicroOp.AND);
        codeTableFD.define(0xA3, "and e", MicroOp.FETCH_E, MicroOp.AND);
        codeTableFD.define(0xA4, "and iyh", MicroOp.FETCH_IY_H, MicroOp.AND);
        codeTableFD.define(0xA5, "and iyl", MicroOp.FETCH_IY_L, MicroOp.AND);
        codeTableFD.define(0xA6, "and (iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.AND);
        codeTableFD.define(0xA7, "and a", MicroOp.FETCH_A, MicroOp.AND);
        codeTableFD.define(0xA8, "xor a,b", MicroOp.FETCH_B, MicroOp.XOR);
        codeTableFD.define(0xA9, "xor a,c", MicroOp.FETCH_C, MicroOp.XOR);
        codeTableFD.define(0xAA, "xor a,d", MicroOp.FETCH_D, MicroOp.XOR);
        codeTableFD.define(0xAB, "xor a,e", MicroOp.FETCH_E, MicroOp.XOR);
        codeTableFD.define(0xAC, "xor a,iyh", MicroOp.FETCH_IY_H, MicroOp.XOR);
        codeTableFD.define(0xAD, "xor a,iyl", MicroOp.FETCH_IY_L, MicroOp.XOR);
        codeTableFD.define(0xAE, "xor a,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.XOR);
        codeTableFD.define(0xAF, "xor a,a", MicroOp.FETCH_A, MicroOp.XOR);

        codeTableFD.define(0xB0, "or b", MicroOp.FETCH_B, MicroOp.OR);
        codeTableFD.define(0xB1, "or c", MicroOp.FETCH_C, MicroOp.OR);
        codeTableFD.define(0xB2, "or d", MicroOp.FETCH_D, MicroOp.OR);
        codeTableFD.define(0xB3, "or e", MicroOp.FETCH_E, MicroOp.OR);
        codeTableFD.define(0xB4, "or iyh", MicroOp.FETCH_IY_H, MicroOp.OR);
        codeTableFD.define(0xB5, "or iyl", MicroOp.FETCH_IY_L, MicroOp.OR);
        codeTableFD.define(0xB6, "or (iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.OR);
        codeTableFD.define(0xB7, "or a", MicroOp.FETCH_A, MicroOp.OR);
        codeTableFD.define(0xB8, "cp a,b", MicroOp.FETCH_B, MicroOp.CP);
        codeTableFD.define(0xB9, "cp a,c", MicroOp.FETCH_C, MicroOp.CP);
        codeTableFD.define(0xBA, "cp a,d", MicroOp.FETCH_D, MicroOp.CP);
        codeTableFD.define(0xBB, "cp a,e", MicroOp.FETCH_E, MicroOp.CP);
        codeTableFD.define(0xBC, "cp a,iyh", MicroOp.FETCH_IY_H, MicroOp.CP);
        codeTableFD.define(0xBD, "cp a,iyl", MicroOp.FETCH_IY_L, MicroOp.CP);
        codeTableFD.define(0xBE, "cp a,(iy+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIY_D, MicroOp.CP);
        codeTableFD.define(0xBF, "cp a,a", MicroOp.FETCH_A, MicroOp.CP);

        codeTableFD.define(0xCB, "IY bit", MicroOp.PREFIX_FD_CB);

        codeTableFD.define(0xE1, "pop iy", MicroOp.POPW, MicroOp.STORE_IY);
        codeTableFD.define(0xE3, "ex (sp),iy", MicroOp.EX_SP_IY);
        codeTableFD.define(0xE5, "push iy", MicroOp.FETCH_IY, MicroOp.PUSHW);
        codeTableFD.define(0xE9, "jp (iy)", MicroOp.FETCH_IY, MicroOp.STORE_PC); // TEST THIS

        codeTableFD.define(0xF9, "LD SP,IY", MicroOp.FETCH_IY, MicroOp.STORE_SP);
    }

    // IX Instructions (DD)
    public void initCodeTableDD() {

        codeTableDD.define(0x04, "inc b", MicroOp.FETCH_B, MicroOp.INC_8, MicroOp.STORE_B);
        codeTableDD.define(0x05, "dec b", MicroOp.FETCH_B, MicroOp.DEC_8, MicroOp.STORE_B);
        codeTableDD.define(0x06, "ld b,n", MicroOp.FETCH_8, MicroOp.STORE_B);

        codeTableDD.define(0x09, "add ix,bc", MicroOp.FETCH_BC, MicroOp.ADD_IX, MicroOp.STORE_IX);

        codeTableDD.define(0x0c, "inc c", MicroOp.FETCH_C, MicroOp.INC_8, MicroOp.STORE_C);
        codeTableDD.define(0x0d, "dec c", MicroOp.FETCH_C, MicroOp.DEC_8, MicroOp.STORE_C);
        codeTableDD.define(0x0e, "ld c,n", MicroOp.FETCH_8, MicroOp.STORE_C);

        codeTableDD.define(0x14, "inc d", MicroOp.FETCH_D, MicroOp.INC_8, MicroOp.STORE_D);
        codeTableDD.define(0x15, "dec d", MicroOp.FETCH_D, MicroOp.DEC_8, MicroOp.STORE_D);
        codeTableDD.define(0x16, "ld d,n", MicroOp.FETCH_8, MicroOp.STORE_D);

        codeTableDD.define(0x19, "add ix,de", MicroOp.FETCH_DE, MicroOp.ADD_IX, MicroOp.STORE_IX);

        codeTableDD.define(0x1c, "inc e", MicroOp.FETCH_E, MicroOp.INC_8, MicroOp.STORE_E);
        codeTableDD.define(0x1d, "dec e", MicroOp.FETCH_E, MicroOp.DEC_8, MicroOp.STORE_E);
        codeTableDD.define(0x1e, "ld e,n", MicroOp.FETCH_8, MicroOp.STORE_E);

        codeTableDD.define(0x21, "ld ix,nn", MicroOp.FETCH_16, MicroOp.STORE_IX);
        codeTableDD.define(0x22, "ld (nn),ix", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_IX, MicroOp.STORE_WORD_AT_ADDRESS);
        codeTableDD.define(0x23, "inc ix", MicroOp.FETCH_IX, MicroOp.INC_16, MicroOp.STORE_IX);
        codeTableDD.define(0x24, "inc ixh", MicroOp.FETCH_IX_H, MicroOp.INC_8, MicroOp.STORE_IX_H);
        codeTableDD.define(0x25, "dec ixh", MicroOp.FETCH_IX_H, MicroOp.DEC_8, MicroOp.STORE_IX_H);
        codeTableDD.define(0x26, "ld ixh, n", MicroOp.FETCH_8, MicroOp.STORE_IX_H);

        codeTableDD.define(0x29, "add ix,ix", MicroOp.FETCH_IX, MicroOp.ADD_IX, MicroOp.STORE_IX);
        codeTableDD.define(0x2A, "ld ix,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_IX);
        codeTableDD.define(0x2B, "dec ix", MicroOp.FETCH_IX, MicroOp.DEC_16, MicroOp.STORE_IX);
        codeTableDD.define(0x2C, "inc ixl", MicroOp.FETCH_IX_L, MicroOp.INC_8, MicroOp.STORE_IX_L);
        codeTableDD.define(0x2D, "dec ixl", MicroOp.FETCH_IX_L, MicroOp.DEC_8, MicroOp.STORE_IX_L);
        codeTableDD.define(0x2E, "ld ixl,n", MicroOp.FETCH_8, MicroOp.STORE_IX_L);

        codeTableDD.define(0x34, "inc (ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.INC_8, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x35, "dec (ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.DEC_8, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x36, "ld (ix+d),n", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_8, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x39, "add ix,sp", MicroOp.FETCH_SP, MicroOp.ADD_IX, MicroOp.STORE_IX);

        codeTableDD.define(0x3c, "inc A", MicroOp.FETCH_A, MicroOp.INC_8, MicroOp.STORE_A);
        codeTableDD.define(0x3d, "dec A", MicroOp.FETCH_A, MicroOp.DEC_8, MicroOp.STORE_A);
        codeTableDD.define(0x3e, "ld A,n", MicroOp.FETCH_8, MicroOp.STORE_A);


        codeTableDD.define(0x40, "ld b,b", MicroOp.FETCH_B, MicroOp.STORE_B);
        codeTableDD.define(0x41, "ld b,c", MicroOp.FETCH_C, MicroOp.STORE_B);
        codeTableDD.define(0x42, "ld b,d", MicroOp.FETCH_D, MicroOp.STORE_B);
        codeTableDD.define(0x43, "ld b,e", MicroOp.FETCH_E, MicroOp.STORE_B);
        codeTableDD.define(0x44, "ld b,ixh", MicroOp.FETCH_IX_H, MicroOp.STORE_B);
        codeTableDD.define(0x45, "ld b,ixl", MicroOp.FETCH_IX_L, MicroOp.STORE_B);
        codeTableDD.define(0x46, "ld b,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_B);
        codeTableDD.define(0x47, "ld b,a", MicroOp.FETCH_A, MicroOp.STORE_B);
        codeTableDD.define(0x48, "ld c,b", MicroOp.FETCH_B, MicroOp.STORE_C);
        codeTableDD.define(0x49, "ld c,c", MicroOp.FETCH_C, MicroOp.STORE_C);
        codeTableDD.define(0x4A, "ld c,d", MicroOp.FETCH_D, MicroOp.STORE_C);
        codeTableDD.define(0x4B, "ld c,e", MicroOp.FETCH_E, MicroOp.STORE_C);
        codeTableDD.define(0x4C, "ld c,ixh", MicroOp.FETCH_IX_H, MicroOp.STORE_C);
        codeTableDD.define(0x4D, "ld c,ixl", MicroOp.FETCH_IX_L, MicroOp.STORE_C);
        codeTableDD.define(0x4E, "ld c,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_C);
        codeTableDD.define(0x4F, "ld c,a", MicroOp.FETCH_A, MicroOp.STORE_C);

        codeTableDD.define(0x50, "ld d,b", MicroOp.FETCH_B, MicroOp.STORE_D);
        codeTableDD.define(0x51, "ld d,c", MicroOp.FETCH_C, MicroOp.STORE_D);
        codeTableDD.define(0x52, "ld d,d", MicroOp.FETCH_D, MicroOp.STORE_D);
        codeTableDD.define(0x53, "ld d,e", MicroOp.FETCH_E, MicroOp.STORE_D);
        codeTableDD.define(0x54, "ld d,ixh", MicroOp.FETCH_IX_H, MicroOp.STORE_D);
        codeTableDD.define(0x55, "ld d,ixl", MicroOp.FETCH_IX_L, MicroOp.STORE_D);
        codeTableDD.define(0x56, "ld d,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_D);
        codeTableDD.define(0x57, "ld d,a", MicroOp.FETCH_A, MicroOp.STORE_D);
        codeTableDD.define(0x58, "ld e,b", MicroOp.FETCH_B, MicroOp.STORE_E);
        codeTableDD.define(0x59, "ld e,c", MicroOp.FETCH_C, MicroOp.STORE_E);
        codeTableDD.define(0x5A, "ld e,d", MicroOp.FETCH_D, MicroOp.STORE_E);
        codeTableDD.define(0x5B, "ld e,e", MicroOp.FETCH_E, MicroOp.STORE_E);
        codeTableDD.define(0x5C, "ld e,ixh", MicroOp.FETCH_IX_H, MicroOp.STORE_E);
        codeTableDD.define(0x5D, "ld e,ixl", MicroOp.FETCH_IX_L, MicroOp.STORE_E);
        codeTableDD.define(0x5E, "ld e,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_E);
        codeTableDD.define(0x5F, "ld e,a", MicroOp.FETCH_A, MicroOp.STORE_E);

        codeTableDD.define(0x60, "ld ixh,b", MicroOp.FETCH_B, MicroOp.STORE_IX_H);
        codeTableDD.define(0x61, "ld ixh,c", MicroOp.FETCH_C, MicroOp.STORE_IX_H);
        codeTableDD.define(0x62, "ld ixh,d", MicroOp.FETCH_D, MicroOp.STORE_IX_H);
        codeTableDD.define(0x63, "ld ixh,e", MicroOp.FETCH_E, MicroOp.STORE_IX_H);
        codeTableDD.define(0x64, "ld ixh,ixh", MicroOp.FETCH_IX_H, MicroOp.STORE_IX_H);
        codeTableDD.define(0x65, "ld ixh,ixl", MicroOp.FETCH_IX_H, MicroOp.STORE_IX_L);
        codeTableDD.define(0x66, "ld h,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_H);
        codeTableDD.define(0x67, "ld ixh,a", MicroOp.FETCH_A, MicroOp.STORE_IX_H);
        codeTableDD.define(0x68, "ld ixl,b", MicroOp.FETCH_B, MicroOp.STORE_IX_L);
        codeTableDD.define(0x69, "ld ixl,c", MicroOp.FETCH_C, MicroOp.STORE_IX_L);
        codeTableDD.define(0x6A, "ld ixl,d", MicroOp.FETCH_D, MicroOp.STORE_IX_L);
        codeTableDD.define(0x6B, "ld ixl,e", MicroOp.FETCH_E, MicroOp.STORE_IX_L);
        codeTableDD.define(0x6C, "ld ixl,ixh", MicroOp.FETCH_IX_H, MicroOp.STORE_IX_L);
        codeTableDD.define(0x6D, "ld ixl,ixl", MicroOp.FETCH_IX_L, MicroOp.STORE_IX_L);
        codeTableDD.define(0x6E, "ld l,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_L);
        codeTableDD.define(0x6F, "ld ixl,a", MicroOp.FETCH_A, MicroOp.STORE_IX_L);

        codeTableDD.define(0x70, "ld (ix+d),b", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_B, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x71, "ld (ix+d),c", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_C, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x72, "ld (ix+d),d", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_D, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x73, "ld (ix+d),e", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_E, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x74, "ld (ix+d),h", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_H, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x75, "ld (ix+d),l", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_L, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x77, "ld (ix+d),a", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_A, MicroOp.STORE_pIX_D);
        codeTableDD.define(0x78, "ld a,b", MicroOp.FETCH_B, MicroOp.STORE_A);
        codeTableDD.define(0x79, "ld a,c", MicroOp.FETCH_C, MicroOp.STORE_A);
        codeTableDD.define(0x7A, "ld a,d", MicroOp.FETCH_D, MicroOp.STORE_A);
        codeTableDD.define(0x7B, "ld a,e", MicroOp.FETCH_E, MicroOp.STORE_A);
        codeTableDD.define(0x7C, "ld a,ixh", MicroOp.FETCH_IX_H, MicroOp.STORE_A);
        codeTableDD.define(0x7D, "ld a,ixl", MicroOp.FETCH_IX_L, MicroOp.STORE_A);
        codeTableDD.define(0x7E, "ld a,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.STORE_A);
        codeTableDD.define(0x7F, "ld a,a", MicroOp.FETCH_A, MicroOp.STORE_A);

        codeTableDD.define(0x80, "add a,b", MicroOp.FETCH_B, MicroOp.ADD);
        codeTableDD.define(0x81, "add a,c", MicroOp.FETCH_C, MicroOp.ADD);
        codeTableDD.define(0x82, "add a,d", MicroOp.FETCH_D, MicroOp.ADD);
        codeTableDD.define(0x83, "add a,e", MicroOp.FETCH_E, MicroOp.ADD);
        codeTableDD.define(0x84, "add a,ixh", MicroOp.FETCH_IX_H, MicroOp.ADD);
        codeTableDD.define(0x85, "add a,ixl", MicroOp.FETCH_IX_L, MicroOp.ADD);
        codeTableDD.define(0x86, "add a,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.ADD);
        codeTableDD.define(0x87, "add a,a", MicroOp.FETCH_A, MicroOp.ADD);
        codeTableDD.define(0x88, "adc a,b", MicroOp.FETCH_B, MicroOp.ADC);
        codeTableDD.define(0x89, "adc a,c", MicroOp.FETCH_C, MicroOp.ADC);
        codeTableDD.define(0x8A, "adc a,d", MicroOp.FETCH_D, MicroOp.ADC);
        codeTableDD.define(0x8B, "adc a,e", MicroOp.FETCH_E, MicroOp.ADC);
        codeTableDD.define(0x8C, "adc a,ixh", MicroOp.FETCH_IX_H, MicroOp.ADC);
        codeTableDD.define(0x8D, "adc a,ixl", MicroOp.FETCH_IX_L, MicroOp.ADC);
        codeTableDD.define(0x8E, "adc a,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.ADC);
        codeTableDD.define(0x8F, "adc a,a", MicroOp.FETCH_A, MicroOp.ADC);

        codeTableDD.define(0x90, "sub a,b", MicroOp.FETCH_B, MicroOp.SUB);
        codeTableDD.define(0x91, "sub a,c", MicroOp.FETCH_C, MicroOp.SUB);
        codeTableDD.define(0x92, "sub a,d", MicroOp.FETCH_D, MicroOp.SUB);
        codeTableDD.define(0x93, "sub a,e", MicroOp.FETCH_E, MicroOp.SUB);
        codeTableDD.define(0x94, "sub a,ixh", MicroOp.FETCH_IX_H, MicroOp.SUB);
        codeTableDD.define(0x95, "sub a,ixl", MicroOp.FETCH_IX_L, MicroOp.SUB);
        codeTableDD.define(0x96, "sub a,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.SUB);
        codeTableDD.define(0x97, "sub a,a", MicroOp.FETCH_A, MicroOp.SUB);
        codeTableDD.define(0x98, "sbc a,b", MicroOp.FETCH_B, MicroOp.SBC);
        codeTableDD.define(0x99, "sbc a,c", MicroOp.FETCH_C, MicroOp.SBC);
        codeTableDD.define(0x9A, "sbc a,d", MicroOp.FETCH_D, MicroOp.SBC);
        codeTableDD.define(0x9B, "sbc a,e", MicroOp.FETCH_E, MicroOp.SBC);
        codeTableDD.define(0x9C, "sbc a,ixh", MicroOp.FETCH_IX_H, MicroOp.SBC);
        codeTableDD.define(0x9D, "sbc a,ixl", MicroOp.FETCH_IX_L, MicroOp.SBC);
        codeTableDD.define(0x9E, "sbc a,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.SBC);
        codeTableDD.define(0x9F, "sbc a,a", MicroOp.FETCH_A, MicroOp.SBC);

        codeTableDD.define(0xA0, "and b", MicroOp.FETCH_B, MicroOp.AND);
        codeTableDD.define(0xA1, "and c", MicroOp.FETCH_C, MicroOp.AND);
        codeTableDD.define(0xA2, "and d", MicroOp.FETCH_D, MicroOp.AND);
        codeTableDD.define(0xA3, "and e", MicroOp.FETCH_E, MicroOp.AND);
        codeTableDD.define(0xA4, "and ixh", MicroOp.FETCH_IX_H, MicroOp.AND);
        codeTableDD.define(0xA5, "and ixl", MicroOp.FETCH_IX_L, MicroOp.AND);
        codeTableDD.define(0xA6, "and (ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.AND);
        codeTableDD.define(0xA7, "and a", MicroOp.FETCH_A, MicroOp.AND);
        codeTableDD.define(0xA8, "xor a,b", MicroOp.FETCH_B, MicroOp.XOR);
        codeTableDD.define(0xA9, "xor a,c", MicroOp.FETCH_C, MicroOp.XOR);
        codeTableDD.define(0xAA, "xor a,d", MicroOp.FETCH_D, MicroOp.XOR);
        codeTableDD.define(0xAB, "xor a,e", MicroOp.FETCH_E, MicroOp.XOR);
        codeTableDD.define(0xAC, "xor a,ixh", MicroOp.FETCH_IX_H, MicroOp.XOR);
        codeTableDD.define(0xAD, "xor a,ixl", MicroOp.FETCH_IX_L, MicroOp.XOR);
        codeTableDD.define(0xAE, "xor a,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.XOR);
        codeTableDD.define(0xAF, "xor a,a", MicroOp.FETCH_A, MicroOp.XOR);

        codeTableDD.define(0xB0, "or b", MicroOp.FETCH_B, MicroOp.OR);
        codeTableDD.define(0xB1, "or c", MicroOp.FETCH_C, MicroOp.OR);
        codeTableDD.define(0xB2, "or d", MicroOp.FETCH_D, MicroOp.OR);
        codeTableDD.define(0xB3, "or e", MicroOp.FETCH_E, MicroOp.OR);
        codeTableDD.define(0xB4, "or ixh", MicroOp.FETCH_IX_H, MicroOp.OR);
        codeTableDD.define(0xB5, "or ixl", MicroOp.FETCH_IX_L, MicroOp.OR);
        codeTableDD.define(0xB6, "or (ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.OR);
        codeTableDD.define(0xB7, "or a", MicroOp.FETCH_A, MicroOp.OR);
        codeTableDD.define(0xB8, "cp a,b", MicroOp.FETCH_B, MicroOp.CP);
        codeTableDD.define(0xB9, "cp a,c", MicroOp.FETCH_C, MicroOp.CP);
        codeTableDD.define(0xBA, "cp a,d", MicroOp.FETCH_D, MicroOp.CP);
        codeTableDD.define(0xBB, "cp a,e", MicroOp.FETCH_E, MicroOp.CP);
        codeTableDD.define(0xBC, "cp a,ixh", MicroOp.FETCH_IX_H, MicroOp.CP);
        codeTableDD.define(0xBD, "cp a,ixl", MicroOp.FETCH_IX_L, MicroOp.CP);
        codeTableDD.define(0xBE, "cp a,(ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.CP);
        codeTableDD.define(0xBF, "cp a,a", MicroOp.FETCH_A, MicroOp.CP);

        codeTableDD.define(0xE1, "pop ix", MicroOp.POPW, MicroOp.STORE_IX);
        codeTableDD.define(0xE5, "push ix", MicroOp.FETCH_IX, MicroOp.PUSHW);

        codeTableDD.define(0xE9, "jp (ix)", MicroOp.FETCH_IX, MicroOp.STORE_PC);

        codeTableDD.define(0xBE, "cp (ix+d)", MicroOp.FETCH_BYTE_TO_DISPLACEMENT, MicroOp.FETCH_pIX_D, MicroOp.CP);

        codeTableDD.define(0xBC, "cp ixh", MicroOp.FETCH_IX_H, MicroOp.CP);
        codeTableDD.define(0xBD, "cp ixl", MicroOp.FETCH_IX_L, MicroOp.CP);

        codeTableDD.define(0xB4, "or ixh", MicroOp.FETCH_IX_H, MicroOp.OR);
        codeTableDD.define(0xB5, "or ixl", MicroOp.FETCH_IX_L, MicroOp.OR);

        codeTableDD.define(0xCB, "IX bit", MicroOp.PREFIX_DD_CB);

        codeTableDD.define(0xE3, "ex (sp),ix", MicroOp.EX_SP_IX);

        codeTableDD.define(0xF9, "LD SP,IX", MicroOp.FETCH_IX, MicroOp.STORE_SP);
    }

    public void initCodeTableED() {
        codeTableED.define(0x00, "in0 b,(n)", MicroOp.FETCH_8_ADDRESS, MicroOp.IN0, MicroOp.STORE_B);
        codeTableED.define(0x10, "in0 d,(n)", MicroOp.FETCH_8_ADDRESS, MicroOp.IN0, MicroOp.STORE_D);
        codeTableED.define(0x20, "in0 h,(n)", MicroOp.FETCH_8_ADDRESS, MicroOp.IN0, MicroOp.STORE_H);

        codeTableED.define(0x01, "out0 (n),b", MicroOp.FETCH_8_ADDRESS, MicroOp.FETCH_B, MicroOp.OUT0);
        codeTableED.define(0x11, "out0 (n),d", MicroOp.FETCH_8_ADDRESS, MicroOp.FETCH_D, MicroOp.OUT0);
        codeTableED.define(0x21, "out0 (n),h", MicroOp.FETCH_8_ADDRESS, MicroOp.FETCH_H, MicroOp.OUT0);

        codeTableED.define(0x08, "in0 c,(n)", MicroOp.FETCH_8_ADDRESS, MicroOp.IN0, MicroOp.STORE_C);
        codeTableED.define(0x18, "in0 e,(n)", MicroOp.FETCH_8_ADDRESS, MicroOp.IN0, MicroOp.STORE_E);
        codeTableED.define(0x28, "in0 l,(n)", MicroOp.FETCH_8_ADDRESS, MicroOp.IN0, MicroOp.STORE_L);
        codeTableED.define(0x38, "in0 a,(n)", MicroOp.FETCH_8_ADDRESS, MicroOp.IN0, MicroOp.STORE_A);
        codeTableED.define(0x09, "out0 (n),c", MicroOp.FETCH_8_ADDRESS, MicroOp.FETCH_C, MicroOp.OUT0);

        codeTableED.define(0x40, "in b,(c)", MicroOp.FETCH_PORT_C, MicroOp.STORE_B);
        codeTableED.define(0x41, "out (c),b", MicroOp.FETCH_B, MicroOp.OUT_PORT_C);
        codeTableED.define(0x42, "sbc hl,bc", MicroOp.FETCH_BC, MicroOp.SBC_HL);
        codeTableED.define(0x43, "ld (nn),bc", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_BC, MicroOp.STORE_WORD_AT_ADDRESS);

        codeTableED.define(0x44, "neg", MicroOp.NEG);
        codeTableED.define(0x45, "RETN", MicroOp.RETN);
        codeTableED.define(0x46, "IM 0", MicroOp.IM_0); // Undocumented
        codeTableED.define(0x47, "LD i,a", MicroOp.FETCH_A, MicroOp.STORE_I);
        codeTableED.define(0x48, "in c,(c)", MicroOp.FETCH_PORT_C, MicroOp.STORE_C);
        codeTableED.define(0x49, "out (c),c", MicroOp.FETCH_C, MicroOp.OUT_PORT_C);
        codeTableED.define(0x4A, "adc hl,bc", MicroOp.FETCH_BC, MicroOp.ADC_HL);
        codeTableED.define(0x4B, "ld bc,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_BC);

//        codeTableED.define(0x4C, "MLT BC", MicroOp.MLT_BC);
        codeTableED.define(0x4C, "neg", MicroOp.NEG); // Undocumented - instruction page has something different

        codeTableED.define(0x4D, "RETI", MicroOp.RETI);
        codeTableED.define(0x4E, "IM 0", MicroOp.IM_0); // Undocumented
        codeTableED.define(0x4F, "LD r,a", MicroOp.FETCH_A, MicroOp.STORE_R);

        codeTableED.define(0x50, "in d,(c)", MicroOp.FETCH_PORT_C, MicroOp.STORE_D);
        codeTableED.define(0x51, "out (c),d", MicroOp.FETCH_D, MicroOp.OUT_PORT_C);
        codeTableED.define(0x52, "sbc hl,de", MicroOp.FETCH_DE, MicroOp.SBC_HL);
        codeTableED.define(0x53, "ld (nn),de", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_DE, MicroOp.STORE_WORD_AT_ADDRESS);
        codeTableED.define(0x54, "neg", MicroOp.NEG); // Undocumented - instruction page has something different
        codeTableED.define(0x55, "RETN", MicroOp.RETN); // Undocumented
        codeTableED.define(0x56, "IM 1", MicroOp.IM_1); // Undocumented
        codeTableED.define(0x57, "LD a,i", MicroOp.LD_A_I);
        codeTableED.define(0x58, "in e,(c)", MicroOp.FETCH_PORT_C, MicroOp.STORE_E);
        codeTableED.define(0x59, "out (c),e", MicroOp.FETCH_E, MicroOp.OUT_PORT_C);
        codeTableED.define(0x5A, "adc hl,de", MicroOp.FETCH_DE, MicroOp.ADC_HL);

        codeTableED.define(0x5B, "ld de,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_DE);
//        codeTableED.define(0x5C, "MLT DE", MicroOp.MLT_DE);
        codeTableED.define(0x5C, "neg", MicroOp.NEG); // Undocumented - instruction page has something different
        codeTableED.define(0x5D, "RETN", MicroOp.RETN); // Undocumented
        codeTableED.define(0x5E, "IM 2", MicroOp.IM_2); // Undocumented
        codeTableED.define(0x5F, "LD a,r", MicroOp.FETCH_R, MicroOp.STORE_A);

        codeTableED.define(0x60, "in h,(c)", MicroOp.FETCH_PORT_C, MicroOp.STORE_H);
        codeTableED.define(0x61, "out (c),h", MicroOp.FETCH_H, MicroOp.OUT_PORT_C);
        codeTableED.define(0x62, "sbc hl,hl", MicroOp.FETCH_HL, MicroOp.SBC_HL);
        codeTableED.define(0x63, "ld (nn),hl", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_HL, MicroOp.STORE_WORD_AT_ADDRESS);
        codeTableED.define(0x64, "neg", MicroOp.NEG); // Undocumented - instruction page has something different
        codeTableED.define(0x65, "RETN", MicroOp.RETN); // Undocumented
        codeTableED.define(0x66, "IM 0", MicroOp.IM_0); // Undocumented
        codeTableED.define(0x67, "RRD", MicroOp.RRD);
        codeTableED.define(0x68, "in l,(c)", MicroOp.FETCH_PORT_C, MicroOp.STORE_L);
        codeTableED.define(0x69, "out (c),l", MicroOp.FETCH_L, MicroOp.OUT_PORT_C);
        codeTableED.define(0x6A, "adc hl,hl", MicroOp.FETCH_HL, MicroOp.ADC_HL);

        codeTableED.define(0x6B, "ld hl,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_HL);
//        codeTableED.define(0x6C, "MLT HL", MicroOp.MLT_HL);
        codeTableED.define(0x6C, "neg", MicroOp.NEG); // Undocumented - instruction page has something different
        codeTableED.define(0x6D, "RETN", MicroOp.RETN); // Undocumented
        codeTableED.define(0x6E, "IM 0", MicroOp.IM_0); // Undocumented
        codeTableED.define(0x6F, "RLD", MicroOp.RLD);

        codeTableED.define(0x70, "in (c)", MicroOp.FETCH_PORT_C);
        codeTableED.define(0x71, "out (c),0", MicroOp.FETCH_0, MicroOp.OUT_PORT_C);
        codeTableED.define(0x72, "sbc hl,sp", MicroOp.FETCH_SP, MicroOp.SBC_HL);

        codeTableED.define(0x73, "ld (nn),sp", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_SP, MicroOp.STORE_WORD_AT_ADDRESS);

        codeTableED.define(0x74, "neg", MicroOp.NEG); // Undocumented - instruction page has something different
        codeTableED.define(0x75, "RETN", MicroOp.RETN); // Undocumented
        codeTableED.define(0x76, "IM 1", MicroOp.IM_1); // Undocumented
        codeTableED.define(0x78, "in a,(c)", MicroOp.FETCH_PORT_C, MicroOp.STORE_A);
        codeTableED.define(0x79, "out (c),a", MicroOp.FETCH_A, MicroOp.OUT_PORT_C);

        codeTableED.define(0x7A, "adc hl,sp", MicroOp.FETCH_SP, MicroOp.ADC_HL);
        codeTableED.define(0x7B, "ld sp,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_SP);
//        codeTableED.define(0x7C, "MLT SP", MicroOp.MLT_SP);
        codeTableED.define(0x7C, "neg", MicroOp.NEG); // Undocumented - instruction page has something different
        codeTableED.define(0x7D, "RETN", MicroOp.RETN); // Undocumented
        codeTableED.define(0x7E, "IM 2", MicroOp.IM_2); // Undocumented
        codeTableED.define(0xA0, "LDI", MicroOp.LDI);
        codeTableED.define(0xA1, "CPI", MicroOp.CPI);
        codeTableED.define(0xA2, "INI", MicroOp.INI);
        codeTableED.define(0xA3, "OUTI", MicroOp.OUTI);
        codeTableED.define(0xA8, "LDD", MicroOp.LDD);
        codeTableED.define(0xA9, "CPD", MicroOp.CPD);
        codeTableED.define(0xAA, "IND", MicroOp.IND);
        codeTableED.define(0xAB, "OUTD", MicroOp.OUTD);

        codeTableED.define(0xB0, "LDIR", MicroOp.LDIR);
        codeTableED.define(0xB1, "CPIR", MicroOp.CPIR);
        codeTableED.define(0xB3, "OTIR", MicroOp.OTIR);
        codeTableED.define(0xB8, "LDDR", MicroOp.LDDR);
        codeTableED.define(0xB9, "CPDR", MicroOp.CPDR);
        codeTableED.define(0xBB, "OTDR", MicroOp.OTDR);
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
        codeTableMain.define(0x34, "INC (HL)", MicroOp.FETCH_pHL, MicroOp.INC_8, MicroOp.STORE_pHL);
        codeTableMain.define(0x35, "DEC (HL)", MicroOp.FETCH_pHL, MicroOp.DEC_8, MicroOp.STORE_pHL);
        codeTableMain.define(0x36, "LD (HL),d8", MicroOp.FETCH_8, MicroOp.STORE_pHL);
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
        codeTableMain.define(0x46, "LD B,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_B);
        codeTableMain.define(0x47, "LD B,A", MicroOp.FETCH_A, MicroOp.STORE_B);
        codeTableMain.define(0x48, "LD C,B", MicroOp.FETCH_B, MicroOp.STORE_C);
        codeTableMain.define(0x49, "LD C,C", MicroOp.FETCH_C, MicroOp.STORE_C);
        codeTableMain.define(0x4A, "LD C,D", MicroOp.FETCH_D, MicroOp.STORE_C);
        codeTableMain.define(0x4B, "LD C,E", MicroOp.FETCH_E, MicroOp.STORE_C);
        codeTableMain.define(0x4C, "LD C,H", MicroOp.FETCH_H, MicroOp.STORE_C);
        codeTableMain.define(0x4D, "LD C,L", MicroOp.FETCH_L, MicroOp.STORE_C);
        codeTableMain.define(0x4E, "LD C,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_C);
        codeTableMain.define(0x4F, "LD C,A", MicroOp.FETCH_A, MicroOp.STORE_C);

        codeTableMain.define(0x50, "LD D,B", MicroOp.FETCH_B, MicroOp.STORE_D);
        codeTableMain.define(0x51, "LD D,C", MicroOp.FETCH_C, MicroOp.STORE_D);
        codeTableMain.define(0x52, "LD D,D", MicroOp.FETCH_D, MicroOp.STORE_D);
        codeTableMain.define(0x53, "LD D,E", MicroOp.FETCH_E, MicroOp.STORE_D);
        codeTableMain.define(0x54, "LD D,H", MicroOp.FETCH_H, MicroOp.STORE_D);
        codeTableMain.define(0x55, "LD D,L", MicroOp.FETCH_L, MicroOp.STORE_D);
        codeTableMain.define(0x56, "LD D,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_D);
        codeTableMain.define(0x57, "LD D,A", MicroOp.FETCH_A, MicroOp.STORE_D);
        codeTableMain.define(0x58, "LD E,B", MicroOp.FETCH_B, MicroOp.STORE_E);
        codeTableMain.define(0x59, "LD E,C", MicroOp.FETCH_C, MicroOp.STORE_E);
        codeTableMain.define(0x5A, "LD E,D", MicroOp.FETCH_D, MicroOp.STORE_E);
        codeTableMain.define(0x5B, "LD E,E", MicroOp.FETCH_E, MicroOp.STORE_E);
        codeTableMain.define(0x5C, "LD E,H", MicroOp.FETCH_H, MicroOp.STORE_E);
        codeTableMain.define(0x5D, "LD E,L", MicroOp.FETCH_L, MicroOp.STORE_E);
        codeTableMain.define(0x5E, "LD E,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_E);
        codeTableMain.define(0x5F, "LD E,A", MicroOp.FETCH_A, MicroOp.STORE_E);

        codeTableMain.define(0x60, "LD H,B", MicroOp.FETCH_B, MicroOp.STORE_H);
        codeTableMain.define(0x61, "LD H,C", MicroOp.FETCH_C, MicroOp.STORE_H);
        codeTableMain.define(0x62, "LD H,D", MicroOp.FETCH_D, MicroOp.STORE_H);
        codeTableMain.define(0x63, "LD H,E", MicroOp.FETCH_E, MicroOp.STORE_H);
        codeTableMain.define(0x64, "LD H,H", MicroOp.FETCH_H, MicroOp.STORE_H);
        codeTableMain.define(0x65, "LD H,L", MicroOp.FETCH_L, MicroOp.STORE_H);
        codeTableMain.define(0x66, "LD H,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_H);
        codeTableMain.define(0x67, "LD H,A", MicroOp.FETCH_A, MicroOp.STORE_H);
        codeTableMain.define(0x68, "LD L,B", MicroOp.FETCH_B, MicroOp.STORE_L);
        codeTableMain.define(0x69, "LD L,C", MicroOp.FETCH_C, MicroOp.STORE_L);
        codeTableMain.define(0x6A, "LD L,D", MicroOp.FETCH_D, MicroOp.STORE_L);
        codeTableMain.define(0x6B, "LD L,E", MicroOp.FETCH_E, MicroOp.STORE_L);
        codeTableMain.define(0x6C, "LD L,H", MicroOp.FETCH_H, MicroOp.STORE_L);
        codeTableMain.define(0x6D, "LD L,L", MicroOp.FETCH_L, MicroOp.STORE_L);
        codeTableMain.define(0x6E, "LD L,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_L);
        codeTableMain.define(0x6F, "LD L,A", MicroOp.FETCH_A, MicroOp.STORE_L);

        codeTableMain.define(0x70, "LD (HL),B", MicroOp.FETCH_B, MicroOp.STORE_pHL);
        codeTableMain.define(0x71, "LD (HL),C", MicroOp.FETCH_C, MicroOp.STORE_pHL);
        codeTableMain.define(0x72, "LD (HL),D", MicroOp.FETCH_D, MicroOp.STORE_pHL);
        codeTableMain.define(0x73, "LD (HL),E", MicroOp.FETCH_E, MicroOp.STORE_pHL);
        codeTableMain.define(0x74, "LD (HL),H", MicroOp.FETCH_H, MicroOp.STORE_pHL);
        codeTableMain.define(0x75, "LD (HL),L", MicroOp.FETCH_L, MicroOp.STORE_pHL);
        codeTableMain.define(0x76, "HALT", MicroOp.HALT);
        codeTableMain.define(0x77, "LD (HL),A", MicroOp.FETCH_A, MicroOp.STORE_pHL);
        codeTableMain.define(0x78, "LD A,B", MicroOp.FETCH_B, MicroOp.STORE_A);
        codeTableMain.define(0x79, "LD A,C", MicroOp.FETCH_C, MicroOp.STORE_A);
        codeTableMain.define(0x7A, "LD A,D", MicroOp.FETCH_D, MicroOp.STORE_A);
        codeTableMain.define(0x7B, "LD A,E", MicroOp.FETCH_E, MicroOp.STORE_A);
        codeTableMain.define(0x7C, "LD A,H", MicroOp.FETCH_H, MicroOp.STORE_A);
        codeTableMain.define(0x7D, "LD A,L", MicroOp.FETCH_L, MicroOp.STORE_A);
        codeTableMain.define(0x7E, "LD A,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_A);
        codeTableMain.define(0x7F, "LD A,A", MicroOp.FETCH_A, MicroOp.STORE_A);

        codeTableMain.define(0x80, "ADD A,B", MicroOp.FETCH_B, MicroOp.ADD);
        codeTableMain.define(0x81, "ADD A,C", MicroOp.FETCH_C, MicroOp.ADD);
        codeTableMain.define(0x82, "ADD A,D", MicroOp.FETCH_D, MicroOp.ADD);
        codeTableMain.define(0x83, "ADD A,E", MicroOp.FETCH_E, MicroOp.ADD);
        codeTableMain.define(0x84, "ADD A,H", MicroOp.FETCH_H, MicroOp.ADD);
        codeTableMain.define(0x85, "ADD A,L", MicroOp.FETCH_L, MicroOp.ADD);
        codeTableMain.define(0x86, "ADD A,(HL)", MicroOp.FETCH_pHL, MicroOp.ADD);
        codeTableMain.define(0x87, "ADD A,A", MicroOp.FETCH_A, MicroOp.ADD);
        codeTableMain.define(0x88, "ADC A,B", MicroOp.FETCH_B, MicroOp.ADC);
        codeTableMain.define(0x89, "ADC A,C", MicroOp.FETCH_C, MicroOp.ADC);
        codeTableMain.define(0x8A, "ADC A,D", MicroOp.FETCH_D, MicroOp.ADC);
        codeTableMain.define(0x8B, "ADC A,E", MicroOp.FETCH_E, MicroOp.ADC);
        codeTableMain.define(0x8C, "ADC A,H", MicroOp.FETCH_H, MicroOp.ADC);
        codeTableMain.define(0x8D, "ADC A,L", MicroOp.FETCH_L, MicroOp.ADC);
        codeTableMain.define(0x8E, "ADC A,(HL)", MicroOp.FETCH_pHL, MicroOp.ADC);
        codeTableMain.define(0x8F, "ADC A,A", MicroOp.FETCH_A, MicroOp.ADC);

        codeTableMain.define(0x90, "SUB A,B", MicroOp.FETCH_B, MicroOp.SUB);
        codeTableMain.define(0x91, "SUB A,C", MicroOp.FETCH_C, MicroOp.SUB);
        codeTableMain.define(0x92, "SUB A,D", MicroOp.FETCH_D, MicroOp.SUB);
        codeTableMain.define(0x93, "SUB A,E", MicroOp.FETCH_E, MicroOp.SUB);
        codeTableMain.define(0x94, "SUB A,H", MicroOp.FETCH_H, MicroOp.SUB);
        codeTableMain.define(0x95, "SUB A,L", MicroOp.FETCH_L, MicroOp.SUB);
        codeTableMain.define(0x96, "SUB A,(HL)", MicroOp.FETCH_pHL, MicroOp.SUB);
        codeTableMain.define(0x97, "SUB A,A", MicroOp.FETCH_A, MicroOp.SUB);
        codeTableMain.define(0x98, "SBC A,B", MicroOp.FETCH_B, MicroOp.SBC);
        codeTableMain.define(0x99, "SBC A,C", MicroOp.FETCH_C, MicroOp.SBC);
        codeTableMain.define(0x9A, "SBC A,D", MicroOp.FETCH_D, MicroOp.SBC);
        codeTableMain.define(0x9B, "SBC A,E", MicroOp.FETCH_E, MicroOp.SBC);
        codeTableMain.define(0x9C, "SBC A,H", MicroOp.FETCH_H, MicroOp.SBC);
        codeTableMain.define(0x9D, "SBC A,L", MicroOp.FETCH_L, MicroOp.SBC);
        codeTableMain.define(0x9E, "SBC A,(HL)", MicroOp.FETCH_pHL, MicroOp.SBC);
        codeTableMain.define(0x9F, "SBC A,A", MicroOp.FETCH_A, MicroOp.SBC);


        codeTableMain.define(0xA0, "AND A,B", MicroOp.FETCH_B, MicroOp.AND);
        codeTableMain.define(0xA1, "AND A,C", MicroOp.FETCH_C, MicroOp.AND);
        codeTableMain.define(0xA2, "AND A,D", MicroOp.FETCH_D, MicroOp.AND);
        codeTableMain.define(0xA3, "AND A,E", MicroOp.FETCH_E, MicroOp.AND);
        codeTableMain.define(0xA4, "AND A,H", MicroOp.FETCH_H, MicroOp.AND);
        codeTableMain.define(0xA5, "AND A,L", MicroOp.FETCH_L, MicroOp.AND);
        codeTableMain.define(0xA6, "AND A,(HL)", MicroOp.FETCH_pHL, MicroOp.AND);
        codeTableMain.define(0xA7, "AND A,A", MicroOp.FETCH_A, MicroOp.AND);
        codeTableMain.define(0xA8, "XOR A,B", MicroOp.FETCH_B, MicroOp.XOR);
        codeTableMain.define(0xA9, "XOR A,C", MicroOp.FETCH_C, MicroOp.XOR);
        codeTableMain.define(0xAA, "XOR A,D", MicroOp.FETCH_D, MicroOp.XOR);
        codeTableMain.define(0xAB, "XOR A,E", MicroOp.FETCH_E, MicroOp.XOR);
        codeTableMain.define(0xAC, "XOR A,H", MicroOp.FETCH_H, MicroOp.XOR);
        codeTableMain.define(0xAD, "XOR A,L", MicroOp.FETCH_L, MicroOp.XOR);
        codeTableMain.define(0xAE, "XOR A,(HL)", MicroOp.FETCH_pHL, MicroOp.XOR);
        codeTableMain.define(0xAF, "XOR A,A", MicroOp.FETCH_A, MicroOp.XOR);

        codeTableMain.define(0xB0, "OR A,B", MicroOp.FETCH_B, MicroOp.OR);
        codeTableMain.define(0xB1, "OR A,C", MicroOp.FETCH_C, MicroOp.OR);
        codeTableMain.define(0xB2, "OR A,D", MicroOp.FETCH_D, MicroOp.OR);
        codeTableMain.define(0xB3, "OR A,E", MicroOp.FETCH_E, MicroOp.OR);
        codeTableMain.define(0xB4, "OR A,H", MicroOp.FETCH_H, MicroOp.OR);
        codeTableMain.define(0xB5, "OR A,L", MicroOp.FETCH_L, MicroOp.OR);
        codeTableMain.define(0xB6, "OR A,(HL)", MicroOp.FETCH_pHL, MicroOp.OR);
        codeTableMain.define(0xB7, "OR A,A", MicroOp.FETCH_A, MicroOp.OR);
        codeTableMain.define(0xB8, "CP A,B", MicroOp.FETCH_B, MicroOp.CP);
        codeTableMain.define(0xB9, "CP A,C", MicroOp.FETCH_C, MicroOp.CP);
        codeTableMain.define(0xBA, "CP A,D", MicroOp.FETCH_D, MicroOp.CP);
        codeTableMain.define(0xBB, "CP A,E", MicroOp.FETCH_E, MicroOp.CP);
        codeTableMain.define(0xBC, "CP A,H", MicroOp.FETCH_H, MicroOp.CP);
        codeTableMain.define(0xBD, "CP A,L", MicroOp.FETCH_L, MicroOp.CP);
        codeTableMain.define(0xBE, "CP A,(HL)", MicroOp.FETCH_pHL, MicroOp.CP);
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
        codeTableMain.define(0xE8, "RET pe", MicroOp.RET_PE);
        codeTableMain.define(0xE9, "JP (HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.JP); // CHECK
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
        codeTableMain.define(0xF8, "RET m", MicroOp.RET_M); // FIX
        codeTableMain.define(0xF9, "LD SP,HL", MicroOp.FETCH_HL, MicroOp.STORE_SP);
        codeTableMain.define(0xFA, "jp m,nn", MicroOp.FETCH_16_ADDRESS, MicroOp.JP_M);
        codeTableMain.define(0xFB, "EI", MicroOp.EI);
        codeTableMain.define(0xFC, "call m,nn", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLM);
        codeTableMain.define(0xFD, "Prefix FD", MicroOp.PREFIX_FD);
        codeTableMain.define(0xFE, "CP d8", MicroOp.FETCH_8, MicroOp.CP);
        codeTableMain.define(0xFF, "RST 38H", MicroOp.RST_38H);
    }


}
