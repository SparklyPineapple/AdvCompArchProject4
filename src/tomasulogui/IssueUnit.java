package tomasulogui;

import tomasulogui.IssuedInst.INST_TYPE;

public class IssueUnit {

    private enum EXEC_TYPE {
        NONE, LOAD, ALU, MULT, DIV, BRANCH
    };

    PipelineSimulator simulator;
    IssuedInst issuee;
    Object fu;

    public IssueUnit(PipelineSimulator sim) {
        simulator = sim;
    }

    public void execCycle() {
        // an execution cycle involves:
        // 1. checking if ROB and Reservation Station avail
        // 2. issuing to reservation station, if no structural hazard

        // to issue, we make an IssuedInst, filling in what we know
        // We check the BTB, and put prediction if branch, updating PC
        //     if pred taken, incr PC otherwise
        // We then send this to the ROB, which fills in the data fields
        // We then check the CDB, and see if it is broadcasting data we need,
        //    so that we can forward during issue
        boolean isStall = false;
        Instruction memInstr = simulator.getMemory().getInstAtAddr(simulator.getPC());

        issuee = IssuedInst.createIssuedInst(memInstr);
        issuee.pc = simulator.getPC();

        //all instructions that need ALUs. if ROB or reservation stations are full stall
        if (!simulator.reorder.isFull()) {

            //this is where we would check the btb -----------------------------------------------------------------
            //loads
            if (memInstr.getOpcode() == Instruction.INST_LW) {
                if (simulator.loader.isReservationStationAvail()) {
                    simulator.reorder.updateInstForIssue(issuee);
                    simulator.loader.acceptIssue(issuee);
                } else {
                    isStall = true;
                }

            } //stores
            else if (memInstr.getOpcode() == Instruction.INST_SW) {
                simulator.reorder.updateInstForIssue(issuee);
            }//HALTS
            else if (memInstr.getOpcode() == Instruction.INST_HALT) {
                simulator.reorder.updateInstForIssue(issuee);
            } //NOPS
            else if (memInstr.getOpcode() == Instruction.INST_NOP) {
                simulator.reorder.updateInstForIssue(issuee);
            } //intALU
            else if (!simulator.alu.areReservationStationsFull() 
                    && memInstr.getOpcode() != Instruction.INST_DIV 
                    && memInstr.getOpcode() != Instruction.INST_MUL) {

                simulator.reorder.updateInstForIssue(issuee);
                simulator.alu.acceptIssue(issuee);

            } //intDIV
            else if (!simulator.divider.areReservationStationsFull() 
                    && memInstr.getOpcode() == Instruction.INST_DIV) {

                simulator.reorder.updateInstForIssue(issuee);
                simulator.divider.acceptIssue(issuee);

            } //intMULT
            else if (!simulator.multiplier.areReservationStationsFull() 
                    && memInstr.getOpcode() == Instruction.INST_MUL) {

                simulator.reorder.updateInstForIssue(issuee);
                simulator.multiplier.acceptIssue(issuee);

            } else {
                isStall = true; //set true if non of ALU reservation stations are ready
            }

            if (isStall) {
                //stall 
                System.out.println("we got stallllllllllllllll; resStations FULLLLLLL");

                //change PC to stall
                simulator.setPC(simulator.getPC() - 4);

            }
        }

        //updatePC -----PC gets updated in ROBEntry
        simulator.pc.incrPC();
        //IF NECESSARY RESET PC FOR BRANCHING HERE
    }
}
