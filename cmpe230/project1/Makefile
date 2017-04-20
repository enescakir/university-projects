all: stm2ir

stm2ir: stm2ir.o
	@g++ -std=c++11 -o stm2ir stm2ir.o

stm2ir.o: stm2ir.cpp
	@g++ -std=c++11 -c stm2ir.cpp -o stm2ir.o

run: stm2ir
	@./stm2ir ${ARGS}

clean:
	@rm stm2ir *.o
