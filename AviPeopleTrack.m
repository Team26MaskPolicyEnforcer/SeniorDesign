clc;
clear;
close all;

addpath('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/footageframes');
cd('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/footageframes');
imagefiles = dir('*.jpg');      
nfiles = length(imagefiles);

% set up
peopleDetector = vision.CascadeObjectDetector('ClassificationModel','UpperBody',...
    'MergeThreshold', 9, 'MinSize', [164, 164], 'MaxSize', [300,300]);
shapeInserter = vision.ShapeInserter('BorderColor','Custom','CustomBorderColor',[0 255 255],'LineWidth', 3);

% when it actually runs on the picture
I = imread('Frame108.jpg');
bboxes = step(peopleDetector,I);
I_people = step(shapeInserter, I, int32(bboxes));
figure, imshow(I_people)
title('detected people');
tracks = cell(1,1); % initialize a cell to store matrices

% tracking time
[row, col] = size(bboxes); % gets the rows, in each row is the coordinates for each both
for r = 1:row
   x = bboxes(r,1);
   y = bboxes(r,2);
   width = bboxes(r,3);
   height = bboxes(r,4);
   centerx = round(x + (width/2));
   centery = round(y + (height/2));
   center = [centerx,centery];
   tracks{1,r} = center;
end
