`timescale 1ns/1ns
module testbench();

reg x;
reg rst;
reg clk;

wire [1:0] y;

source s(y,x,rst,clk);

// Change clock every 10 ns
always
    #10 clk = !clk;

initial begin
  clk = 0;
  rst = 0;
  x = 0;
    $dumpfile("TimingDiagram.vcd");
    $dumpvars(0, y, x, rst, clk);

    #20 x = 1;
    #20 x = 1;
    #20 x = 0;
    #20 x = 1;
    // FAST
    #20 x = 0;
    #20 x = 0;
    #20 x = 1;
    // NORMAL
    #20 x = 0;
    #20 x = 0;
    #20 x = 0;
    #20 x = 1;
    // SLOW
    #20 x = 0;
    #20 x = 0;
    #20 x = 0;
    #20 x = 0;
    #20 x = 1;
    // SLOW
    #20 x = 0;
    #20 x = 0;
    #20 x = 0;
    #20 x = 0;
    #20 rst = 1;
    #20 rst = 0;
    // RESET SESSION
    #20 x = 1;
    #20 x = 0;
    #20 x = 1;
    // FAST
    #20 x = 0;
    #20 x = 0;
    #20 x = 0;
    #20 x = 0;
    #20 x = 0;
    #20 x = 1;
    // SLOW
    #20 x = 1;
    #20 x = 0;
    #20 x = 0;
    #20 x = 1;
    // NORMAL
    #20 x = 1;
    #20 x = 1;
    #20 x = 1;
    #20 x = 1;
    #20 x = 1;
    #20 x = 0;
    #20 x = 0;
    #20 x = 0;
    #20 x = 0;
    #20 x = 0;
    #20
    $finish;
end

endmodule
