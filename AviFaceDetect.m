clc;
clear;
close all;

faceDetector = vision.CascadeObjectDetector('ClassificationModel','UpperBody',...
    'MergeThreshold',3, 'MinSize',[100,100], 'MaxSize', [200, 200]);
shapeInserter = vision.ShapeInserter('BorderColor','Custom','CustomBorderColor',[0 255 255]);

addpath('C:\Users\David\Documents\MATLAB\403\footageframes');
cd('C:\Users\David\Documents\MATLAB\403\footageframes');

imagefiles = dir('*.jpg');      
nfiles = length(imagefiles);    % Number of files found
facenum = 1;

if ~exist('C:\Users\David\Documents\MATLAB\403\faces', 'dir') % checks if faces folder exists
    mkdir('C:\Users\David\Documents\MATLAB\403\faces') % makes it if it doesn't
else
    rmdir('C:\Users\David\Documents\MATLAB\403\faces', 's'); % make sure any old images dont transfer over
    mkdir('C:\Users\David\Documents\MATLAB\403\faces');
end
cd('C:\Users\David\Documents\MATLAB\403\faces'); % change directory

for ii=1:nfiles % iterates thru the frames
   currentfilename = imagefiles(ii).name;
   i = im2double(imread(currentfilename));
   bbox = step(faceDetector, i);
% Draw boxes around detected faces and display results
i_faces = step(shapeInserter, i, int32(bbox));
%  figure;
%  imshow(i_faces), title('Detected faces');



[row, col] = size(bbox); % gets the rows
for r = 1:row % each row is a face detected within same frame
a = imcrop(i_faces, bbox(r,:)); % a is the currently indexed face image
%  figure;
%  imshow(a);
newfilename = strcat('Face', num2str(facenum), '.jpg');
imwrite(a,newfilename);
facenum = facenum+1;
end

end
