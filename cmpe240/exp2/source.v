`timescale 1ns/1ns
module source(y, x);

input wire [4:0] x;
output wire [0:0] y;

wire [3:0] d;
wire nX0, nNor, nX1nX0, x1x0;

not (nX0, x[0]);
not (nNor, nX1nX0);
and (x1x0, x[1], x[0]);
nor (nX1nX0, x[1], x[0]);
Mux8to1 mux(x1x0, nX1nX0, nNor, x1x0, nX0, nX0, x[0], x[1], x[4], x[3], x[2], y);

endmodule
