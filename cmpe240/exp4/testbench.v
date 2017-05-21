`timescale 1ns/1ns
module testbench();

reg x;
reg rst;
reg clk;

wire [1:0] y;

source s(
	.y(y),
	.x(x),
	.rst(rst),
	.clk(clk)
);

initial begin
    $dumpfile("TimingDiagram.vcd");
    $dumpvars(0, y, x, rst, clk);
	x = 0;
	rst = 1;
	#40;
	rst = 0;
	x = 0;
	#40;
	x = 1;
	#40;
	x = 1;
	#40;
	x = 1;
	#40;
	x = 0;
	#40;
	x = 0;
	#40;
	x = 0;
	#40;
	x = 1;
	#40;
  x = 1;
  #40;
  x = 0;
  #40;
  x = 1;
  #40;
  x = 0;
	#40


    $finish;
end

always begin
	clk = 0;
	#20;
	clk = 1;
	#20;
end

endmodule
