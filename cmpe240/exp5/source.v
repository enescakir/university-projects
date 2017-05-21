`timescale 1ns/1ns
module source(output reg [4:0] F,
  output reg [0:0] Cout,
  output reg [0:0] Overflow,
  input [1:0] S,
	input [4:0] X,
  input [4:0] Y
);

parameter S0 = 2'b00, S1 = 2'b01, S2 = 2'b10, S3 = 2'b11;

reg [2:0] s;
reg [4:0] temp;
integer i;

always @(S, X, Y) begin
  begin
		if(S == S0) begin
      {Cout, F} = X[3:1] * Y[2:0];
      Overflow = 1'b0;
		end
		else if(S == S1) begin
      Cout = X > Y;
      Overflow = 1'b0;
      F <= 5'b00000;
		end
		else if(S == S2) begin
      {Cout, F} = X + Y;
      Overflow = X[4] == Y[4] && F[4] != X[4];
		end
    else if(S == S3) begin
      temp = {Y[2:0], 2'b00};
      temp = ~temp + 5'b00001;
      {Cout, F} = X + temp;
      Overflow = X[4] == temp[4] && F[4] != X[4];
    end
	end
end
endmodule
