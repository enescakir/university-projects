clc; clear;

% Read clap sound
[sound_clap, fs_clap] = audioread('clap.wav');

% Read snap sound
[sound_snap, fs_snap] = audioread('snap.wav');

% Read input sound
[sound_input, fs_input] = audioread('input.wav');

% Convert time domains to frequency domains
% Change stereo sound to mono
sound_clap = mean(sound_clap,2);
[fft_clap, f_clap] = convert_frequency_domain(sound_clap, fs_clap);

sound_snap = mean(sound_snap,2);
[fft_snap, f_snap] = convert_frequency_domain(sound_snap, fs_snap);

sound_input = mean(sound_input,2);
[fft_input, f_input] = convert_frequency_domain(sound_input, fs_input);

disp("Clap Max:")
disp(max(fft_clap))
disp("Snap Max:")
disp(max(fft_snap))
disp("Input Max:")
disp(max(fft_input))

% Plot street, mike and mixed in frequency domain
figure('Name','Clap, Snap and Input in Frequency Domain','NumberTitle','off')
subplot(3,1,1), 
    plot(f_clap, fft_clap); 
    title('Clap Sound - Frequecy Domain');
subplot(3,1,2), 
    plot(f_snap, fft_snap); 
    title('Snap Sound - Frequecy Domain');
subplot(3,1,3), 
    plot(f_input, fft_input); 
    title('Input Sound - Frequecy Domain');

threshold = 20;
if(max(fft_input) > threshold)
    disp('Clap is detected');
else
    disp('Snap is detected');
end

% Converts time domain to frequency domains
function [result_fft,result_freqs] = convert_frequency_domain(signal, fs)
    result_fft = abs(fftshift(fft(signal)));
    
    result_freqs = ( -fs/2 : (1/(length(signal)-1)) * fs : fs/2);

end