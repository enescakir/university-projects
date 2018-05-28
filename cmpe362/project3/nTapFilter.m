clc; clear;

[signal, fs] = audioread('mike.wav');

% Delay amount in ms
K = 100;

mixed_delayed_signal = delay_signal(signal, fs, K) + signal;

% Use constant N and K, change α from 0 to 1
% plot SNR of mike.wav and recovered signal.
N = 50;
K = 100;
alphas = (0:0.02:1);
snr_results_1 = zeros(length(alphas),1);

for i = 1:length(alphas)
    filtered = n_tap_filter(mixed_delayed_signal, fs, N, K, alphas(i));
    snr_results_1(i) = SNR(signal, filtered);
end
figure;
plot(alphas, snr_results_1)
title('Constant N(50) and K(100), Change α from 0 to 1');

% Use constant α and K, change N from 1 to 50
% plot SNR of mike.wav and recovered signal.
alpha = 0.2;
K = 100;
snr_results_2 = zeros(50,1);
Ns = 1:50;

for N = Ns
    filtered = n_tap_filter(mixed_delayed_signal, fs, N, K, alpha);
    snr_results_2(N) = SNR(signal, filtered);
end
figure;
plot(Ns, snr_results_2)
title('Constant α(0.2) and K(100), change N from 1 to 50');


% Use constant α and N, change K between 100,200,300,400 miliseconds
% plot SNR of mike.wav and recovered signal.
alpha = 0.2;
N = 50;
Ks = [100 200 300 400];

snr_results_3 = zeros(4,1);
for i = 1:length(Ks) 
   delayed_signal = delay_signal(signal, fs, Ks(i)) + signal;
   filtered_signal = n_tap_filter(delayed_signal, fs, N, Ks(i), alpha);
   snr_results_3(i) = SNR(signal, filtered_signal); 
end
figure;
plot(Ks, snr_results_3)
title('Constant α(0.2) and N(50), change K between 100,200,300,400 ms');


%Apply N Tap Filter
function result = n_tap_filter(signal, fs, N, K, alpha)
    result = signal;
    for i=1:N
        signal = delay_signal(signal,fs,K);
        result = result + ((-1)*alpha)^i * signal;
    end
end

% Delay given signal
function result_signal = delay_signal(signal, fs , k)
    N = length(signal);
    result_signal = zeros(N, 1);
    delay_in_sample = fs * k / 1000;
    for i = delay_in_sample+1:N
        result_signal(i) = signal(i-delay_in_sample);
    end
end

% Calculates SNR
function result = SNR(original, recovered)
    result = 10 * log10(sum(original.^2) ./ sum((recovered-original).^2));
end