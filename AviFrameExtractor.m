clc;
clear;
close all;

addpath('C:\Users\David\Documents\MATLAB\403');
cd('C:\Users\David\Documents\MATLAB\403');
obj = VideoReader('DemoTest3.avi'); % reads the video file
vid = read(obj);
frames = obj.NumFrames; % gets the number of frames in the file
F = cell(1,frames); % cells with frames matrices

if ~exist('C:\Users\David\Documents\MATLAB\403\footageframes', 'dir')
       mkdir('C:\Users\David\Documents\MATLAB\403\footageframes')
else
    rmdir('C:\Users\David\Documents\MATLAB\403\footageframes', 's'); % make sure any old images dont transfer over
    mkdir('C:\Users\David\Documents\MATLAB\403\footageframes');
end
cd('C:\Users\David\Documents\MATLAB\403\footageframes');

for f = 1:5:frames % this loop extracts all of the frames in the video
    F{1,f} = vid(:,:,:,f);    
    newfilename = strcat('Frame', num2str(f), '.jpg');
    imwrite(F{1,f},newfilename);
end