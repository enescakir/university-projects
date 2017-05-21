module Reg3(s3, s2, s1, n3, n2, n1, clk, rst);

  input rst, clk, n3, n2, n1;
  output reg s3, s2, s1;

  // Set initial state
  initial begin
    s3 = 0;
    s2 = 0;
    s1 = 0;
  end

  always @(posedge clk)
    if(rst)
      begin
        s3 = 0;
        s2 = 0;
        s1 = 0;
      end
    else
      begin
        s3 = n3;
        s2 = n2;
        s1 = n1;
      end

endmodule
