package tomasulogui;

public class ROBEntry {
  ReorderBuffer rob;

  // TODO - add many more fields into entry
  // I deleted most, and only kept those necessary to compile GUI
  
  //K+A variables
  //store fields
  int addr; //addr of where we are storing
  int virtRegSrc; //virtual register source (number of index in array)
  int virtRegAddr; //virtual register holding adress to be written to (number of index in array)
  int addrOffset; //offset to add to virtRegAddr to get final address to write to
  
  //branch fields
  //boolean prediction; //taken/nottaken prediction
 // boolean mispredict; //is the result of the prediction correct
  int result;//conditional result for branches w/ conditions
  int target;//address of branch target
  int nextPC; //PC+4
   
  //fields
  
  
  
  
  
  //G's variables
  boolean complete = false; //state field
  boolean predictTaken = false;
  boolean mispredicted = false;
  int instPC = -1; 
  int writeReg = -1; //dest reg??? aka destination field
  int writeValue = -1;//value to store in dest reg?? //value field

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

  public IssuedInst.INST_TYPE getOpcode () {
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
    instPC = inst.getPC();
    inst.setRegDestTag(frontQ);

    // TODO - This is a long and complicated method, probably the most complex
    // of the project.  It does 2 things:
    // 1. update the instruction, as shown in 2nd line of code above
    // 2. update the fields of the ROBEntry, as shown in the 1st line of code above

    
    //fields: busy, instruction type, state (complete/executing), destination (reg# or mem addr for stores), value (result of operation or value for stores),tag (what to snoop for in ROB) 
    //NOTE bufferslot is TAG field so no need to add/implement
    
    
    
    
    
    
  }

}
