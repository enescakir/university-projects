### Notice: It has some problems. Last minute work
## Question 1
### Lockset Algorithm
The lockset-based detectors never produces false negatives.
They never miss a real race. So I couldn't find an example that data race is missed.
But it may produce many false positives.
`Lockset.java` shows a potential race that can be found by Lockset based race detection algorithm.
Two thread overwrite same variable. Final value should be 6000, but it's not sometimes.

To run Road Runner use this pattern:
`javac Lockset.java && rrrun -quiet -tool=TL:RS:LS Lockset`

### Happens-Before Algorithm
`HappensBeforeThread` has a shared variable and counting semaphore.
Not detectable version use counting semaphore to protect variable. But it has 2 resource.
We don't know which thread will get resource. But it protected somehow. Happens before algorithm not detect it.
`HappensBeforeNotDetect.java` class shows it.

Detectable version is not using any lock. Algorithm detects it.
`HappensBeforeDetect.java` class shows it.

To run Road Runner use this pattern:
`javac HappensBeforeDetect.java && rrrun -quiet -tool=FT2 HappensBeforeDetect`

## Question 2
`question2.pml` file contains solutions for Question 2.

Promela has LTL unary operators
[] the temporal operator always
<> the temporal operator eventually
! the boolean operator for negation

Last part of code has 3 different LTL formulas for Question 2.
`ltl mutex { [] !(cs[0] && cs[1]) }`
`ltl unbounded_overtaking { (<> cs[0]) && (<> cs[1]) }`
`ltl infinitely_often { ([]<> cs[0]) && ([]<> cs[1]) }`

### Question 3
`Question3_a.jpg` and `Question3_b.jpg` show answers.
