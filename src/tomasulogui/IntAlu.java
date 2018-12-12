package tomasulogui;

public class IntAlu extends FunctionalUnit {

    public static final int EXEC_CYCLES = 1;

    public IntAlu(PipelineSimulator sim) {
        super(sim);
    }

    public int calculateResult(int station) {
        switch (stations[station].function) {
            case ADD:
                return stations[station].data1 + stations[station].data2;
            case ADDI:
                return stations[station].data1 + stations[station].data2;
            case SUB:
                return stations[station].data1 - stations[station].data2;
            case AND:
                return stations[station].data1 & stations[station].data2;
            case ANDI:
                return stations[station].data1 & stations[station].data2;
            case OR:
                return stations[station].data1 | stations[station].data2;
            case ORI:
                return stations[station].data1 | stations[station].data2;
            case XOR:
                return stations[station].data1 ^ stations[station].data2;
            case XORI:
                return stations[station].data1 ^ stations[station].data2;
            case SLL:
                return stations[station].data1 << stations[station].data2;
            case SRL:
                return stations[station].data1 >> stations[station].data2;
            case SRA:
                return stations[station].data1 >>> stations[station].data2;
            //branch and jump instructions (for comparing etc)
//            case ADD:
//                int a = 1;
//                break;
//            case ADD:
//                int a = 1;
//                break;
//            case ADD:
//                int a = 1;
//                break;

        }
        return -1;
    }

    public int getExecCycles() {
        return EXEC_CYCLES;
    }
}
