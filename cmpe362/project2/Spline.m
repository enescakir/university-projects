clc; clear;

% Set global variables
y = [1025, 1400, 1710, 2080, 2425, 2760, 3005, 2850, 2675];
x = [265 400 500 700 950 1360 2080 2450 2940];
w = [3.86, 3.50, 3.42, 2.97, 2.55, 2.03, 1.44, 1.16, 0.91];
nPoints = length(x);
% Keeps result of equations for further usage
equations = zeros(3, nPoints-1);

% Plot points. Don't put the first point
plot(x(1, 2:end),y(1, 2:end),'*r');

% Scale graph for starting from 0
ylim([0 3500]);

% Put lines to same plot
hold on;

% Solve equeation for first space
A = [x(1:2).', [1;1]];
B = y(1:2)';
equations(2:3,1) = linsolve(A,B);


% Calculate spline for each gap
for i = 2:nPoints-1
    A = [[(x(i) .^ 2) (x(i+1) .^ 2) (2 .* x(i))]' [x(i) x(i+1) 1]' [1 1 0]'];  
    B = [y(i) y(i+1) (2 * equations(1,i-1) * x(i) + equations(2,i-1))]';
    equations(:,i) = linsolve(A,B);
    
    % Put it to plot
    plotx = (x(i):1:x(i+1))';
    ploty = [plotx .^ 2  plotx  ones(size(plotx))] * equations(:,i);
    plot(plotx,ploty);
end