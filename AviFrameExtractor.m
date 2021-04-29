clc;
clear;
close all;

addpath('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem');
cd('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem');
obj = VideoReader('3manTestWhitemask.avi'); % reads the video file
vid = read(obj);
frames = obj.NumFrames; % gets the number of frames in the file
F = cell(1,frames); % cells with frames matrices

if ~exist('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/footageframes', 'dir')
       mkdir('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/footageframes')
else
    rmdir('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/footageframes', 's'); % make sure any old images dont transfer over
    mkdir('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/footageframes');
end
cd('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/footageframes');

for f = 1:1:frames % this loop extracts all of the frames in the video
    F{1,f} = vid(:,:,:,f);    
    newfilename = strcat('Frame', num2str(f), '.jpg');
    imwrite(F{1,f},newfilename);
end