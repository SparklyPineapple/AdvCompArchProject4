package tomasulogui;

public class ReservationStation {

    PipelineSimulator simulator;

    int tag1;
    int tag2;
    int data1;
    int data2;
    boolean data1Valid = false;
    boolean data2Valid = false;
    // destTag doubles as branch tag
    int destTag;
    IssuedInst.INST_TYPE function = IssuedInst.INST_TYPE.NOP;

    // following just for branches
    int addressTag;
    boolean addressValid = false;
    int address;
    boolean predictedTaken = false;

    //k+a
    //boolean isEmpty = true;

    public ReservationStation(PipelineSimulator sim) {
        simulator = sim;
    }

    public int getDestTag() {
        return destTag;
    }

    public int getData1() {
        return data1;
    }

    public int getData2() {
        return data2;
    }

    public boolean isPredictedTaken() {
        return predictedTaken;
    }

    public IssuedInst.INST_TYPE getFunction() {
        return function;
    }

    public void snoop(CDB cdb) {
        if (cdb.getDataValid()) {
            if (cdb.getDataTag() == tag1) {
                data1 = cdb.dataValue;
                data1Valid = true;

            } else if (cdb.getDataTag() == tag2) {
                data2 = cdb.dataValue;
                data2Valid = true;
            }

        }
    }

    public boolean isReady() {
        return data1Valid && data2Valid;
    }

    public void loadInst(IssuedInst inst) {

        int newData2;
        int newData2Tag;
        boolean newData2Valid;

        //Depending on instruction type, data2 will be assign differently (either operand2, or immediate)
        if (inst.regSrc2Used) {
            newData2 = inst.regSrc2;
            newData2Tag = inst.regSrc2Tag;
            newData2Valid = inst.regSrc2Valid;
        } else {
            newData2 = inst.immediate;
            newData2Tag = -1;
            newData2Valid = true;
        }
    
            tag1 = inst.regSrc1Tag;
            data1 = inst.regSrc1Value;
            data1Valid = inst.regSrc1Valid;
            tag2 = newData2Tag;
            data2 = newData2;
            data2Valid = newData2Valid;
            destTag = inst.regDestTag;
            function = inst.opcode;
            //isEmpty = false;
            
            
            //inititalze stuff for branches here
             //FOR BRANCHES
//            stations[1].addressTag = inst.branchTgt?????
//            if (stations[1].addressTag != -1){
//                stations[1].addressValid = true;
//            }else{
//               stations[1].addressValid = false; 
//            }
//            stations[1].address = ?????
//            stations[1].predictedTaken = inst.branchPrediction;
// 

    }
}
