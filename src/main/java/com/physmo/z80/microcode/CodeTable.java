package com.physmo.z80.microcode;

import com.physmo.z80.Utils;

import java.util.HashMap;
import java.util.Map;


public class CodeTable {

    MicroOp[][] n = new MicroOp[500][];
    Map<Integer, String> names = new HashMap<>();
    Map<String, Integer> opCodeLookup = new HashMap<>();
    String name;

    public CodeTable(String name) {
        this.name = name;
        for (int i = 0; i < n.length; i++) {
            n[i] = new MicroOp[]{MicroOp.TODO};
        }
    }

    public void define(int opCode, String name, MicroOp... microcodes) {
        n[opCode] = microcodes;
        names.put(opCode, name);
        opCodeLookup.put(name, opCode);
    }

    public MicroOp[] getInstructionCode(int instruction) {
        if (n[instruction].length == 1 && n[instruction][0] == MicroOp.TODO) {
            throw new RuntimeException("Instruction " + Utils.toHex2(instruction) + " not defined");
        }
        return n[instruction];
    }

    public String getInstructionName(int instruction) {
        return names.get(instruction);
    }

    public int getOpcode(String name) {
        if (!opCodeLookup.containsKey(name)) {
            throw new RuntimeException("Opcode not found: " + name);
        }
        return opCodeLookup.get(name);
    }

}
