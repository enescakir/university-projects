function []=problem2(directory)
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
        datas = [datas;data(2:end,:)];
        % Append new datas to old ones
    end
    figure;
    % Plot datas
    plot(datas); 
    % Find peaks of datas
    findpeaks(datas, 'MinPeakProminence', 0.6, 'MinPeakDistance', 200, 'MinPeakHeight', 3.5, 'MinPeakWidth', 100);
end