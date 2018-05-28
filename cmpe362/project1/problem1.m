clear;clc;

% Read high_signal frequency sound
[high_signal, freq_high] = audioread('highFreq.wav');
% Generate time vector for x axis of the high frequency time domain plot
high_time = (0:length(high_signal) - 1) / freq_high;

% Read low_signal frequency sound
[low_signal, freq_low]= audioread('lowFreq.wav');
% Generate time vector for x axis of the low frequency time domain plot
low_time = (0:length(low_signal) - 1) / freq_low;
    
% Calculate highest frequency using Fourier transform
high_ydft = fft(high_signal);
high_freqs = 0:freq_high/length(high_signal):freq_high/2;
high_ydft = high_ydft(1:length(high_signal)/2+1);
[maxval,high_idx] = max(abs(high_ydft));
highest_freq = high_freqs(high_idx);

% Print highest frequency
disp("Highest Frequency: ");
disp(highest_freq);

% Calculate lowest frequency using Fourier transform
low_ydft = fft(low_signal);
low_freqs = 0:freq_low/length(low_signal):freq_low/2;
low_ydft = low_ydft(1:floor(length(low_signal)/2)+1);
[minval,low_idx] = max(abs(low_ydft));
lowest_freq = low_freqs(low_idx);

% Print highest frequency
disp("Lowest Frequency: ");
disp(lowest_freq);

% Plot high frequency sound
% Plot in time domain
subplot(2,3,1), 
    plot(high_time, high_signal, "r"); 
    title('High Frequency - Time Domain');
% Plot spectrogram in frequency domain
subplot(2,3,2), 
    spectrogram(high_signal(1:end), 256, [], [], freq_high, 'xaxis', 'MinThreshold', -90); 
    title('High Frequency - Spectrogram');
% Plot in frequency domain
subplot(2,3,3), 
    plot(high_freqs,abs(high_ydft)); 
    title('High Frequency - Frequency Domain');

% Plot low frequency sound
% Plot in time domain
subplot(2,3,4), 
    plot(low_time, low_signal, "r");
    title('Low Frequency - Time Domain');
% Plot spectrogram in frequency domain
subplot(2,3,5), 
    spectrogram(low_signal, 256, [], [], freq_low, 'xaxis', 'MinThreshold', -95); 
    title('Low Frequency - Spectrogram');

% Plot in frequency domain
subplot(2,3,6), 
    plot(low_freqs,abs(low_ydft)); 
    title('Low Frequency - Frequency Domain');