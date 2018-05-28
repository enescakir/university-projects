%%
% Problem 3
% Clear old variables from workspace and console
clear; clc;
% Set name of the figure
figure('Name','Problem 3');

% Create a vector from -100 to 100
x = -100:100;

% Create functions for plotting
y1 = sin(x);
y2 = sin(50 .* x);
y3 = 50 .* sin(x);
y4 = sin(x) + 50;
y5 = sin(x + 50);
y6 = 50 .* sin(50 .* x);
y7 = x .* sin(x);
y8 = sin(x) ./ x;

% Draw plots and put to subplot with their titles
subplot(4,2,1), plot(x,y1); title('Graph of sin(x)');
subplot(4,2,2), plot(x,y2); title('Graph of sin(50 * x)');
subplot(4,2,3), plot(x,y3); title('Graph of 50 * sin(x)');
subplot(4,2,4), plot(x,y4); title('Graph of sin(x) + 50');
subplot(4,2,5), plot(x,y5); title('Graph of sin(x + 50)');
subplot(4,2,6), plot(x,y6); title('Graph of 50 * sin(50 * x)');
subplot(4,2,7), plot(x,y7); title('Graph of x * sin(x)');
subplot(4,2,8), plot(x,y8); title('Graph of sin(x) / x');

%%
% Problem 4
% Clear old variables from workspace and console
clear; clc;
% Set name of the figure
figure('Name','Problem 4');

% Create a vector from -20 to 20
x = -20:20;

% Create functions for plotting
y1 = sin(x);
y2 = sin(50 .* x);
y3 = 50 .* sin(x);
y4 = sin(x) + 50;
y5 = sin(x + 50);
y6 = 50 .* sin(50 .* x);
y7 = x .* sin(x);
y8 = sin(x) ./ x;
y9 = y1+y2+y3+y4+y5+y6+y7+y8;

% Draw plots and put to subplot with their titles
subplot(5,2,1), plot(x,y1); title('Graph of sin(x)');
subplot(5,2,2), plot(x,y2); title('Graph of sin(50 * x)');
subplot(5,2,3), plot(x,y3); title('Graph of 50 * sin(x)');
subplot(5,2,4), plot(x,y4); title('Graph of sin(x) + 50');
subplot(5,2,5), plot(x,y5); title('Graph of sin(x + 50)');
subplot(5,2,6), plot(x,y6); title('Graph of 50 * sin(50 * x)');
subplot(5,2,7), plot(x,y7); title('Graph of x * sin(x)');
subplot(5,2,8), plot(x,y8); title('Graph of sin(x) / x');
subplot(5,2,9), plot(x,y9); title('Graph of Sum Up All');

%%
% Problem 5
% Clear old variables from workspace and console
clear; clc;
% Set name of the figure
figure('Name','Problem 5');

% Create a vector from -20 to 20
x = -20:20;
% Generate 41 random number from gaussian distributed random numbers
z = randn(1, 41);

% Create functions for plotting
y10 = z;
y11 = z + x;
y12 = z + sin(x);
y13 = z .* sin(x);
y14 = x .* sin(z);
y15 = sin(x + z);
y16 = z .* sin(50 .* x);
y17 = sin(x + 50 .* z);
y18 = sin(x) ./ z;
y19 = y11+y12+y13+y14+y15+y16+y17+y18;

% Draw plots and put to subplot with their titles
subplot(5,2,1), plot(x,y10); title('Graph of z');
subplot(5,2,2), plot(x,y11); title('Graph of z + x');
subplot(5,2,3), plot(x,y12); title('Graph of z + sin(x)');
subplot(5,2,4), plot(x,y13); title('Graph of z * sin(x)');
subplot(5,2,5), plot(x,y14); title('Graph of x * sin(z)');
subplot(5,2,6), plot(x,y15); title('Graph of sin(x + z)');
subplot(5,2,7), plot(x,y16); title('Graph of z * sin(50x)');
subplot(5,2,8), plot(x,y17); title('Graph of sin(x + 50z)');
subplot(5,2,9), plot(x,y18); title('Graph of sin(x)/z');
subplot(5,2,10), plot(x,y19); title('Graph of Sum Up All');

%%
% Problem 6
% Clear old variables from workspace and console
clear; clc;
% Set name of the figure
figure('Name','Problem 6');

% Create a vector from -20 to 20
x = -20:20;
% Generate 41 random number from uniformly distributed random numbers
z = rand(1, 41);

% Create functions for plotting
y20 = z;
y21 = z + x;
y22 = z + sin(x);
y23 = z .* sin(x);
y24 = x .* sin(z);
y25 = sin(x + z);
y26 = z .* sin(50 .* x);
y27 = sin(x + 50 .* z);
y28 = sin(x) ./ z;
y29 = y21+y22+y23+y24+y25+y26+y27+y28;

