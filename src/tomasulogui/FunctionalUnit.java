package tomasulogui;

public abstract class FunctionalUnit {

    PipelineSimulator simulator;
    ReservationStation[] stations = new ReservationStation[2];
    int cycles2Execute = -1;
    boolean doCountDown = false;
    int reservationStationBeingCalc = -1;
    boolean iWantToTalk = false;
    boolean iCanTalk = false;

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

//        //snoop on the CDB first to validate/update data before checking if
//        //a functional unit can perform its function
        if (cdb.getDataValid() && stations[0] != null && (!(stations[0].data1Valid) || !(stations[0].data2Valid))) {
            stations[0].snoop(cdb);
        }
        if (cdb.getDataValid() && stations[1] != null && (!(stations[1].data1Valid) || !(stations[1].data2Valid))) {
            stations[1].snoop(cdb);
        }

        if (iCanTalk) {//DO THIS HERE RATHER THEN PIPSIM
            int result = calculateResult(reservationStationBeingCalc);
            cdb.setDataValue(result);
            cdb.setDataTag(stations[reservationStationBeingCalc].destTag);
            cdb.setDataValid(true);
            doCountDown = false;
            
            if((reservationStationBeingCalc == 0) && !stations[1].isReady()){
                if(stations[1].tag1 == stations[reservationStationBeingCalc].destTag){
                    stations[1].data1 = result;
                    
                }
                
                if(stations[1].tag2 == stations[reservationStationBeingCalc].destTag){
                    stations[1].data2 = result;
                }
            }
            stations[reservationStationBeingCalc] = null;
            reservationStationBeingCalc = -1;
            iCanTalk = false;
            iWantToTalk = false;
//
//            }
        }
        if (doCountDown == false && reservationStationBeingCalc == -1) {
            if (stations[1] != null && stations[1].isReady()) {
                reservationStationBeingCalc = 1;
                doCountDown = true;
                cycles2Execute = getExecCycles();
            } else if (stations[0] != null && stations[0].isReady()) {
                reservationStationBeingCalc = 0;
                doCountDown = true;
                cycles2Execute = getExecCycles();
            }
        }

        if (doCountDown) {
            cycles2Execute--;
            //I want to talk = true if next clock cycle I will be ready to talk
            if (cycles2Execute == 0) {
                iWantToTalk = true; //i want to talk next clk cycle
            }
        }
        
        //ailin's pitiful attempt to get things to work
//        if (cycles2Execute == 0) {
//                iWantToTalk = true; //i want to talk next clk cycle
//            }
        

    }

    public void acceptIssue(IssuedInst inst) {

        //accepts issues into reservation stations 
        //reservation stations are already verified to be open before it hits this point (in IssueUnit) 
        //fill in index 1 (lower index close it ALU if empty)
        ReservationStation tempStation = new ReservationStation(simulator);
        tempStation.loadInst(inst);
        if (stations[1] == null) {
            stations[1] = tempStation;
        } else {
            stations[0] = tempStation;
        }

    }

    //for every call to execCycle() an iterator should go down by 1 but when do we start the count down and how do we communicate when we are finished?
    public boolean areReservationStationsFull() {
        if (stations[0] != null && stations[1] != null) {
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
