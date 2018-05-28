clear;clc;

% Read street sound
[sound_street, fs_street] = audioread('street.wav');

% Generate time vector for x axis of the street time domain plot
time_street = (0:length(sound_street) - 1) / fs_street;

% Read mike sound
[sound_mike, fs_mike] = audioread('mike.wav');

% Generate time vector for x axis of the mike time domain plot
time_mike= (0:length(sound_mike) - 1) / fs_mike;

% Generate mixed sound
sound_mixed = sound_street + sound_mike;

fs_mixed = (fs_mike + fs_street) / 2;

time_mixed= (0:length(sound_mixed) - 1) / fs_mixed;

% Convert time domains to frequency domains
[fft_street, f_street] = convert_frequency_domain(sound_street, fs_street);

[fft_mike, f_mike] = convert_frequency_domain(sound_mike, fs_mike);

[fft_mixed, f_mixed] = convert_frequency_domain(sound_mixed, fs_mixed);

% Apply filter
filter_order = 7;
low_human_freq = 200 / (fs_mixed/2);
high_human_freq = 3400 / (fs_mixed/2);

% sound_filtered = bandpass(sound_mixed, [low_human_freq high_human_freq], fs_mixed);
[b,a] = butter(filter_order, [low_human_freq, high_human_freq], 'stop');
sound_filtered = filter(b, a, sound_mixed);

[fft_filtered, f_mixed] = convert_frequency_domain(sound_filtered, fs_mixed);


% Plot street, mike and mixed in frequency domain
figure('Name','Street, Mike, Mixed Frequecy Domain','NumberTitle','off')
subplot(3,1,1), 
    plot(f_street, fft_street); 
    title('Street Sound - Frequecy Domain');
subplot(3,1,2), 
    plot(f_mike, fft_mike); 
    title('Mike Sound - Frequecy Domain');
subplot(3,1,3), 
    plot(f_mixed, fft_mixed); 
    title('Mixed Sound - Frequecy Domain');

% Plot street, mike and mixed in time domain
figure('Name','Street, Mike, Mixed Time Domain','NumberTitle','off')
subplot(3,1,1), 
    plot(time_street, sound_street); 
    title('Street Sound - Time Domain');
subplot(3,1,2), 
    plot(time_mike, sound_mike); 
    title('Mike Sound - Time Domain');
subplot(3,1,3), 
    plot(time_mixed, sound_mixed); 
    title('Mixed Sound - Time Domain');

% Plot street, mike and mixed in frequency domain
figure('Name','Mike, Filtered Mixed Frequecy Domain','NumberTitle','off')
subplot(2,1,1), 
    plot(f_mike, fft_mike); 
    title('Mike Sound - Frequecy Domain');
subplot(2,1,2), 
    plot(f_mixed, fft_filtered); 
    title('Filtered Mixed Sound - Frequecy Domain');

 % Plot street, mike and mixed in frequency domain
figure('Name','Mike, Filtered Mixed Time Domain','NumberTitle','off')
subplot(2,1,1), 
    plot(time_mike, sound_mike); 
    title('Mike Sound - Time Domain');
subplot(2,1,2), 
    plot(time_mike, sound_filtered); 
    title('Filtered Mixed Sound - Time Domain');

disp("SNR between mike and mixed:");
disp(SNR(sound_mike, sound_mixed));

disp("SNR between mike and filtered sound:");
disp(SNR(sound_mike, sound_filtered));

% Converts time domain to frequency domains
function [result_fft,result_freqs] = convert_frequency_domain(signal, fs)
    result_fft = abs(fftshift(fft(signal)));
    
    result_freqs = ( -fs/2 : (1/(length(signal)-1)) * fs : fs/2);

end

% Calculates SNR
function result = SNR(original, recovered)
    result = 10 * log10(sum(original.^2) ./ sum((recovered-original).^2));
end

