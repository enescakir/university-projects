# CmpE300 - Analysis of Algorithms
| Term | Instructor |
| --- | --- |
| Fall 2017  | Tunga Güngör  |

We are asked to implement MapReduce algorithm using `Message Passing Interface (MPI)` for calculating word frequencies. Master processor coordinates other slave processors. After this point I will use master for master processor and slaves for slave processors. Master shares data between slaves in balanced parts and combines results.

I used `Boost MPI` library for `C++`. It has more modern programatic interface than `Open MPI`

### Install and Run Boost MPI on macOS
```bash
# Install Open MPI
brew install open-mpi

# Install Boost MPI. Please read any error message.
# It might be has linking error. You can google it to fix permission error.
brew install boost-mpi

# Compile it
mpic++ -std=c++11 main.cpp -o main.o -I/usr/local/include/ -L/usr/local/lib -lboost_mpi -lboost_serialization

# Run it with 10 processors
mpirun --oversubscribe -np 10 main.o speech_tokenized.txt
```
