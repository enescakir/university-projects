`timescale 1ns/1ns
module Comb(n3, n2, n1, y, s3, s2, s1, x);

  input s3, s2, s1, x;
  output n3, n2, n1;
  output [1:0]y;

  // NOT gates
  wire ns3, ns2, ns1, nx;
  not(ns3, s3);
  not(ns2, s2);
  not(ns1, s1);
  not(nx, x);

  // Results of AND & OR gates
  wire a13,a12,a11,a10, a9, a8, a7, a5, a4, a3, a2, a1, o5, o4, o3;

  // Output for n3
  and (a9, ns3, s2, ns1, x);
  and (a8, ns3, s2, s1);
  and (a7, s3, ns2, ns1);
  or (n3, a9, a8, a7);

  // Output for n2
  and (a13, s2, ns1, nx);
  and (a12, ns3, s2, x);
  and (a11, s3, s1, nx);
  and (a10, ns3, ns2, s1, nx);
  or (n2, a13, a12, a11, a10);

  // Output for n1
  and (a5, ns3, s2, ns1);
  and (a4, s3, x);
  and (a3, ns3, ns2, x);
  or (n1, a5, a4, a3);

  // Output for o2
  and (y[1], s3, s2);

  // Output for o1
  and (y[0], s3, s1);

endmodule
