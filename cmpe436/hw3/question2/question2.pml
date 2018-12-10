// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 3

#define NUMBER_OF_PROCESS 2

bool s = true; // Shared variable
bit y[NUMBER_OF_PROCESS]; // local variable
bit cs[NUMBER_OF_PROCESS]; // critical sections

active proctype P(bit i) {
	do
 	::
		// non-cs section
		atomic {
			y[i] = 1;
			s = i;
		}

		// wait until
		(y[1-i] == 0 || s != 0);

		// cs section
		cs[i] = 1; 
		y[i] = 0;
		cs[i] = 0;
 	od
}

init {
	int i;
	for (i : 1 .. NUMBER_OF_PROCESS) {
		run P(i);
	}
}

// Q2.a
// Mutual Exclusion
ltl mutex { [] !(cs[0] && cs[1]) }

// Q2.b
// Ensures absence of unbounded overtaking
ltl unbounded_overtaking { (<> cs[0]) && (<> cs[1]) }

// Q2.c
// Occupy its critical section infinitely often.
ltl infinitely_often { ([]<> cs[0]) && ([]<> cs[1]) }
