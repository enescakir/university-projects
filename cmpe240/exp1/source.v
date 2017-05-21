`timescale 1ns / 1ps
module source(y, x);

input wire [2:0] x;
output wire [0:0] y;

wire notX2, w1;

not (notX2, x[2]);
and A(w1, notX2, x[1]);
or O(y, w1, x[0]);

endmodule
