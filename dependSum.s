--add 1 and 2 together and store the result (3) into R3
--all ADDIs, and ADDs. no branches, jumps, or memory operations
Begin Assembly
ADDI R1, R1, 1 
ADDI R2, R2, 2
ADD R3, R1, R2
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