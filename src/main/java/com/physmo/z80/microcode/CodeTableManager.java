package com.physmo.z80.microcode;

public class CodeTableManager {
    public CodeTable codeTableMain;
    public CodeTable codeTableED; // Misc Instructions - prefix ED
    public CodeTable codeTableFD; // IY Instructions (FD)
    public CodeTable codeTableFDCB; // IY Bit Instructions (FDCB)
    public CodeTable codeTableCB; // Bit instructions

    public CodeTableManager() {
        codeTableMain = new CodeTable("Main");
        codeTableED = new CodeTable("ED");
        codeTableFD = new CodeTable("FD");
        codeTableFDCB = new CodeTable("FDCB");
        codeTableCB = new CodeTable("CB");

        initCodeTableMain();
        initCodeTableED();
        initCodeTableFD();
        initCodeTableFDCB();
        initCodeTableCB();
    }

    public void initCodeTableCB() {
        codeTableCB.define(0x07, "rlc a", MicroOp.FETCH_A, MicroOp.RLC, MicroOp.STORE_A);

        codeTableCB.define(0x3C, "srl h", MicroOp.FETCH_H, MicroOp.SRL, MicroOp.STORE_H);

        codeTableCB.define(0x6F, "bit 5,a", MicroOp.FETCH_A, MicroOp.BIT5);

        codeTableCB.define(0x7E, "bit 7,(hl)", MicroOp.FETCH_pHL, MicroOp.BIT7);

        codeTableCB.define(0x80, "res 0,b", MicroOp.FETCH_B, MicroOp.RES0, MicroOp.STORE_B);
        codeTableCB.define(0x81, "res 0,c", MicroOp.FETCH_C, MicroOp.RES0, MicroOp.STORE_C);
        codeTableCB.define(0x82, "res 0,d", MicroOp.FETCH_D, MicroOp.RES0, MicroOp.STORE_D);
        codeTableCB.define(0x83, "res 0,e", MicroOp.FETCH_E, MicroOp.RES0, MicroOp.STORE_E);
        codeTableCB.define(0x84, "res 0,h", MicroOp.FETCH_H, MicroOp.RES0, MicroOp.STORE_H);
        codeTableCB.define(0x85, "res 0,l", MicroOp.FETCH_L, MicroOp.RES0, MicroOp.STORE_L);

        codeTableCB.define(0x86, "res 0,(hl)", MicroOp.FETCH_pHL, MicroOp.RES0, MicroOp.STORE_pHL);
        codeTableCB.define(0x96, "res 2,(hl)", MicroOp.FETCH_pHL, MicroOp.RES2, MicroOp.STORE_pHL);

        codeTableCB.define(0xC6, "set 0,(hl)", MicroOp.FETCH_pHL, MicroOp.SET0, MicroOp.STORE_pHL);
        codeTableCB.define(0xBC, "res 7,h", MicroOp.FETCH_H, MicroOp.RES7, MicroOp.STORE_H);
        codeTableCB.define(0xAE, "res 5,(hl)", MicroOp.FETCH_pHL, MicroOp.RES5, MicroOp.STORE_pHL);
        codeTableCB.define(0xFD, "set 7,l", MicroOp.FETCH_L, MicroOp.SET7, MicroOp.STORE_L);
    }

