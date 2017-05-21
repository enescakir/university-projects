`timescale 1ns/1ns
module source(	output reg [1:0] y,
	input x,
	input rst,
	input clk
);

parameter S0 = 3'b000, S1 = 3'b001, S2 = 3'b010, S3 = 3'b011, S4 = 3'b100, S5 = 3'b101, S6 = 3'b110, S7 = 3'b111;

reg [2:0] s;

initial begin
	y <= 2'b00;
end

always @(negedge clk) begin
	if(rst == 1'b1) begin
		s <= S0;
    y <= 2'b00;
	end
end

always @(posedge clk) begin
  begin
		if(s == S0) begin
			if(x == 1'b0) begin
				s <= S1;
        y <= 2'b00;
			end
			else
      begin
				s <= S0;
        y <= 2'b00;
			end
		end
		else if(s == S1) begin
			if(x == 1'b0) begin
				s <= S1;
        y <= 2'b00;
			end
			else begin
				s <= S2;
        y <= 2'b00;
			end
		end
		else if(s == S2) begin
			if(x == 1'b0) begin
				s <= S1;
        y <= 2'b01;
			end
			else begin
				s <= S3;
        y <= 2'b00;
			end
		end
    else if(s == S3) begin
      if(x == 1'b0) begin
        s <= S1;
        y <= 2'b10;
      end
      else begin
        s <= S4;
        y <= 2'b00;
      end
    end
    else if(s == S4) begin
      if(x == 1'b0) begin
        s <= S1;
        y <= 2'b11;
      end
      else begin
        s <= S4;
        y <= 2'b00;
      end
    end
		else begin
			if(x == 1'b0) begin
				s <= S0;
        y <= 2'b00;
			end
			else begin
				s <= S0;
        y <= 2'b00;
			end
		end
	end
end
endmodule
