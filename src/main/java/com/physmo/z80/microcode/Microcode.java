package com.physmo.z80.microcode;

import java.util.HashMap;
import java.util.Map;


public class Microcode {

    MicroOp[][] n = new MicroOp[500][];
    Map<Integer, String> names = new HashMap<>();

    public Microcode() {
        for (int i = 0; i < n.length; i++) {
            n[i] = new MicroOp[]{MicroOp.TODO};
        }

        define(0x00, "NOP", MicroOp.NOP);
        define(0x01, "LD BC,d16", MicroOp.FETCH_16, MicroOp.STORE_BC);
        define(0x02, "LD (BC),A", MicroOp.FETCH_A, MicroOp.SET_ADDR_FROM_BC, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x03, "INC BC", MicroOp.FETCH_BC, MicroOp.INC_16, MicroOp.STORE_BC);
        define(0x04, "INC B", MicroOp.FETCH_B, MicroOp.INC_8, MicroOp.STORE_B);
        define(0x05, "DEC B", MicroOp.FETCH_B, MicroOp.DEC_8, MicroOp.STORE_B);
        define(0x06, "LD B,d8", MicroOp.FETCH_8, MicroOp.STORE_B);
        define(0x07, "RLCA", MicroOp.RLCA); // 8-bit rotation left
        define(0x08, "LD (a16),SP", MicroOp.FETCH_SP, MicroOp.FETCH_16_ADDRESS, MicroOp.STORE_p16WORD);

        define(0x09, "ADD HL,BC", MicroOp.FETCH_BC, MicroOp.ADD_HL, MicroOp.STORE_HL);
        define(0x0A, "LD A,(BC)", MicroOp.SET_ADDR_FROM_BC, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
        define(0x0B, "DEC BC", MicroOp.FETCH_BC, MicroOp.DEC_16, MicroOp.STORE_BC);
        define(0x0C, "INC C", MicroOp.FETCH_C, MicroOp.INC_8, MicroOp.STORE_C);
        define(0x0D, "DEC C", MicroOp.FETCH_C, MicroOp.DEC_8, MicroOp.STORE_C);
        define(0x0E, "LD C,d8", MicroOp.FETCH_8, MicroOp.STORE_C);
        define(0x0F, "RRCA", MicroOp.RRCA);

        define(0x10, "STOP", MicroOp.STOP);

        define(0x11, "LD DE,d16", MicroOp.FETCH_16, MicroOp.STORE_DE);
        define(0x12, "LD (DE),A", MicroOp.FETCH_A, MicroOp.SET_ADDR_FROM_DE, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x13, "INC DE", MicroOp.FETCH_DE, MicroOp.INC_16, MicroOp.STORE_DE);
        define(0x14, "INC D", MicroOp.FETCH_D, MicroOp.INC_8, MicroOp.STORE_D);
        define(0x15, "DEC D", MicroOp.FETCH_D, MicroOp.DEC_8, MicroOp.STORE_D);
        define(0x16, "LD D,d8", MicroOp.FETCH_8, MicroOp.STORE_D);
        define(0x17, "RLA", MicroOp.FETCH_A, MicroOp.RL, MicroOp.STORE_A);
        define(0x18, "JR r8", MicroOp.FETCH_8, MicroOp.JR);
        define(0x19, "ADD HL,DE", MicroOp.FETCH_DE, MicroOp.ADD_HL, MicroOp.STORE_HL);
        define(0x1A, "LD A,(DE)", MicroOp.SET_ADDR_FROM_DE, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
        define(0x1B, "DEC DE", MicroOp.FETCH_DE, MicroOp.DEC_16, MicroOp.STORE_DE);
        define(0x1C, "INC E", MicroOp.FETCH_E, MicroOp.INC_8, MicroOp.STORE_E);
        define(0x1D, "DEC E", MicroOp.FETCH_E, MicroOp.DEC_8, MicroOp.STORE_E);
        define(0x1E, "LD E,d8", MicroOp.FETCH_8, MicroOp.STORE_E);
        define(0x1F, "RRA", MicroOp.RRA);

        define(0x20, "JR NZ,r8", MicroOp.FETCH_8, MicroOp.JRNZ);
        define(0x21, "LD HL,d16", MicroOp.FETCH_16, MicroOp.STORE_HL);
        define(0x22, "LD (HL+),A", MicroOp.FETCH_A, MicroOp.SET_ADDR_FROM_HL_INC, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x23, "INC HL", MicroOp.FETCH_HL, MicroOp.INC_16, MicroOp.STORE_HL);
        define(0x24, "INC H", MicroOp.FETCH_H, MicroOp.INC_8, MicroOp.STORE_H);
        define(0x25, "DEC H", MicroOp.FETCH_H, MicroOp.DEC_8, MicroOp.STORE_H);
        define(0x26, "LD H,d8", MicroOp.FETCH_8, MicroOp.STORE_H);
        define(0x27, "DAA", MicroOp.DAA);
        define(0x28, "JR Z,r8", MicroOp.FETCH_8, MicroOp.JRZ);
        define(0x29, "ADD HL,HL", MicroOp.FETCH_HL, MicroOp.ADD_HL, MicroOp.STORE_HL);
        define(0x2A, "LD A,(HL)", MicroOp.SET_ADDR_FROM_HL_INC, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
        define(0x2B, "DEC HL", MicroOp.FETCH_HL, MicroOp.DEC_16, MicroOp.STORE_HL);
        define(0x2C, "INC L", MicroOp.FETCH_L, MicroOp.INC_8, MicroOp.STORE_L);
        define(0x2D, "DEC L", MicroOp.FETCH_L, MicroOp.DEC_8, MicroOp.STORE_L);
        define(0x2E, "LD L,d8", MicroOp.FETCH_8, MicroOp.STORE_L);
        define(0x2F, "CPL", MicroOp.CPL);

        define(0x30, "JR NC,r8", MicroOp.FETCH_8, MicroOp.JRNC);
        define(0x31, "LD SP,d16", MicroOp.FETCH_16, MicroOp.STORE_SP);
        define(0x32, "LD (HL-),A", MicroOp.FETCH_A, MicroOp.SET_ADDR_FROM_HL_DEC, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x33, "INC SP", MicroOp.FETCH_SP, MicroOp.INC_16, MicroOp.STORE_SP);
        define(0x34, "INC (HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.INC_8, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x35, "DEC (HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.DEC_8, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x36, "LD (HL),d8", MicroOp.FETCH_8, MicroOp.SET_ADDR_FROM_HL, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x37, "SCF", MicroOp.SCF);
        define(0x38, "JR C,r8", MicroOp.FETCH_8, MicroOp.JRC);
        define(0x39, "ADD HL,SP", MicroOp.FETCH_SP, MicroOp.ADD_HL, MicroOp.STORE_HL);
        define(0x3A, "LD A,(HL-)", MicroOp.SET_ADDR_FROM_HL_DEC, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
        define(0x3B, "DEC SP", MicroOp.FETCH_SP, MicroOp.DEC_16, MicroOp.STORE_SP);
        define(0x3C, "INC A", MicroOp.FETCH_A, MicroOp.INC_8, MicroOp.STORE_A);
        define(0x3D, "DEC A", MicroOp.FETCH_A, MicroOp.DEC_8, MicroOp.STORE_A);
        define(0x3E, "LD A,d8", MicroOp.FETCH_8, MicroOp.STORE_A);
        define(0x3F, "CCF", MicroOp.CCF);


        define(0x40, "LD B,B", MicroOp.FETCH_B, MicroOp.STORE_B);
        define(0x41, "LD B,C", MicroOp.FETCH_C, MicroOp.STORE_B);
        define(0x42, "LD B,D", MicroOp.FETCH_D, MicroOp.STORE_B);
        define(0x43, "LD B,E", MicroOp.FETCH_E, MicroOp.STORE_B);
        define(0x44, "LD B,H", MicroOp.FETCH_H, MicroOp.STORE_B);
        define(0x45, "LD B,L", MicroOp.FETCH_L, MicroOp.STORE_B);
        define(0x46, "LD B,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_B);
        define(0x47, "LD B,A", MicroOp.FETCH_A, MicroOp.STORE_B);
        define(0x48, "LD C,B", MicroOp.FETCH_B, MicroOp.STORE_C);
        define(0x49, "LD C,C", MicroOp.FETCH_C, MicroOp.STORE_C);
        define(0x4A, "LD C,D", MicroOp.FETCH_D, MicroOp.STORE_C);
        define(0x4B, "LD C,E", MicroOp.FETCH_E, MicroOp.STORE_C);
        define(0x4C, "LD C,H", MicroOp.FETCH_H, MicroOp.STORE_C);
        define(0x4D, "LD C,L", MicroOp.FETCH_L, MicroOp.STORE_C);
        define(0x4E, "LD C,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_C);
        define(0x4F, "LD C,A", MicroOp.FETCH_A, MicroOp.STORE_C);

        define(0x50, "LD D,B", MicroOp.FETCH_B, MicroOp.STORE_D);
        define(0x51, "LD D,C", MicroOp.FETCH_C, MicroOp.STORE_D);
        define(0x52, "LD D,D", MicroOp.FETCH_D, MicroOp.STORE_D);
        define(0x53, "LD D,E", MicroOp.FETCH_E, MicroOp.STORE_D);
        define(0x54, "LD D,H", MicroOp.FETCH_H, MicroOp.STORE_D);
        define(0x55, "LD D,L", MicroOp.FETCH_L, MicroOp.STORE_D);
        define(0x56, "LD D,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_D);
        define(0x57, "LD D,A", MicroOp.FETCH_A, MicroOp.STORE_D);
        define(0x58, "LD E,B", MicroOp.FETCH_B, MicroOp.STORE_E);
        define(0x59, "LD E,C", MicroOp.FETCH_C, MicroOp.STORE_E);
        define(0x5A, "LD E,D", MicroOp.FETCH_D, MicroOp.STORE_E);
        define(0x5B, "LD E,E", MicroOp.FETCH_E, MicroOp.STORE_E);
        define(0x5C, "LD E,H", MicroOp.FETCH_H, MicroOp.STORE_E);
        define(0x5D, "LD E,L", MicroOp.FETCH_L, MicroOp.STORE_E);
        define(0x5E, "LD E,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_E);
        define(0x5F, "LD E,A", MicroOp.FETCH_A, MicroOp.STORE_E);

        define(0x60, "LD H,B", MicroOp.FETCH_B, MicroOp.STORE_H);
        define(0x61, "LD H,C", MicroOp.FETCH_C, MicroOp.STORE_H);
        define(0x62, "LD H,D", MicroOp.FETCH_D, MicroOp.STORE_H);
        define(0x63, "LD H,E", MicroOp.FETCH_E, MicroOp.STORE_H);
        define(0x64, "LD H,H", MicroOp.FETCH_H, MicroOp.STORE_H);
        define(0x65, "LD H,L", MicroOp.FETCH_L, MicroOp.STORE_H);
        define(0x66, "LD H,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_H);
        define(0x67, "LD H,A", MicroOp.FETCH_A, MicroOp.STORE_H);
        define(0x68, "LD L,B", MicroOp.FETCH_B, MicroOp.STORE_L);
        define(0x69, "LD L,C", MicroOp.FETCH_C, MicroOp.STORE_L);
        define(0x6A, "LD L,D", MicroOp.FETCH_D, MicroOp.STORE_L);
        define(0x6B, "LD L,E", MicroOp.FETCH_E, MicroOp.STORE_L);
        define(0x6C, "LD L,H", MicroOp.FETCH_H, MicroOp.STORE_L);
        define(0x6D, "LD L,L", MicroOp.FETCH_L, MicroOp.STORE_L);
        define(0x6E, "LD L,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_L);
        define(0x6F, "LD L,A", MicroOp.FETCH_A, MicroOp.STORE_L);

        define(0x70, "LD (HL),B", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_B, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x71, "LD (HL),C", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_C, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x72, "LD (HL),D", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_D, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x73, "LD (HL),E", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_E, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x74, "LD (HL),H", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_H, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x75, "LD (HL),L", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_L, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x76, "HALT", MicroOp.HALT);
        define(0x77, "LD (HL),A", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_A, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0x78, "LD A,B", MicroOp.FETCH_B, MicroOp.STORE_A);
        define(0x79, "LD A,C", MicroOp.FETCH_C, MicroOp.STORE_A);
        define(0x7A, "LD A,D", MicroOp.FETCH_D, MicroOp.STORE_A);
        define(0x7B, "LD A,E", MicroOp.FETCH_E, MicroOp.STORE_A);
        define(0x7C, "LD A,H", MicroOp.FETCH_H, MicroOp.STORE_A);
        define(0x7D, "LD A,L", MicroOp.FETCH_L, MicroOp.STORE_A);
        define(0x7E, "LD A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
        define(0x7F, "LD A,A", MicroOp.FETCH_A, MicroOp.STORE_A);

        define(0x80, "ADD A,B", MicroOp.FETCH_B, MicroOp.ADD);
        define(0x81, "ADD A,C", MicroOp.FETCH_C, MicroOp.ADD);
        define(0x82, "ADD A,D", MicroOp.FETCH_D, MicroOp.ADD);
        define(0x83, "ADD A,E", MicroOp.FETCH_E, MicroOp.ADD);
        define(0x84, "ADD A,H", MicroOp.FETCH_H, MicroOp.ADD);
        define(0x85, "ADD A,L", MicroOp.FETCH_L, MicroOp.ADD);
        define(0x86, "ADD A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.ADD);
        define(0x87, "ADD A,A", MicroOp.FETCH_A, MicroOp.ADD);
        define(0x88, "ADC A,B", MicroOp.FETCH_B, MicroOp.ADC);
        define(0x89, "ADC A,C", MicroOp.FETCH_C, MicroOp.ADC);
        define(0x8A, "ADC A,D", MicroOp.FETCH_D, MicroOp.ADC);
        define(0x8B, "ADC A,E", MicroOp.FETCH_E, MicroOp.ADC);
        define(0x8C, "ADC A,H", MicroOp.FETCH_H, MicroOp.ADC);
        define(0x8D, "ADC A,L", MicroOp.FETCH_L, MicroOp.ADC);
        define(0x8E, "ADC A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.ADC);
        define(0x8F, "ADC A,A", MicroOp.FETCH_A, MicroOp.ADC);

        define(0x90, "SUB A,B", MicroOp.FETCH_B, MicroOp.SUB);
        define(0x91, "SUB A,C", MicroOp.FETCH_C, MicroOp.SUB);
        define(0x92, "SUB A,D", MicroOp.FETCH_D, MicroOp.SUB);
        define(0x93, "SUB A,E", MicroOp.FETCH_E, MicroOp.SUB);
        define(0x94, "SUB A,H", MicroOp.FETCH_H, MicroOp.SUB);
        define(0x95, "SUB A,L", MicroOp.FETCH_L, MicroOp.SUB);
        define(0x96, "SUB A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.SUB);
        define(0x97, "SUB A,A", MicroOp.FETCH_A, MicroOp.SUB);
        define(0x98, "SBC A,B", MicroOp.FETCH_B, MicroOp.SBC);
        define(0x99, "SBC A,C", MicroOp.FETCH_C, MicroOp.SBC);
        define(0x9A, "SBC A,D", MicroOp.FETCH_D, MicroOp.SBC);
        define(0x9B, "SBC A,E", MicroOp.FETCH_E, MicroOp.SBC);
        define(0x9C, "SBC A,H", MicroOp.FETCH_H, MicroOp.SBC);
        define(0x9D, "SBC A,L", MicroOp.FETCH_L, MicroOp.SBC);
        define(0x9E, "SBC A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.SBC);
        define(0x9F, "SBC A,A", MicroOp.FETCH_A, MicroOp.SBC);


        define(0xA0, "AND A,B", MicroOp.FETCH_B, MicroOp.AND);
        define(0xA1, "AND A,C", MicroOp.FETCH_C, MicroOp.AND);
        define(0xA2, "AND A,D", MicroOp.FETCH_D, MicroOp.AND);
        define(0xA3, "AND A,E", MicroOp.FETCH_E, MicroOp.AND);
        define(0xA4, "AND A,H", MicroOp.FETCH_H, MicroOp.AND);
        define(0xA5, "AND A,L", MicroOp.FETCH_L, MicroOp.AND);
        define(0xA6, "AND A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.AND);
        define(0xA7, "AND A,A", MicroOp.FETCH_A, MicroOp.AND);
        define(0xA8, "XOR A,B", MicroOp.FETCH_B, MicroOp.XOR);
        define(0xA9, "XOR A,C", MicroOp.FETCH_C, MicroOp.XOR);
        define(0xAA, "XOR A,D", MicroOp.FETCH_D, MicroOp.XOR);
        define(0xAB, "XOR A,E", MicroOp.FETCH_E, MicroOp.XOR);
        define(0xAC, "XOR A,H", MicroOp.FETCH_H, MicroOp.XOR);
        define(0xAD, "XOR A,L", MicroOp.FETCH_L, MicroOp.XOR);
        define(0xAE, "XOR A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.XOR);
        define(0xAF, "XOR A,A", MicroOp.FETCH_A, MicroOp.XOR);

        define(0xB0, "OR A,B", MicroOp.FETCH_B, MicroOp.OR);
        define(0xB1, "OR A,C", MicroOp.FETCH_C, MicroOp.OR);
        define(0xB2, "OR A,D", MicroOp.FETCH_D, MicroOp.OR);
        define(0xB3, "OR A,E", MicroOp.FETCH_E, MicroOp.OR);
        define(0xB4, "OR A,H", MicroOp.FETCH_H, MicroOp.OR);
        define(0xB5, "OR A,L", MicroOp.FETCH_L, MicroOp.OR);
        define(0xB6, "OR A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.OR);
        define(0xB7, "OR A,A", MicroOp.FETCH_A, MicroOp.OR);
        define(0xB8, "CP A,B", MicroOp.FETCH_B, MicroOp.CP);
        define(0xB9, "CP A,C", MicroOp.FETCH_C, MicroOp.CP);
        define(0xBA, "CP A,D", MicroOp.FETCH_D, MicroOp.CP);
        define(0xBB, "CP A,E", MicroOp.FETCH_E, MicroOp.CP);
        define(0xBC, "CP A,H", MicroOp.FETCH_H, MicroOp.CP);
        define(0xBD, "CP A,L", MicroOp.FETCH_L, MicroOp.CP);
        define(0xBE, "CP A,(HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.CP);
        define(0xBF, "CP A,A", MicroOp.FETCH_A, MicroOp.CP);

        define(0xC0, "RET NZ", MicroOp.RETNZ);
        define(0xC1, "POP BC", MicroOp.POPW, MicroOp.STORE_BC);
        define(0xC2, "JP NZ,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.JPNZ);
        define(0xC3, "JP a16", MicroOp.FETCH_16_ADDRESS, MicroOp.JP);
        define(0xC4, "CALL NZ,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLNZ);
        define(0xC5, "PUSH BC", MicroOp.FETCH_BC, MicroOp.PUSHW);
        define(0xC6, "ADD A,d8", MicroOp.FETCH_8, MicroOp.ADD);
        define(0xC7, "RST 00H", MicroOp.RST_00H);
        define(0xC8, "RET Z", MicroOp.RETZ);
        define(0xC9, "RET", MicroOp.RET);
        define(0xCA, "JP Z,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.JPZ);
        define(0xCB, "PREFIX CB", MicroOp.FETCH_8, MicroOp.PREFIX_CB);
        define(0xCC, "CALL Z,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLZ);
        define(0xCD, "CALL a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALL);
        define(0xCE, "ADC A,d8", MicroOp.FETCH_8, MicroOp.ADC);
        define(0xCF, "RST 08H", MicroOp.RST_08H);

        define(0xD0, "RET NC", MicroOp.RETNC);
        define(0xD1, "POP DE", MicroOp.POPW, MicroOp.STORE_DE);
        define(0xD2, "JP NC,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.JPNC);
        define(0xD3, "NO OP", MicroOp.NOP);
        define(0xD4, "CALL NC,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLNC);
        define(0xD5, "PUSH DE", MicroOp.FETCH_DE, MicroOp.PUSHW);
        define(0xD6, "SUB d8", MicroOp.FETCH_8, MicroOp.SUB);
        define(0xD7, "RST 10H", MicroOp.RST_10H);
        define(0xD8, "RET C", MicroOp.RETC);
        define(0xD9, "RETI", MicroOp.RETI);
        define(0xDA, "JP C,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.JPC);
        define(0xDB, "NO OP", MicroOp.NOP);
        define(0xDC, "CALL C,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLC);
        define(0xDD, "NO OP", MicroOp.NOP);
        define(0xDE, "SBC A,d8", MicroOp.FETCH_8, MicroOp.SBC);
        define(0xDF, "RST 18H", MicroOp.RST_18H);

        define(0xE0, "LDH (a8),A", MicroOp.FETCH_8, MicroOp.LDZPGA);
        define(0xE1, "POP HL", MicroOp.POPW, MicroOp.STORE_HL);
        define(0xE2, "LD (C),A", MicroOp.FETCH_C, MicroOp.LDZPGA);
        define(0xE3, "NO OP", MicroOp.NOP);
        define(0xE4, "NO OP", MicroOp.NOP);
        define(0xE5, "PUSH HL", MicroOp.FETCH_HL, MicroOp.PUSHW);
        define(0xE6, "AND d8", MicroOp.FETCH_8, MicroOp.AND);
        define(0xE7, "RST 20H", MicroOp.RST_20H);
        define(0xE8, "ADD SP,r8", MicroOp.FETCH_8, MicroOp.ADDSPNN);
        define(0xE9, "JP (HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.JP);
        define(0xEA, "LD (a16),A", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_A, MicroOp.STORE_BYTE_AT_ADDRESS);
        define(0xEB, "NO OP", MicroOp.NOP);
        define(0xEC, "NO OP", MicroOp.NOP);
        define(0xED, "NO OP", MicroOp.NOP);
        define(0xEE, "XOR d8", MicroOp.FETCH_8, MicroOp.XOR);
        define(0xEF, "RST 28H", MicroOp.RST_28H);

        define(0xF0, "LDH A,(a8)", MicroOp.FETCH_8, MicroOp.FETCH_ZPG, MicroOp.STORE_A);  // *
        define(0xF1, "POP AF", MicroOp.POPW, MicroOp.STORE_AF);
        define(0xF2, "LD A,(C)", MicroOp.FETCH_C, MicroOp.FETCH_ZPG, MicroOp.STORE_A);
        define(0xF3, "DI", MicroOp.DI);
        define(0xF4, "NO OP", MicroOp.NOP);
        define(0xF5, "PUSH AF", MicroOp.FETCH_AF, MicroOp.PUSHW);
        define(0xF6, "OR d8", MicroOp.FETCH_8, MicroOp.OR);
        define(0xF7, "RST 30H", MicroOp.RST_30H);
        define(0xF8, "LD HL,SP+r8", MicroOp.FETCH_8, MicroOp.LDHLSPN);
        define(0xF9, "LD SP,HL", MicroOp.FETCH_HL, MicroOp.STORE_SP);
        define(0xFA, "LD A,(a16)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
        define(0xFB, "EI", MicroOp.EI);
        define(0xFC, "NO OP", MicroOp.NOP);
        define(0xFD, "NO OP", MicroOp.NOP);
        define(0xFE, "CP d8", MicroOp.FETCH_8, MicroOp.CP);
        define(0xFF, "RST 38H", MicroOp.RST_38H);
    }

    public void define(int opCode, String name, MicroOp... microcodes) {
        n[opCode] = microcodes;
        names.put(opCode, name);
    }

    public MicroOp[] getInstructionCode(int instruction) {
        return n[instruction];
    }

    public String getInstructionName(int instruction) {
        return names.get(instruction);
    }


}
