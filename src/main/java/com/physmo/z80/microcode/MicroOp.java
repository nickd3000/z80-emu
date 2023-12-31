package com.physmo.z80.microcode;

public enum MicroOp {
    TODO,
    NOP,
    PREFIX_CB,
    PREFIX_ED,
    PREFIX_FD,

    INC_8,      // INC temp (8 bit)
    INC_16,     // INC temp (16 bit)
    DEC_8,      // DEC temp (8 bit)
    DEC_16,     // DEC temp (16 bit)
    LD_8,
    LD_16,
    ADD_8,
    ADD_16,
    SUB_8,
    SUB_16,

    FETCH_A,     // Read register A to temp
    FETCH_B,     // Read register B to temp
    FETCH_C,     // Read register C to temp
    FETCH_D,     // Read register D to temp
    FETCH_E,     // Read register E to temp
    FETCH_H,     // Read register H to temp
    FETCH_L,     // Read register L to temp
    FETCH_F,     // Read register F to temp
    FETCH_I,     // Read register I to temp
    FETCH_R,     // Read register R to temp

    FETCH_8,     // Read next byte
    FETCH_16,    // Read next word

    FETCH_AF,    // Read register AF to temp
    FETCH_BC,    // Read register BC to temp
    FETCH_DE,    // Read register DE to temp
    FETCH_HL,    // Read register HL to temp
    FETCH_IX,    // Read register IX to temp
    FETCH_IY,    // Read register IX to temp
    FETCH_SP,    // Read Stack Pointer to temp
    FETCH_PC,    // Read Program Counter to temp
    FETCH_pBC,   // Read value in memory location BC to temp
    FETCH_pIY_D,    // Read value in memory location (IY + D) to temp

    FETCH_8_ADDRESS,    // Fetches the next 1 byte onto the lower end of the address bus
    FETCH_16_ADDRESS, // Fetches the next 2 bytes onto the Address bus
    FETCH_pHL,
    FETCH_BYTE_FROM_ADDR,

    STORE_A,    // Store temp in A
    STORE_B,    // Store temp in B
    STORE_C,    // Store temp in C
    STORE_D,    // Store temp in D
    STORE_E,    // Store temp in E
    STORE_H,    // Store temp in H
    STORE_L,    // Store temp in L
    STORE_I,    // Store temp in L
    STORE_BC,
    STORE_HL,
    STORE_p16WORD,
    STORE_DE,
    STORE_SP,
    STORE_IX,
    STORE_IY,
    STORE_pIY_D,


    ADD_HL,    // Add HL to temp


    RLCA,
    STOP,
    HALT,

    SET_ADDR_FROM_A,
    SET_ADDR_FROM_HL,

    SET_ADDR_FROM_BC,
    SET_ADDR_FROM_DE,
    STORE_BYTE_AT_ADDRESS,
    RRCA,
    SET_ADDR_FROM_HL_INC,
    SET_ADDR_FROM_HL_DEC,


    RLA,
    RL,

    RRA,
    DAA,
    CPL,

    SCF,
    CCF,

    ADD,
    ADC,
    SUB,
    SBC,
    CP,
    OR,
    XOR,
    AND,

    SBC_HL, // 16 bit version applied to HL

    RET, RETI, RETZ, RETNZ,
    RETNC, RETC, PUSHW,
    POPW,

    CALLNZ,

    // JUMPS
    JP, JRNZ, JRZ,
    JRNC, JRC, JR,
    JPZ, JPNZ, JPC, JPNC,

    // CALL
    CALLZ, CALLC, CALLNC, CALL,

    // PORT IN/OUT
    OUT,
    IN,

    // Reset vectors
    RST_00H, RST_08H,
    RST_10H, RST_18H,
    RST_20H, RST_28H,
    RST_30H, RST_38H,

    EXX,

    // Special ED commands
    LDI,
    CPI,
    INI,
    OUTI,
    LDD,
    CPD,
    IND,
    OUTD,
    // Repeating versions of the special ED commands
    LDIR,
    LDDR, // Data copying loop

    LDZPGA, ADDSPNN,
    FETCH_ZPG,
    STORE_AF,
    // Interrupt enable/disable
    DI, EI,
    IM_0, IM_1, IM_2,
    LDHLSPN, EX_DE_HL,
}


// 0x04     inc b   READ_B, INC_8, STORE_B