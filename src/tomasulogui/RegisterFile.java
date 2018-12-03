package tomasulogui;

public class RegisterFile {
  PipelineSimulator simulator;
  int [] regs = new int[32]; //Register File; looking for reg 2
  int [] robSlot = new int[32]; //Vreg File; waits until virt Reg 2 is broadcast

  public RegisterFile(PipelineSimulator sim) {
    simulator = sim;
    for (int i=0; i < 32; i++ ) {
      robSlot[i] = -1;
    }
  }

  public int getReg(int regNum) {
    return regs[regNum];
  }

  public void setReg(int regNum, int regValue) {
    regs[regNum] = regValue;
  }

  public int getSlotForReg (int regNum) {
    return robSlot[regNum];
  }

  public void setSlotForReg (int regNum, int slot) {
    robSlot[regNum] = slot;
  }

  public void squashAll() {
    for (int i=0; i < 32; i++) {
      robSlot[i] = -1;
    }
  }

}


/*
When filling out ROB Entry for R1= R2+R3
and before this Reg[3] holds 50,  but Reg[2] is not written to yet

ROB Entry 
RegSrc1 = 3;
RegSrc1value = 50
RegSrcTag = -1;
RegSrc1Valid = true


RegSrc2 = -1
RegSrc2Tag = Slot # of ROB Entry holding R2 = R15+5
RegSrcValid = false;


//Setting Src1
first scan ROB for Reg[3] to set RegSrc1Valid

if ROB does not have Reg[3] as a dest Reg, RegSrc1Valid = true

if(RegScrc1Valid){
    RegSrc1 = 3;
    RegSrc1value = 50;
    RegSrcTag = -1;
}else{
    RegSrc1 = -1;
    RegSrc1value = -1
    RegSrcTag = slot number of ROBEntry;

}

*/