package tomasulogui;

public class IntAlu extends FunctionalUnit {

    public static final int EXEC_CYCLES = 1;

    public IntAlu(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
        // just placeholder code
        int result = 0;
        //IssuedInst.INST_TYPE 
        switch (stations[station].function) {
            case ADD:
                int a = 1;
                break;
            case ADDI:
                int a = 1;
                break;
            case SUB:
                int a = 1;
                break;
            case AND:
                int a = 1;
                break;
            case ANDI:
                int a = 1;
                break;
            case OR:
                int a = 1;
                break;
            case ORI:
                int a = 1;
                break;
            case XOR:
                int a = 1;
                break;
            case XORI:
                int a = 1;
                break;
            case SLL:
                int a = 1;
                break;
            case SRL:
                int a = 1;
                break;
            case SRA:
                int a = 1;
                break;
            case ADD:
                int a = 1;
                break;
                            case ADD:
                int a = 1;
                break;
                            case ADD:
                int a = 1;
                break;
                            case ADD:
                int a = 1;
                break;
                            case ADD:
                int a = 1;
                break;
        }
//    {
//        
//    }
//    

        return result;
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
