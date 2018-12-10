package tomasulogui;

public class ReorderBuffer {

    public static final int size = 30;
    int frontQ = 0;
    int rearQ = 0;
    ROBEntry[] buff = new ROBEntry[size];
    int numRetirees = 0;

    PipelineSimulator simulator;
    RegisterFile regs;
    boolean halted = false;

    public ReorderBuffer(PipelineSimulator sim, RegisterFile registers) {
        simulator = sim;
        regs = registers;
    }

    public ROBEntry getEntryByTag(int tag) {
        return buff[tag];
    }

    public int getInstPC(int tag) {
        return buff[tag].getInstPC();
    }

    public boolean isHalted() {
        return halted;
    }

    public boolean isFull() {
        return (frontQ == rearQ && buff[frontQ] != null);
    }

    public int getNumRetirees() {
        return numRetirees;
    }

    /**
     * if instruction is a HALT instruction return true
     *
     * @return
     */
    public boolean retireInst() {//---------------------------------------------------------------------
        // 1. regular reg dest inst
        // 2. isBranch w/ mispredict
        // 3. isStore (store if instruction needs to store)
        ROBEntry retiree = buff[frontQ];

        if (retiree == null) {
            return false;
        }

        if (retiree.isHaltOpcode()) {
            halted = true;
            return true;
        }

        boolean shouldAdvance = true; //what is this?

        // TODO - this is where you look at the type of instruction and
        // figure out how to retire it properly
        //retire one closest to the front of the quese frontQ
        // if mispredict branch, won't do normal advance
        if (retiree.complete) {

            //stores
            if (retiree.getOpcode() == IssuedInst.INST_TYPE.STORE) {
                simulator.memory.setIntDataAtAddr(retiree.addr, retiree.getWriteValue());

            } //reg to reg operations. 
            else if (retiree.getOpcode() != IssuedInst.INST_TYPE.NOP) {
                regs.setReg(retiree.getWriteReg(), retiree.getWriteValue());
            } else {
                //branches?
            }

            //if branch 
            //if mispredict should advance = false;
            if (shouldAdvance) { //if not branch mispredict
                numRetirees++; //prolly tells us total instr executed
                buff[frontQ] = null;
                frontQ = (frontQ + 1) % size;
            } else { //if branch mispredict

            }
        }

        return false;
    }

    public void readCDB(CDB cdb) {
        // check entire CDB for someone waiting on this data 
        // could be destination reg
        // could be store address source

        if (cdb.getDataValid()) {
            buff[cdb.getDataTag()].setWriteValue(cdb.getDataValue());
            buff[cdb.getDataTag()].complete = true;
        }
    }

    public void updateInstForIssue(IssuedInst inst) {
        // the task is to simply annotate the register fields
        // the dest reg will be assigned a tag, which is just our slot#
        // all src regs will either be assigned a tag, read from reg, or forwarded from ROB

        // TODO - possibly nothing if you use my model
        // I use the call to copyInstData below to do 2 things:
        // 1. update the Issued Inst
        // 2. fill in the ROB entry
        // first get a ROB slot
        if (buff[rearQ] != null) {
            throw new MIPSException("updateInstForIssue: no ROB slot avail");
        }
        ROBEntry newEntry = new ROBEntry(this);
        buff[rearQ] = newEntry;
        newEntry.copyInstData(inst, rearQ);

        rearQ = (rearQ + 1) % size;
    }

    public int getTagForReg(int regNum) {
        return (regs.getSlotForReg(regNum));
    }

    public int getDataForReg(int regNum) {
        return (regs.getReg(regNum));
    }

    public void setTagForReg(int regNum, int tag) {
        regs.setSlotForReg(regNum, tag);
    }

}
