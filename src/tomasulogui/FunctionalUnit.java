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
        
        // snoop on the CDB in case it has something we need
        if (cdb.getDataValid() && stations[0] != null && (!(stations[0].data1Valid) || !(stations[0].data2Valid))) {
            stations[0].snoop(cdb);
        }
        if (cdb.getDataValid() && stations[1] != null && (!(stations[1].data1Valid) || !(stations[1].data2Valid))) {
            stations[1].snoop(cdb);
        }
        
        //Broadcast if the pipeline Simulator has permissed this FU
        if (iCanTalk) {
            int result = calculateResult(reservationStationBeingCalc);
            cdb.setDataValue(result);
            cdb.setDataTag(stations[reservationStationBeingCalc].destTag);
            cdb.setDataValid(true);
            doCountDown = false;
            stations[reservationStationBeingCalc] = null;
            reservationStationBeingCalc = -1;
            iCanTalk = false;
            iWantToTalk = false;
        }
        
        //if the ALU is available, start the next calculation
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

        //if the ALU is in the middle of a calculation, complete the next Cycle
        if (doCountDown) {
            cycles2Execute--;
            //Ask to broadcast if Calculation is finished
            if (cycles2Execute == 0) {
                iWantToTalk = true; 
            }
        }
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
            return true;
        }
        return false;
    }
}