    public void initCodeTableFDCB() {
        codeTableFDCB.define(0x00, "rlc (iy+d),b", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RLC, MicroOp.STORE_B);
        codeTableFDCB.define(0x01, "rlc (iy+d),c", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RLC, MicroOp.STORE_C);
        codeTableFDCB.define(0x02, "rlc (iy+d),d", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RLC, MicroOp.STORE_D);
        codeTableFDCB.define(0x03, "rlc (iy+d),e", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RLC, MicroOp.STORE_E);
        codeTableFDCB.define(0x04, "rlc (iy+d),h", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RLC, MicroOp.STORE_H);
        codeTableFDCB.define(0x05, "rlc (iy+d),l", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RLC, MicroOp.STORE_L);
        codeTableFDCB.define(0x06, "rlc (iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RLC); // Note: I don't know if this should modify memory?
        codeTableFDCB.define(0x07, "rlc (iy+d),a", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RLC, MicroOp.STORE_A);

        codeTableFDCB.define(0x08, "rrc (iy+d),b", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RRC, MicroOp.STORE_B);
        codeTableFDCB.define(0x09, "rrc (iy+d),c", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RRC, MicroOp.STORE_C);
        codeTableFDCB.define(0x0A, "rrc (iy+d),d", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RRC, MicroOp.STORE_D);
        codeTableFDCB.define(0x0B, "rrc (iy+d),e", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RRC, MicroOp.STORE_E);
        codeTableFDCB.define(0x0C, "rrc (iy+d),h", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RRC, MicroOp.STORE_H);
        codeTableFDCB.define(0x0D, "rrc (iy+d),l", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RRC, MicroOp.STORE_L);
        codeTableFDCB.define(0x0E, "rrc (iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RRC); // Note: I don't know if this should modify memory?
        codeTableFDCB.define(0x0F, "rrc (iy+d),a", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RRC, MicroOp.STORE_A);

        codeTableFDCB.define(0x10, "rl (iy+d),b", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RL, MicroOp.STORE_B);
        codeTableFDCB.define(0x11, "rl (iy+d),c", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RL, MicroOp.STORE_C);
        codeTableFDCB.define(0x12, "rl (iy+d),d", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RL, MicroOp.STORE_D);
        codeTableFDCB.define(0x13, "rl (iy+d),e", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RL, MicroOp.STORE_E);
        codeTableFDCB.define(0x14, "rl (iy+d),h", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RL, MicroOp.STORE_H);
        codeTableFDCB.define(0x15, "rl (iy+d),l", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RL, MicroOp.STORE_L);
        codeTableFDCB.define(0x16, "rl (iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RL); // Note: I don't know if this should modify memory?
        codeTableFDCB.define(0x17, "rl (iy+d),a", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RL, MicroOp.STORE_A);

        codeTableFDCB.define(0x18, "rr (iy+d),b", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RR, MicroOp.STORE_B);
        codeTableFDCB.define(0x19, "rr (iy+d),c", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RR, MicroOp.STORE_C);
        codeTableFDCB.define(0x1A, "rr (iy+d),d", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RR, MicroOp.STORE_D);
        codeTableFDCB.define(0x1B, "rr (iy+d),e", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RR, MicroOp.STORE_E);
        codeTableFDCB.define(0x1C, "rr (iy+d),h", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RR, MicroOp.STORE_H);
        codeTableFDCB.define(0x1D, "rr (iy+d),l", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RR, MicroOp.STORE_L);
        codeTableFDCB.define(0x1E, "rr (iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RR); // Note: I don't know if this should modify memory?
        codeTableFDCB.define(0x1F, "rr (iy+d),a", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RR, MicroOp.STORE_A);

        codeTableFDCB.define(0x20, "sla (iy+d),b", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLA, MicroOp.STORE_B);
        codeTableFDCB.define(0x21, "sla (iy+d),c", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLA, MicroOp.STORE_C);
        codeTableFDCB.define(0x22, "sla (iy+d),d", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLA, MicroOp.STORE_D);
        codeTableFDCB.define(0x23, "sla (iy+d),e", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLA, MicroOp.STORE_E);
        codeTableFDCB.define(0x24, "sla (iy+d),h", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLA, MicroOp.STORE_H);
        codeTableFDCB.define(0x25, "sla (iy+d),l", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLA, MicroOp.STORE_L);
        codeTableFDCB.define(0x26, "sla (iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLA); // Note: I don't know if this should modify memory?
        codeTableFDCB.define(0x27, "sla (iy+d),a", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLA, MicroOp.STORE_A);

        codeTableFDCB.define(0x28, "sra (iy+d),b", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRA, MicroOp.STORE_B);
        codeTableFDCB.define(0x29, "sra (iy+d),c", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRA, MicroOp.STORE_C);
        codeTableFDCB.define(0x2A, "sra (iy+d),d", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRA, MicroOp.STORE_D);
        codeTableFDCB.define(0x2B, "sra (iy+d),e", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRA, MicroOp.STORE_E);
        codeTableFDCB.define(0x2C, "sra (iy+d),h", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRA, MicroOp.STORE_H);
        codeTableFDCB.define(0x2D, "sra (iy+d),l", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRA, MicroOp.STORE_L);
        codeTableFDCB.define(0x2E, "sra (iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRA); // Note: I don't know if this should modify memory?
        codeTableFDCB.define(0x2F, "sra (iy+d),a", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRA, MicroOp.STORE_A);

        codeTableFDCB.define(0x30, "SLL (iy+d),b", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLL, MicroOp.STORE_B);
        codeTableFDCB.define(0x31, "SLL (iy+d),c", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLL, MicroOp.STORE_C);
        codeTableFDCB.define(0x32, "SLL (iy+d),d", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLL, MicroOp.STORE_D);
        codeTableFDCB.define(0x33, "SLL (iy+d),e", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLL, MicroOp.STORE_E);
        codeTableFDCB.define(0x34, "SLL (iy+d),h", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLL, MicroOp.STORE_H);
        codeTableFDCB.define(0x35, "SLL (iy+d),l", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLL, MicroOp.STORE_L);
        codeTableFDCB.define(0x36, "SLL (iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLL); // Note: I don't know if this should modify memory?
        codeTableFDCB.define(0x37, "SLL (iy+d),a", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SLL, MicroOp.STORE_A);
        codeTableFDCB.define(0x38, "SRL (iy+d),b", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRL, MicroOp.STORE_B);
        codeTableFDCB.define(0x39, "SRL (iy+d),c", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRL, MicroOp.STORE_C);
        codeTableFDCB.define(0x3A, "SRL (iy+d),d", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRL, MicroOp.STORE_D);
        codeTableFDCB.define(0x3B, "SRL (iy+d),e", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRL, MicroOp.STORE_E);
        codeTableFDCB.define(0x3C, "SRL (iy+d),h", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRL, MicroOp.STORE_H);
        codeTableFDCB.define(0x3D, "SRL (iy+d),l", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRL, MicroOp.STORE_L);
        codeTableFDCB.define(0x3E, "SRL (iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRL); // Note: I don't know if this should modify memory?
        codeTableFDCB.define(0x3F, "SRL (iy+d),a", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SRL, MicroOp.STORE_A);

        codeTableFDCB.define(0x40, "BIT 0,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT0);
        codeTableFDCB.define(0x41, "BIT 0,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT0);
        codeTableFDCB.define(0x42, "BIT 0,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT0);
        codeTableFDCB.define(0x43, "BIT 0,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT0);
        codeTableFDCB.define(0x44, "BIT 0,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT0);
        codeTableFDCB.define(0x45, "BIT 0,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT0);
        codeTableFDCB.define(0x46, "BIT 0,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT0);
        codeTableFDCB.define(0x47, "BIT 0,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT0);

        codeTableFDCB.define(0x48, "BIT 1,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT1);
        codeTableFDCB.define(0x49, "BIT 1,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT1);
        codeTableFDCB.define(0x4A, "BIT 1,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT1);
        codeTableFDCB.define(0x4B, "BIT 1,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT1);
        codeTableFDCB.define(0x4C, "BIT 1,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT1);
        codeTableFDCB.define(0x4D, "BIT 1,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT1);
        codeTableFDCB.define(0x4E, "BIT 1,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT1);
        codeTableFDCB.define(0x4F, "BIT 1,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT1);


        codeTableFDCB.define(0x50, "bit 2,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT2);
        codeTableFDCB.define(0x51, "bit 2,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT2);
        codeTableFDCB.define(0x52, "bit 2,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT2);
        codeTableFDCB.define(0x53, "bit 2,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT2);
        codeTableFDCB.define(0x54, "bit 2,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT2);
        codeTableFDCB.define(0x55, "bit 2,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT2);
        codeTableFDCB.define(0x56, "bit 2,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT2);
        codeTableFDCB.define(0x57, "bit 2,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT2);

        codeTableFDCB.define(0x58, "bit 3,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT3);
        codeTableFDCB.define(0x59, "bit 3,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT3);
        codeTableFDCB.define(0x5A, "bit 3,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT3);
        codeTableFDCB.define(0x5B, "bit 3,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT3);
        codeTableFDCB.define(0x5C, "bit 3,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT3);
        codeTableFDCB.define(0x5D, "bit 3,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT3);
        codeTableFDCB.define(0x5E, "bit 3,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT3);
        codeTableFDCB.define(0x5F, "bit 3,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT3);

        codeTableFDCB.define(0x60, "bit 4,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT4);
        codeTableFDCB.define(0x61, "bit 4,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT4);
        codeTableFDCB.define(0x62, "bit 4,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT4);
        codeTableFDCB.define(0x63, "bit 4,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT4);
        codeTableFDCB.define(0x64, "bit 4,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT4);
        codeTableFDCB.define(0x65, "bit 4,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT4);
        codeTableFDCB.define(0x66, "bit 4,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT4);
        codeTableFDCB.define(0x67, "bit 4,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT4);

        codeTableFDCB.define(0x68, "bit 5,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT5);
        codeTableFDCB.define(0x69, "bit 5,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT5);
        codeTableFDCB.define(0x6A, "bit 5,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT5);
        codeTableFDCB.define(0x6B, "bit 5,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT5);
        codeTableFDCB.define(0x6C, "bit 5,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT5);
        codeTableFDCB.define(0x6D, "bit 5,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT5);
        codeTableFDCB.define(0x6E, "bit 5,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT5);
        codeTableFDCB.define(0x6F, "bit 5,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT5);

        codeTableFDCB.define(0x70, "bit 6,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT6);
        codeTableFDCB.define(0x71, "bit 6,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT6);
        codeTableFDCB.define(0x72, "bit 6,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT6);
        codeTableFDCB.define(0x73, "bit 6,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT6);
        codeTableFDCB.define(0x74, "bit 6,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT6);
        codeTableFDCB.define(0x75, "bit 6,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT6);
        codeTableFDCB.define(0x76, "bit 6,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT6);
        codeTableFDCB.define(0x77, "bit 6,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT6);

        codeTableFDCB.define(0x78, "bit 7,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT7);
        codeTableFDCB.define(0x79, "bit 7,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT7);
        codeTableFDCB.define(0x7A, "bit 7,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT7);
        codeTableFDCB.define(0x7B, "bit 7,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT7);
        codeTableFDCB.define(0x7C, "bit 7,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT7);
        codeTableFDCB.define(0x7D, "bit 7,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT7);
        codeTableFDCB.define(0x7E, "bit 7,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT7);
        codeTableFDCB.define(0x7F, "bit 7,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.BIT7);

        codeTableFDCB.define(0x80, "RES 0,(iy+d),b", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES0, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_B);
        codeTableFDCB.define(0x81, "RES 0,(iy+d),c", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES0, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_C);
        codeTableFDCB.define(0x82, "RES 0,(iy+d),d", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES0, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_D);
        codeTableFDCB.define(0x83, "RES 0,(iy+d),e", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES0, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_E);
        codeTableFDCB.define(0x84, "RES 0,(iy+d),h", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES0, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_H);
        codeTableFDCB.define(0x85, "RES 0,(iy+d),l", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES0, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_L);
        codeTableFDCB.define(0x86, "RES 0,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES0, MicroOp.STORE_pIY_D_FDCB);
        codeTableFDCB.define(0x87, "RES 0,(iy+d),a", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES0, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_A);

        codeTableFDCB.define(0x88, "RES 1,(iy+d),b", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES1, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_B);
        codeTableFDCB.define(0x89, "RES 1,(iy+d),c", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES1, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_C);
        codeTableFDCB.define(0x8A, "RES 1,(iy+d),d", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES1, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_D);
        codeTableFDCB.define(0x8B, "RES 1,(iy+d),e", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES1, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_E);
        codeTableFDCB.define(0x8C, "RES 1,(iy+d),h", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES1, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_H);
        codeTableFDCB.define(0x8D, "RES 1,(iy+d),l", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES1, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_L);
        codeTableFDCB.define(0x8E, "RES 1,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES1, MicroOp.STORE_pIY_D_FDCB);
        codeTableFDCB.define(0x8F, "RES 1,(iy+d),a", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES1, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_A);


        codeTableFDCB.define(0x90, "RES 2,(iy+d),b", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES2, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_B);
        codeTableFDCB.define(0x91, "RES 2,(iy+d),c", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES2, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_C);
        codeTableFDCB.define(0x92, "RES 2,(iy+d),d", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES2, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_D);
        codeTableFDCB.define(0x93, "RES 2,(iy+d),e", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES2, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_E);
        codeTableFDCB.define(0x94, "RES 2,(iy+d),h", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES2, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_H);
        codeTableFDCB.define(0x95, "RES 2,(iy+d),l", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES2, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_L);
        codeTableFDCB.define(0x96, "RES 2,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES2, MicroOp.STORE_pIY_D_FDCB);
        codeTableFDCB.define(0x97, "RES 2,(iy+d),a", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES2, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_A);

        codeTableFDCB.define(0x98, "RES 3,(iy+d),b", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES3, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_B);
        codeTableFDCB.define(0x99, "RES 3,(iy+d),c", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES3, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_C);
        codeTableFDCB.define(0x9A, "RES 3,(iy+d),d", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES3, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_D);
        codeTableFDCB.define(0x9B, "RES 3,(iy+d),e", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES3, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_E);
        codeTableFDCB.define(0x9C, "RES 3,(iy+d),h", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES3, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_H);
        codeTableFDCB.define(0x9D, "RES 3,(iy+d),l", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES3, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_L);
        codeTableFDCB.define(0x9E, "RES 3,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES3, MicroOp.STORE_pIY_D_FDCB);
        codeTableFDCB.define(0x9F, "RES 3,(iy+d),a", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES3, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_A);




        codeTableFDCB.define(0xA0, "RES 4,(iy+d),b", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES4, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_B);
        codeTableFDCB.define(0xA1, "RES 4,(iy+d),c", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES4, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_C);
        codeTableFDCB.define(0xA2, "RES 4,(iy+d),d", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES4, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_D);
        codeTableFDCB.define(0xA3, "RES 4,(iy+d),e", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES4, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_E);
        codeTableFDCB.define(0xA4, "RES 4,(iy+d),h", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES4, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_H);
        codeTableFDCB.define(0xA5, "RES 4,(iy+d),l", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES4, MicroOp.STORE_pIY_D_FDCB, MicroOp.STORE_L);
        codeTableFDCB.define(0xA6, "RES 4,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES4, MicroOp.STORE_pIY_D_FDCB);

        codeTableFDCB.define(0xAE, "RES 5,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES5, MicroOp.STORE_pIY_D_FDCB);


        codeTableFDCB.define(0xC6, "SET 0,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SET0, MicroOp.STORE_pIY_D_FDCB);


        codeTableFDCB.define(0x8E, "RES 1,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.RES1, MicroOp.STORE_pIY_D_FDCB);

        codeTableFDCB.define(0xCE, "SET 1,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SET1, MicroOp.STORE_pIY_D_FDCB);

        codeTableFDCB.define(0xE6, "SET 4,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SET4, MicroOp.STORE_pIY_D_FDCB);

        codeTableFDCB.define(0xEE, "set 5,(iy+d)", MicroOp.FETCH_pIY_D_FDCB, MicroOp.SET5, MicroOp.STORE_pIY_D_FDCB);



    }

    // IY Instructions (FD)
    public void initCodeTableFD() {
        // MicroOp.FETCH_BC, MicroOp.PUSHW
        codeTableFD.define(0x21, "ld iy,nn", MicroOp.FETCH_16, MicroOp.STORE_IY);
        codeTableFD.define(0x35, "dec (iy+d)", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_pIY_D_FDCB, MicroOp.DEC_8, MicroOp.STORE_pIY_D_FDCB);

        codeTableFD.define(0x36, "ld (iy+d),n", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_8, MicroOp.STORE_pIY_D_FDCB);

        codeTableFD.define(0x46, "ld b,(iy+d)", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_pIY_D_FDCB, MicroOp.STORE_B);

        codeTableFD.define(0x4E, "ld c,(iy+d)", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_pIY_D_FDCB, MicroOp.STORE_C);


        codeTableFD.define(0x6E, "ld l,(iy+d)", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_pIY_D_FDCB, MicroOp.STORE_L);

        codeTableFD.define(0x70, "ld (iy+d),b", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_B, MicroOp.STORE_pIY_D_FDCB);
        codeTableFD.define(0x71, "ld (iy+d),c", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_C, MicroOp.STORE_pIY_D_FDCB);
        codeTableFD.define(0x72, "ld (iy+d),d", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_D, MicroOp.STORE_pIY_D_FDCB);
        codeTableFD.define(0x73, "ld (iy+d),e", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_E, MicroOp.STORE_pIY_D_FDCB);
        codeTableFD.define(0x74, "ld (iy+d),h", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_H, MicroOp.STORE_pIY_D_FDCB);
        codeTableFD.define(0x75, "ld (iy+d),l", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_L, MicroOp.STORE_pIY_D_FDCB);
        codeTableFD.define(0x7B, "ld a,e", MicroOp.FETCH_E, MicroOp.STORE_A);
        codeTableFD.define(0x86, "add a,(iy+d)", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_pIY_D_FDCB, MicroOp.ADD);

        codeTableFD.define(0x96, "sub (iy+d)", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_pIY_D_FDCB, MicroOp.SUB);


        codeTableFD.define(0xE1, "pop iy", MicroOp.POPW, MicroOp.STORE_IY);
        codeTableFD.define(0xE5, "push iy", MicroOp.FETCH_IY, MicroOp.PUSHW);

        codeTableFD.define(0xBE, "cp (iy+d)", MicroOp.FETCH_BYTE_TO_D, MicroOp.FETCH_pIY_D_FDCB, MicroOp.CP);

        codeTableFD.define(0xCB, "IY bit", MicroOp.PREFIX_FD_CB);
    }

    public void initCodeTableED() {
        codeTableED.define(0x42, "sbc hl,bc", MicroOp.FETCH_BC, MicroOp.SBC_HL);
        codeTableED.define(0x43, "ld (nn),bc", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_BC, MicroOp.STORE_WORD_AT_ADDRESS);
        codeTableED.define(0x47, "LD i,a", MicroOp.FETCH_A, MicroOp.STORE_I);

        codeTableED.define(0x4B, "ld bc,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_BC);

        codeTableED.define(0x52, "sbc hl,de", MicroOp.FETCH_DE, MicroOp.SBC_HL);
        codeTableED.define(0x53, "ld (nn),de", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_DE, MicroOp.STORE_WORD_AT_ADDRESS);


        codeTableED.define(0x5B, "ld de,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_DE);

        codeTableED.define(0x62, "sbc hl,hl", MicroOp.FETCH_HL, MicroOp.SBC_HL);
        codeTableED.define(0x6B, "ld hl,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_HL);

        codeTableED.define(0x72, "sbc hl,sp", MicroOp.FETCH_SP, MicroOp.SBC_HL);

        codeTableED.define(0x73, "ld (nn),sp", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_SP, MicroOp.STORE_WORD_AT_ADDRESS);

        codeTableED.define(0x78, "in a,(c)", MicroOp.FETCH_PORT_C, MicroOp.STORE_A);


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
        codeTableMain.define(0x07, "RLCA", MicroOp.FETCH_A, MicroOp.RLC, MicroOp.STORE_A); // 8-bit rotation left
        codeTableMain.define(0x08, "EX AF,AF'", MicroOp.EX_AF_AF);
        codeTableMain.define(0x09, "ADD HL,BC", MicroOp.FETCH_BC, MicroOp.ADD_HL, MicroOp.STORE_HL);
        codeTableMain.define(0x0A, "LD A,(BC)", MicroOp.SET_ADDR_FROM_BC, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
        codeTableMain.define(0x0B, "DEC BC", MicroOp.FETCH_BC, MicroOp.DEC_16, MicroOp.STORE_BC);
        codeTableMain.define(0x0C, "INC C", MicroOp.FETCH_C, MicroOp.INC_8, MicroOp.STORE_C);
        codeTableMain.define(0x0D, "DEC C", MicroOp.FETCH_C, MicroOp.DEC_8, MicroOp.STORE_C);
        codeTableMain.define(0x0E, "LD C,d8", MicroOp.FETCH_8, MicroOp.STORE_C);
        codeTableMain.define(0x0F, "RRCA", MicroOp.FETCH_A, MicroOp.RRC, MicroOp.STORE_A);


        codeTableMain.define(0x10, "DJNZ d", MicroOp.FETCH_8, MicroOp.DJNZ);
        codeTableMain.define(0x11, "LD DE,d16", MicroOp.FETCH_16, MicroOp.STORE_DE);
        codeTableMain.define(0x12, "LD (DE),A", MicroOp.FETCH_A, MicroOp.SET_ADDR_FROM_DE, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x13, "INC DE", MicroOp.FETCH_DE, MicroOp.INC_16, MicroOp.STORE_DE);
        codeTableMain.define(0x14, "INC D", MicroOp.FETCH_D, MicroOp.INC_8, MicroOp.STORE_D);
        codeTableMain.define(0x15, "DEC D", MicroOp.FETCH_D, MicroOp.DEC_8, MicroOp.STORE_D);
        codeTableMain.define(0x16, "LD D,d8", MicroOp.FETCH_8, MicroOp.STORE_D);
        codeTableMain.define(0x17, "RLA", MicroOp.FETCH_A, MicroOp.RL, MicroOp.STORE_A);
        codeTableMain.define(0x18, "JR r8", MicroOp.FETCH_8, MicroOp.JR);
        codeTableMain.define(0x19, "ADD HL,DE", MicroOp.FETCH_DE, MicroOp.ADD_HL, MicroOp.STORE_HL);
        codeTableMain.define(0x1A, "LD A,(DE)", MicroOp.SET_ADDR_FROM_DE, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
        codeTableMain.define(0x1B, "DEC DE", MicroOp.FETCH_DE, MicroOp.DEC_16, MicroOp.STORE_DE);
        codeTableMain.define(0x1C, "INC E", MicroOp.FETCH_E, MicroOp.INC_8, MicroOp.STORE_E);
        codeTableMain.define(0x1D, "DEC E", MicroOp.FETCH_E, MicroOp.DEC_8, MicroOp.STORE_E);
        codeTableMain.define(0x1E, "LD E,d8", MicroOp.FETCH_8, MicroOp.STORE_E);
        codeTableMain.define(0x1F, "RRA", MicroOp.FETCH_A, MicroOp.RR, MicroOp.STORE_A);

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
        codeTableMain.define(0xDB, "NO OP", MicroOp.NOP);
        codeTableMain.define(0xDC, "CALL C,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLC);
        codeTableMain.define(0xDD, "NO OP", MicroOp.NOP);
        codeTableMain.define(0xDE, "SBC A,d8", MicroOp.FETCH_8, MicroOp.SBC);
        codeTableMain.define(0xDF, "RST 18H", MicroOp.RST_18H);

        codeTableMain.define(0xE0, "RET PO", MicroOp.RET_PO);
        codeTableMain.define(0xE1, "POP HL", MicroOp.POPW, MicroOp.STORE_HL);
        codeTableMain.define(0xE2, "JP PO,NN", MicroOp.FETCH_16_ADDRESS, MicroOp.JP_PO);
        codeTableMain.define(0xE3, "ex (sp),hl", MicroOp.EX_SP_HL);
        codeTableMain.define(0xE4, "NO OP", MicroOp.NOP);
        codeTableMain.define(0xE5, "PUSH HL", MicroOp.FETCH_HL, MicroOp.PUSHW);
        codeTableMain.define(0xE6, "AND d8", MicroOp.FETCH_8, MicroOp.AND);
        codeTableMain.define(0xE7, "RST 20H", MicroOp.RST_20H);
        codeTableMain.define(0xE8, "ADD SP,r8", MicroOp.FETCH_8, MicroOp.ADDSPNN);
        codeTableMain.define(0xE9, "JP (HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.JP);
        codeTableMain.define(0xEA, "JP PE,NN", MicroOp.FETCH_16_ADDRESS, MicroOp.JP_PE);
        codeTableMain.define(0xEB, "EX DE, HL", MicroOp.EX_DE_HL);
        codeTableMain.define(0xEC, "NO OP", MicroOp.NOP);
        codeTableMain.define(0xED, "Prefix ED", MicroOp.PREFIX_ED);
        codeTableMain.define(0xEE, "XOR d8", MicroOp.FETCH_8, MicroOp.XOR);
        codeTableMain.define(0xEF, "RST 28H", MicroOp.RST_28H);

        codeTableMain.define(0xF0, "RET P", MicroOp.RET_P);
        codeTableMain.define(0xF1, "POP AF", MicroOp.POPW, MicroOp.STORE_AF);
        codeTableMain.define(0xF2, "JP P,NN", MicroOp.FETCH_16_ADDRESS, MicroOp.JP_P);
        codeTableMain.define(0xF3, "DI", MicroOp.DI);
        codeTableMain.define(0xF4, "NO OP", MicroOp.NOP);
        codeTableMain.define(0xF5, "PUSH AF", MicroOp.FETCH_AF, MicroOp.PUSHW);
        codeTableMain.define(0xF6, "OR d8", MicroOp.FETCH_8, MicroOp.OR);
        codeTableMain.define(0xF7, "RST 30H", MicroOp.RST_30H);
        codeTableMain.define(0xF8, "LD HL,SP+r8", MicroOp.FETCH_8, MicroOp.LDHLSPN);
        codeTableMain.define(0xF9, "LD SP,HL", MicroOp.FETCH_HL, MicroOp.STORE_SP);
        codeTableMain.define(0xFA, "jp m,nn", MicroOp.FETCH_16_ADDRESS, MicroOp.JP_M); // FIX Jump
        codeTableMain.define(0xFB, "EI", MicroOp.EI);
        codeTableMain.define(0xFC, "NO OP", MicroOp.NOP);
        codeTableMain.define(0xFD, "Prefix FD", MicroOp.PREFIX_FD);
        codeTableMain.define(0xFE, "CP d8", MicroOp.FETCH_8, MicroOp.CP);
        codeTableMain.define(0xFF, "RST 38H", MicroOp.RST_38H);
    }


}
