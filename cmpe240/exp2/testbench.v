`timescale 1ns/1ns
module testbench();

reg [4:0] x;
wire [0:0] y;

source s(y, x);

initial begin
    $dumpfile("TimingDiagram.vcd");
    $dumpvars(0, y, x);
      x = 0;
      repeat(32)
      begin
        #20;
        x = x + 1;
      end
    $finish;
end

endmodule
