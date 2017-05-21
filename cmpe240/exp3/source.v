`timescale 1ns/1ns
module source(y,x,rst,clk);

  output [1:0] y;
  input x;
  input rst;
  input clk;

  // Wires between logic part and register part
  wire n3,n2,n1,s3,s2,s1;

  // Combinational Logic
  Comb comb(n3, n2, n1, y, s3, s2, s1, x);

  // State Register
  Reg3 reg3(s3, s2, s1, n3, n2, n1, clk, rst);

endmodule
