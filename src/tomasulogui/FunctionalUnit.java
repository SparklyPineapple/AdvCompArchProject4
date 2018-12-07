package tomasulogui;

public abstract class FunctionalUnit {

    PipelineSimulator simulator;
    ReservationStation[] stations = new ReservationStation[2];

    public FunctionalUnit(PipelineSimulator sim) {
        simulator = sim;

    }

    public void squashAll() {
        // get rid of inst in the reservation stations
        stations = new ReservationStation[2];
    }

    public abstract int calculateResult(int station);

    public abstract int getExecCycles();

    public void execCycle(CDB cdb) {
        //todo - start executing, ask for CDB, etc.

        if (cdb.getDataValid()) {
            
            if(cdb.getDataTag()== stations[0].tag1){
                stations[0].data1 = cdb.dataValue;
                stations[0].data1Valid = true;
                
            }else if(cdb.getDataTag()== stations[0].tag2){
                stations[0].data2 = cdb.dataValue;
                stations[0].data2Valid = true;
                
            }else if(cdb.getDataTag()== stations[1].tag1){
                stations[1].data1 = cdb.dataValue;
                stations[1].data1Valid = true;
            }else if(cdb.getDataTag()== stations[1].tag2){
                stations[1].data2 = cdb.dataValue;
                stations[1].data2Valid = true;
            }else{
                //crickets
            }
            
            
            //send to the alu if an instruction is ready
            int result;
            if(stations[1].isReady()){
               result = this.calculateResult(1);
               cdb.setDataValue(result);
               cdb.setDataTag(stations[1].destTag);
               cdb.setDataValid(true);
               stations[1].isEmpty = true;
            }else if(stations[0].isReady()){
               result =  this.calculateResult(0);
               cdb.setDataValue(result);
               cdb.setDataTag(stations[0].destTag);
               cdb.setDataValid(true);
               stations[0].isEmpty = false;//clear Res Station if applicable ... aka we can just write over it :)
            }
            
            
            
           

        }
        //look for regTags for stuff thats in the reservation station. once all tags are found for a station then we make it "valid" and ALU can grab it
    }

    public void acceptIssue(IssuedInst inst) {

//        //move stuff in first spot down if second spot is open so we can always know that the top stuff is most recent.
//        //aka FU check 2nd spot first and if nothing there grab from 1st spot
//        if (stations[0] != null && stations[1] == null) {
//            stations[1] = stations[0];
//        }
        //fill in index 1 (lower index close it ALU if empty)
        if (stations[1] == null) {
            stations[1].tag1 = inst.regSrc1Tag;
            stations[1].data1 = inst.regSrc1Value;
            stations[1].data1Valid = inst.regSrc1Valid;
            stations[1].tag2 = inst.regSrc1Tag;
            stations[1].data2 = inst.regSrc1Value;
            stations[1].data2Valid = inst.regSrc1Valid;
            stations[1].destTag = inst.regDestTag;
            stations[1].function = inst.opcode;
            stations[1].isEmpty = false;
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
        } else //write to index 0
        {
            stations[0].tag1 = inst.regSrc1Tag;
            stations[0].data1 = inst.regSrc1Value;
            stations[0].data1Valid = inst.regSrc1Valid;
            stations[0].tag2 = inst.regSrc1Tag;
            stations[0].data2 = inst.regSrc1Value;
            stations[0].data2Valid = inst.regSrc1Valid;
            stations[0].destTag = inst.regDestTag;
            stations[0].function = inst.opcode;
            stations[0].isEmpty = false;
        }

    }

    public boolean areReservationStationsFull() {
        if (!stations[0].isEmpty && !stations[1].isEmpty) {
            return true;
        }
        return false;
    }

}
