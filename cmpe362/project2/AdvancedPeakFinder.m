clear; clc;

% Script from first homework
directory = input('Enter data directory: ', 's');

% Get all files that has .csv extension in given directory recursively
files = dir([directory '/**/*.csv']);

% Combine  path of files and their names
files = strcat({files.folder}, {'/'}, {files.name});
datas = [];
for file = files
    % Read cvs file
    data = csvread(file{1});
    % Remove first row of the read data
    % Because it's sample rate information
    Fs = data(1,1);
    datas = [datas;data(2:end,:)];
    % Append new datas to old ones
end

% Find peaks of datas without anything
peaks_no_filter = findpeaks(datas, 'MinPeakProminence', 0.6, 'MinPeakDistance', 200, 'MinPeakHeight', 3.5, 'MinPeakWidth', 100);

% LOW PASS FILTERING
lp_cutoffs = [0, 1000, 2000, 3000, 4000];
lp_nCutoffs = length(lp_cutoffs);
lp_nPeaks = zeros(1,lp_nCutoffs);
% Add no filter result to first index
lp_nPeaks(1) = length(peaks_no_filter);

for i = 2:lp_nCutoffs
    % Design low pass filter
    lp_filter = designfilt('lowpassiir', ...
             'FilterOrder',8, ...
             'PassbandFrequency', lp_cutoffs(i), ...
             'PassbandRipple',0.3, ...
             'SampleRate', 100e3);
         
    % Apply low pass filter
    lp_result = filter(lp_filter,datas);
    
    % Find peaks
    peaks = findpeaks(lp_result, 'MinPeakProminence', 0.6, 'MinPeakDistance', 200, 'MinPeakHeight', 3.5, 'MinPeakWidth', 100);
    
    % Save number of peaks
    lp_nPeaks(i) = length(peaks);
end

% Plot result of part 1
figure;
plot(lp_cutoffs, lp_nPeaks); title('Number of Peaks vs Changing Cut-Off Frequencies');

% MOVING AVERAGE FILTERING
mf_samples = [0' 2:30];
mf_nSamples = length(mf_samples);
mf_nPeaks = zeros(1,mf_nSamples);
% Add no filter result to first index
mf_nPeaks(1) = length(peaks_no_filter);

for i = 2:mf_nSamples
    % Design moving  average filter
    mf_filter = 1/i * ones(i,1);
    
    % Apply moving  average filter
    mf_result = filter(mf_filter,1,datas);
    
    % Find peaks
    peaks = findpeaks(mf_result, 'MinPeakProminence', 0.6, 'MinPeakDistance', 200);
    
    % Save number of peaks
    mf_nPeaks(i) = length(peaks);
end

% Plot result of part 2
figure;
plot(mf_samples, mf_nPeaks); title('Number of Peaks vs Changing N');
