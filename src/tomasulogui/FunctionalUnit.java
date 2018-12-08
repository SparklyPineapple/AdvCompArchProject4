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

//        //snoop on the CDB first to validate/update data before checking if<---THIS IS DONE IN RESERVATION STATION CLASS
//        //a functional unit can perform its function
//        if (cdb.getDataValid()) {
//            if(cdb.getDataTag()== stations[0].tag1){
//                stations[0].data1 = cdb.dataValue;
//                stations[0].data1Valid = true;
//                
//            }else if(cdb.getDataTag()== stations[0].tag2){
//                stations[0].data2 = cdb.dataValue;
//                stations[0].data2Valid = true;
//                
//            }else if(cdb.getDataTag()== stations[1].tag1){
//                stations[1].data1 = cdb.dataValue;
//                stations[1].data1Valid = true;
//            }else if(cdb.getDataTag()== stations[1].tag2){
//                stations[1].data2 = cdb.dataValue;
//                stations[1].data2Valid = true;
//            }else{
//                //crickets
//            }
        stations[0].snoop(cdb);
        stations[1].snoop(cdb);

        //send to the alu if an instruction is ready
        int result;
        if (stations[1].isReady()) {
            result = this.calculateResult(1);
            cdb.setDataValue(result);
            cdb.setDataTag(stations[1].destTag);
            cdb.setDataValid(true);
            stations[1].isEmpty = true;
        } else if (stations[0].isReady()) {
            result = this.calculateResult(0);
            cdb.setDataValue(result);
            cdb.setDataTag(stations[0].destTag);
            cdb.setDataValid(true);
            stations[0].isEmpty = false;//clear Res Station if applicable ... aka we can just write over it :)
        }

    }

    public void acceptIssue(IssuedInst inst) {

        //accepts issues into reservation stations 
        //reservation stations are already verified to be open before it hits this point (in IssueUnit) 
        //fill in index 1 (lower index close it ALU if empty)
        if (stations[1].isEmpty) {
            stations[1].loadInst(inst);
        } else //write to index 0
        {
            stations[0].loadInst(inst);
        }

    }

    public boolean areReservationStationsFull() {
        if (!stations[0].isEmpty && !stations[1].isEmpty) {
            return true;
        }
        return false;
    }

}

//old stuff in execCycle
////        switch (inst.letter) {
////            case I:
////                if(inst.regDestUsed){
////                    newData2 = inst.immediate;
////                    newData2Tag = -1;
////                    newData2valid = true;
////                }
////                break;
////            case J:
////                System.out.println("j-type detected in functional unit");
////                break;
////            case R:
////                newData2 = inst.regSrc2;
////                newData2Tag = inst.regSrc2Tag;
////                newData2valid = inst.regSrc2Valid;
////                break;
////            default:
////                break;
////        }
//old stuff in accept issue
////        //move stuff in first spot down if second spot is open so we can always know that the top stuff is most recent.
////        //aka FU check 2nd spot first and if nothing there grab from 1st spot
////        if (stations[0] != null && stations[1] == null) {
////            stations[1] = stations[0];
////        }
