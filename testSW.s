Begin Assembly
ADDI R1, R0, 11
NOP
NOP
NOP
NOP
NOP
SW R1, 4000(R0)
HALT
End Assembly
-- begin main data
Begin Data 4000 44
10
0
23
71
33
5
93
82
34
13
111
23
End Data
-- stack
Begin Data 5000 100
End Data