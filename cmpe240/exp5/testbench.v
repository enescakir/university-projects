`timescale 1ns/1ns
module testbench();

wire [4:0] F;
wire [0:0] Cout;
wire [0:0] Overflow;
reg [1:0] S;
reg [4:0] X;
reg [4:0] Y;

source s(
	.F(F),
	.Cout(Cout),
	.Overflow(Overflow),
  .S(S),
  .X(X),
  .Y(Y)
);

initial begin
    $dumpfile("TimingDiagram.vcd");
    $dumpvars(0, S, X, Y, F, Cout, Overflow);

      S = 2'b00;
      X = 5'b00110;
      Y = 5'b00111;
      #40
      S = 2'b00;
      X = 5'b01110;
      Y = 5'b00111;
      #40
      S = 2'b01;
      X = 5'b00000;
      Y = 5'b00001;
      #40
      S = 2'b01;
      X = 5'b00001;
      Y = 5'b00000;
      #40
      S = 2'b01;
      X = 5'b00000;
      Y = 5'b00000;
      #40
      S = 2'b10;
      X = 5'b00001;
      Y = 5'b00001;
      #40
      S = 2'b10;
      X = 5'b11111;
      Y = 5'b11111;
      #40
      S = 2'b10;
      X = 5'b01111;
      Y = 5'b01111;
      #40
      S = 2'b11;
      X = 5'b00001;
      Y = 5'b00001;
      #40
      S = 2'b11;
      X = 5'b00111;
      Y = 5'b00001;
      #40




    $finish;
end

endmodule