% Draw plots and put to subplot with their titles
subplot(5,2,1), plot(x,y20); title('Graph of z');
subplot(5,2,2), plot(x,y21); title('Graph of z + x');
subplot(5,2,3), plot(x,y22); title('Graph of z + sin(x)');
subplot(5,2,4), plot(x,y23); title('Graph of z * sin(x)');
subplot(5,2,5), plot(x,y24); title('Graph of x * sin(z)');
subplot(5,2,6), plot(x,y25); title('Graph of sin(x + z)');
subplot(5,2,7), plot(x,y26); title('Graph of z * sin(50x)');
subplot(5,2,8), plot(x,y27); title('Graph of sin(x + 50z)');
subplot(5,2,9), plot(x,y28); title('Graph of sin(x)/z');
subplot(5,2,10), plot(x,y29); title('Graph of Sum Up All');

%% 
% Problem 7
% Clear old variables from workspace and console
clear; clc;
% Set name of the figure
figure('Name','Problem 7');

% Generate 10000 gaussian random variables;
z = randn(10000,1);

% Change random variables statics with mean 0, variance 1;
r1 = 0 + sqrt(1) .* z;
% Change random variables statics with mean 0, variance 4;
r2 = 0 + sqrt(4) .* z;
% Change random variables statics with mean 0, variance 16;
r3 = 0 + sqrt(16) .* z;
% Change random variables statics with mean 0, variance 256;
r4 = 0 + sqrt(256) .* z;

% Draw histograms and put to subplot with their titles
subplot(2,2,1), hist(r1, 50); title('Gaussian RV mean 0, variance 1');
subplot(2,2,2), hist(r2, 50); title('Gaussian RV mean 0, variance 4');
subplot(2,2,3), hist(r3, 50); title('Gaussian RV mean 0, variance 16');
subplot(2,2,4), hist(r4, 50); title('Gaussian RV mean 0, variance 256');

%% 
% Problem 8
% Clear old variables from workspace and console
clear; clc;
% Set name of the figure
figure('Name','Problem 8');

% Generate 10000 gaussian random variables;
z = randn(10000,1);

% Change random variables statics with mean 10, variance 1;
r6 = 10 + sqrt(1) .* z;
% Change random variables statics with mean 20, variance 4;
r7 = 20 + sqrt(4) .* z;
% Change random variables statics with mean -10, variance 1;
r8 = -10 + sqrt(1) .* z;
% Change random variables statics with mean -20, variance 4;
r9 = -20 + sqrt(4) .* z;

% Draw histograms and put to subplot with their titles
subplot(2,2,1), hist(r6, 50); title('Gaussian RV mean 10, variance 10');
subplot(2,2,2), hist(r7, 50); title('Gaussian RV mean 20, variance 20');
subplot(2,2,3), hist(r8, 50); title('Gaussian RV mean -10, variance -10');
subplot(2,2,4), hist(r9, 50); title('Gaussian RV mean -20, variance -20');

%% 
% Problem 9
% Clear old variables from workspace and console
clear; clc;
% Set name of the figure
figure('Name','Problem 9');

% Generate 10000 uniformly distributed random variables;
z = rand(10000,1);

% Change random variables statics with mean 0, variance 1;
r11 = 0 + sqrt(1) .* z;
% Change random variables statics with mean 0, variance 4;
r21 = 0 + sqrt(4) .* z;
% Change random variables statics with mean 0, variance 16;
r31 = 0 + sqrt(16) .* z;
% Change random variables statics with mean 0, variance 256;
r41 = 0 + sqrt(256) .* z;

% Draw histograms and put to subplot with their titles
subplot(2,2,1), hist(r11, 50); title('Uniformly RV mean 0, variance 1');
subplot(2,2,2), hist(r21, 50); title('Uniformly RV mean 0, variance 4');
subplot(2,2,3), hist(r31, 50); title('Uniformly RV mean 0, variance 16');
subplot(2,2,4), hist(r41, 50); title('Uniformly RV mean 0, variance 256');

%% 
% Problem 10
% Clear old variables from workspace and console
clear; clc;
% Set name of the figure
figure('Name','Problem 10');

% Generate 10000 uniformly distributed random variables;
z = rand(10000,1);

% Change random variables statics with mean 1, variance 10;
r61 = 10 + sqrt(1) .* z;
% Change random variables statics with mean 4, variance 20;
r71 = 20 + sqrt(4) .* z;
% Change random variables statics with mean 1, variance -10;
r81 = -10 + sqrt(1) .* z;
% Change random variables statics with mean 4, variance -20;
r91 = -20 + sqrt(4) .* z;

% Draw histograms and put to subplot with their titles
subplot(2,2,1), hist(r61, 100); title('Uniformly RV mean 10, variance 1');
subplot(2,2,2), hist(r71, 100); title('Uniformly RV mean 20, variance 4');
subplot(2,2,3), hist(r81, 100); title('Uniformly RV mean -10, variance 1');
subplot(2,2,4), hist(r91, 100); title('Uniformly RV mean -20, variance 4');
