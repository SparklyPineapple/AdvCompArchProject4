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

        //IssuedInst instr = new IssuedInst();
        //opcode = simulator.getMemory().getInstAtAddr(instPC).getOpcode()
        Instruction memInstr = simulator.getMemory().getInstAtAddr(simulator.getPC());

        issuee = new IssuedInst();
        if (!simulator.reorder.isFull()) {
            Instruction parsed = Instruction.getInstructionFromName(memInstr.getName());

                //if immediate
                if (parsed instanceof ITypeInst) {
                    issuee.setImmediate(((ITypeInst) parsed).getImmed());
                    issuee.setRegDest(((ITypeInst) parsed).rt);
                    issuee.regSrc1 = (((ITypeInst) parsed).rs);

                } //if reg reg
                else if (parsed instanceof RTypeInst) {
                    issuee.regDest = ((RTypeInst) parsed).rd;
                    issuee.regSrc1 = ((RTypeInst) parsed).rs;
                    issuee.regSrc2 = ((RTypeInst) parsed).rt;
                    issuee.immediate = ((RTypeInst) parsed).shamt;

                  
                } //if J
                else if (parsed instanceof JTypeInst) {
                    issuee.immediate = ((JTypeInst) parsed).offset;
                }

                //this is where we should check the btb -----------------------------------------------------------------
                
                
            
            //intALU
            if (!simulator.alu.areReservationStationsFull() && memInstr.getOpcode() != Instruction.INST_DIV && memInstr.getOpcode() != Instruction.INST_MUL) {
                
                simulator.reorder.updateInstForIssue(issuee);
                
                
                //check CDB for operands if current operands not valid
                
                //send to FU
                simulator.alu.acceptIssue(issuee);

            } //intDIV
            else if (!simulator.divider.areReservationStationsFull() && memInstr.getOpcode() == Instruction.INST_DIV) {
          
                
                
                
                simulator.reorder.updateInstForIssue(issuee);
                //check CDB for operands if current operands not valid (for forwarding)
                
                //send to FU
                simulator.divider.acceptIssue(issuee);
                
                
            } //intMULT
            else if (!simulator.multiplier.areReservationStationsFull() && memInstr.getOpcode() == Instruction.INST_MUL) {
                // to issue, we make an IssuedInst, filling in what we know
                // We check the BTB, and put prediction if branch, updating PC
                //     if pred taken, incr PC otherwise
                // We then send this to the ROB, which fills in the data fields
                // We then check the CDB, and see if it is broadcasting data we need,
                //    so that we can forward during issue

                // We then send this to the FU, who stores in reservation station
                
                simulator.reorder.updateInstForIssue(issuee);
                //check CDB for operands if current operands not valid (for forwarding)
                
                //send to FU
                simulator.multiplier.acceptIssue(issuee);
                
            }else{
                //stall 
                System.out.println("we got stallllllllllllllll; resStations FULLLLLLL");
                
                //change PC to stall
                simulator.setPC(simulator.getPC()-4);
                
            }
        }

    }

}
