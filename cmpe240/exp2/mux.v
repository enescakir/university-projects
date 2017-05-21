`timescale 1ns/1ns
module Mux8to1(I7, I6, I5, I4, I3, I2, I1, I0, S2, S1, S0, O);

input I0, I1, I2, I3, I4, I5, I6, I7;
input S0, S1, S2;
output O;
reg O;

always @(I7, I6, I5, I4, I3, I2, I1, I0, S2, S1, S0)
  begin
    if(S2==0 && S1 == 0 && S0==0)
      O <= I0;
    else if( S2==0 && S1 == 0 && S0==1 )
      O <= I1;
    else if( S2==0 && S1 == 1 && S0==0 )
      O <= I2;
    else if(S2 == 0 && S1 == 1 && S0 == 1)
      O <= I3;
    else if(S2 == 1 && S1 == 0 && S0 == 0)
      O <= I4;
    else if(S2 == 1 && S1 == 0 && S0 == 1)
      O <= I5;
    else if(S2 == 1 && S1 == 1 && S0 == 0)
      O <= I6;
    else if(S2 == 1 && S1 == 1 && S0 == 1)
      O <= I7;
  end
endmodule
