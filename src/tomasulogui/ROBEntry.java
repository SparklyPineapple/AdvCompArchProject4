package tomasulogui;

public class ROBEntry {

    ReorderBuffer rob;

    // TODO - add many more fields into entry
    // I deleted most, and only kept those necessary to compile GUI
    
    // store fields
    int addr = -1;
    int virtRegSrc = -1;
    int virtRegAddr = -1;
    int addrOffset = -1; 

    //branch fields (ignore for now. fill out later)
    int result;
    int target;
    int nextPC;//PC+4


    //G's variables
    boolean complete = false;
    boolean predictTaken = false;
    boolean mispredicted = false;
    int instPC = -1;
    int writeReg = -1;
    int writeValue = -1;

    IssuedInst.INST_TYPE opcode;

    public ROBEntry(ReorderBuffer buffer) {
        rob = buffer;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean branchMispredicted() {
        return mispredicted;
    }

    public boolean getPredictTaken() {
        return predictTaken;
    }

    public int getInstPC() {
        return instPC;
    }

    public IssuedInst.INST_TYPE getOpcode() {
        return opcode;
    }

    public boolean isHaltOpcode() {
        return (opcode == IssuedInst.INST_TYPE.HALT);
    }

    public void setBranchTaken(boolean result) {
        // TODO - maybe more than simple set
    }

    public int getWriteReg() {
        return writeReg;
    }

    public int getWriteValue() {
        return writeValue;
    }

    public void setWriteValue(int value) {
        writeValue = value;
    }

    public void copyInstData(IssuedInst inst, int frontQ) {
        // I use the call to copyInstData below to do 2 things:
        // 1. update the Issued Inst
        // 2. fill in the ROB entry

        // TODO - This is a long and complicated method, probably the most complex
        // of the project.  It does 2 things:
        // 1. update the instruction, 
        // 2. update the fields of the ROBEntry,
        

        //UPDATE FIELDS IN ROBEntry///////////////////////////////////////////////////////
        
        //update branch mispredicted
        //update prediction of branch (taken/nottaken) NOTE: intr without brnaches are predicted not taken
        
        instPC = inst.getPC();
        if (inst.regDestUsed == true) {
            writeReg = inst.getRegDest();
            rob.setTagForReg(writeReg, frontQ);
        }

        opcode = inst.getOpcode();

        if(opcode == IssuedInst.INST_TYPE.NOP || opcode == IssuedInst.INST_TYPE.HALT){
            complete = true;
        }
        
        
        
        //UPDATE INSTRUCTION/////////////////////////////////////////////////////////////
        inst.setRegDestTag(frontQ);
        
        //populate src1 data appropriately
        if(inst.regSrc1Used){
            if(rob.regs.getSlotForReg(inst.getRegSrc1()) == -1){ //if there is no tag in Reg File, reg has valid value so set
                inst.setRegSrc1Value(rob.regs.getReg(inst.getRegSrc1()));
                inst.setRegSrc1Valid();
            }else{
                inst.setRegSrc1Tag(rob.regs.getSlotForReg(inst.getRegSrc1()));
            }
        }
            
        //populate src2 data appropriately
        if(inst.regSrc2Used){
           if(rob.regs.getSlotForReg(inst.getRegSrc2()) == -1){ //if there is no tag in Reg File, reg has valid value so set
               inst.setRegSrc2Value(rob.regs.getReg(inst.getRegSrc2()));
               inst.setRegSrc2Valid();
           }else{
               inst.setRegSrc2Tag(rob.regs.getSlotForReg(inst.getRegSrc2()));
           }
        }
        
        
        //if store
        if (opcode == IssuedInst.INST_TYPE.STORE) { 
            addrOffset = inst.getImmediate(); //offset to add to virtRegAddr to get final address to write to
            
            if(inst.regSrc1Valid){
                addr = addrOffset + inst.getRegSrc1Value();
            }else{
                virtRegAddr = inst.getRegSrc1Tag();
            }
            
            if(inst.regSrc2Valid){
                writeReg = inst.regSrc2; //dest reg. aka destination field
                writeValue = inst.regSrc2Value;//value field
            }else{
                virtRegSrc = inst.getRegSrc2Tag();
            }
            
            if(inst.regSrc1Valid && inst.regSrc2Valid){
                complete = true;
            }
            
            
            
        }       
               
        
        //IF BRANCH (in case of mispredicted branch
        //target (taken addr)
        //PC+4 (not taken addr)
        //IF STORES (SW R1, R2(44)-------R1=src Reg, R2=dest Reg) 
        //addr that we are storing too (addr of virt Reg + offset)
        //srcReg 
        //addr of virtual Reg
        //addr offset
    }

}
