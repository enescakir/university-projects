SETS
       i   sector			/ 1*10 /
       j   disposal site	/ 1*5  / ;

PARAMETER q(j)  capacity of disposal site j /
$include "capacities.txt"
/;

PARAMETER d(i,j)  distance from sector i to disposal site j in kilometers /
$include "distances.txt"
/;

PARAMETER e(i)  estimated annual snow removal requirements of sector i /
$include "estimates.txt"
/;

SCALAR C cost of the transporting one thousand cubic meter of snow one kilometer ;
C = 0.10 * 1000;

VARIABLE Z total cost for objective function ;

VARIABLE X(i,j) indicating assignment of sector i to disposal site j;
X.up(i,j) = 1;
X.lo(i,j) = 0;

BINARY VARIABLE Y(j)  0 or 1 indicating disposal site j capacity increased by 100;

EQUATIONS
COST        define total objective function
CAP(j)      capacity constraints
ASGN(i)     sector assignment constraints
ICAP 		only one disposal site can increase capacity;

COST     ..   Z =e= SUM((i,j), X(i,j) * d(i,j) * e(i) * C);
CAP(J)   ..   SUM(i, X(i,j) * e(i)) =L= q(j) + (Y(j) * 100);
ASGN(I)  ..   SUM(j, X(i,j)) =e= 1;
ICAP     ..   SUM(j, Y(j)) =e= 1;

MODEL  GAP /ALL/ ;
OPTION MIP = Cplex;
OPTION optca=0;
OPTION optcr=0;
SOLVE GAP USING MIP MINIMIZING Z ;

DISPLAY X.L;
