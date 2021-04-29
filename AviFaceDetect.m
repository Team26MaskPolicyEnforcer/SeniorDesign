clc; clear; close all;
% This code will extract the frames, then crop out faces detected in the
% video and track the detected people's positions, and output how many
% people are visible per frame and the total amount of people seen
% throughout the video. 
%Note that you'll have to adjust your file paths accordingly

%--------------------------- frame splitter --------------------------------
addpath('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem');
cd('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem');
obj = VideoReader('Joshalone.avi'); % reads the video file
vid = read(obj);
frames = obj.NumFrames; % gets the number of frames in the file
F = cell(1,frames); % cells with frames matrices

if ~exist('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/VideoFrames', 'dir')
    mkdir('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/VideoFrames')
else
    rmdir('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/VideoFrames', 's'); % make sure any old images dont transfer over
    mkdir('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/VideoFrames');
end
cd('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/VideoFrames'); % change directory

%Start timer
tic
%Frame Extraction
for f = 1:1:frames % this loop extracts all of the frames in the video
    F{1,f} = vid(:,:,:,f);    
    newfilename = strcat('Frame', num2str(f), '.jpg');
    imwrite(F{1,f},newfilename);
end

%--------------------- person tracker --------------------------------
peopleDetector = vision.CascadeObjectDetector('ClassificationModel','UpperBody',...
    'MergeThreshold', 9, 'MinSize', [164, 164], 'MaxSize', [300,300]);
%------------------- face detector ----------------------------------
faceDetector = vision.CascadeObjectDetector('ClassificationModel','UpperBody',...
    'MergeThreshold', 9, 'MinSize',[164,164], 'MaxSize', [300, 300]);
%---------------------- bounding boxes ---------------------------------
shapeInserter = vision.ShapeInserter('BorderColor','Custom','CustomBorderColor',[0 255 255],'LineWidth', 2);

%--------------------- main code ----------------------------------
addpath('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/VideoFrames');
cd('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/VideoFrames');

imagefiles = dir('*.jpg');      
nfiles = length(imagefiles);    % Number of files found
facenum = 1;   % initialize facenum for output crop names

tracks = cell(1,1); % initialize a cell to store matrices
TotalPpl = 1;

if ~exist('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/DetectedFaces', 'dir') % checks if faces folder exists
    mkdir('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/DetectedFaces') % makes it if it doesn't
else
    rmdir('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/DetectedFaces', 's'); % make sure any old images dont transfer over
    mkdir('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/DetectedFaces');
end
cd('/Users/donnyhuang322/Desktop/Engineering/ECEN_and_CSCE/Senior Design/FullSystem/DetectedFaces'); % change directory

%----- start iterating through each of the video's frames  --------------
for ind = 1:nfiles % iterates thru the frames
    currentfilename = strcat('Frame', num2str(ind), '.jpg'); % make string for file name
    currentfilenametxt = strcat('Frame', num2str(ind), '.txt');
    i = im2double(imread(currentfilename)); % loads the image file
    fid = fopen(currentfilenametxt, 'w+');
    
    % --- face part ---
    bbox = step(faceDetector, i);
    i_faces = step(shapeInserter, i, int32(bbox)); % Draw boxes around detected faces
    [row, col] = size(bbox); % gets the rows
    
%     for r = 1:row % each row is a face detected within same frame, for every face in the frame
%        a = imcrop(i_faces, bbox(r,:)); % crop out indexed face
%        newfilename = strcat('Crop', num2str(facenum), '.jpg');
%        imwrite(a,newfilename);
%        facenum = facenum+1;
%     end
    
    % --- tracking part ---
    bboxes = step(peopleDetector, i);
    i_bodies = step(shapeInserter, i, int32(bboxes)); % Draw boxes around their upper bodies
    [row2, col2] = size(bboxes); % get the row2s
    
    if row2 < 1 && isempty(tracks{1,1}) == 1
        % nothing happens bc theres nothing in the camera's view yet
    elseif isempty(tracks{1,1}) == 1 % if it's the first frame where something is seen
        for r = 1:row2 % tracker's turn for a for loop
            x = bboxes(r,1);
            y = bboxes(r,2);
            width = bboxes(r,3);
            height = bboxes(r,4);
            centerx = round(x + (width/2));
            centery = round(y + (height/2));
            center = [centerx,centery, 0, TotalPpl];
            tracks{1,r} = center;
            fprintf(fid, '%d %d %d %d %d\n', TotalPpl, x, y, width, height);
            sizeTracks = size(tracks);
            CurrentPpl = sizeTracks(2);
            TotalPpl = TotalPpl + 1;
       end
   else % for all the frames after the first
       for r = 1:row2 % goes thru each face in the frame
           x = bboxes(r,1);
           y = bboxes(r,2);
           width = bboxes(r,3);
           height = bboxes(r,4);
           centerx = round(x + (width/2));
           centery = round(y + (height/2));
           sizeTracks = size(tracks);
           trackLength = sizeTracks(2);
           CurrentPpl = trackLength;
           j = 0; % flag for in case it's a new person
           for indcell = 1:trackLength % indexes through the old people
               if j == 0
               prevFace = tracks{1, indcell}; % let's compare this new face to the people we know already
               distance = sqrt((centerx - prevFace(1))^2 + (centery - prevFace(2))^2); % distance formula between centers
               if distance < 120 % existing person! [LOOK HERE]
                   j = 1; % change the flag
                   tracks{1, indcell}(1,1) = centerx; % update the person's position
                   tracks{1, indcell}(1,2) = centery;
                   tracks{1, indcell}(1,3) = 0; % note that their counter is reset to 0
                   fprintf(fid, '%d %d %d %d %d\n', tracks{1, indcell}(1,4), x, y, width, height);
               end
               end
           end % after it finishes comparing with all old people
           if j == 0 % if none of the old people matched with new person
               center = [centerx, centery, 0, TotalPpl];
               tracks{1, (trackLength + 1)} = center; % its a new person!
               fprintf(fid, '%d %d %d %d %d\n', TotalPpl, x, y, width, height);
               TotalPpl = TotalPpl + 1;
               
           end
           for indcell2 = 1:(trackLength) % iterate thru the tracks
               tracks{1,indcell2}(1,3) = (tracks{1,indcell2}(1,3)) + 1; % increment their counters
               if tracks{1,indcell2}(1,3) > 15 % if person wasn't reset after 5 frames in a row [LOOK HERE]
                   tracks{1,indcell2} = []; % remove them from tracks
               end
           end
           tracks = tracks(~cellfun('isempty',tracks));
       end
    end
   fclose(fid);
end
toc
disp('current people in view: '); % displays how many people are currently in the camera's view right now
disp(CurrentPpl);
disp('total people seen: '); % displays how many people were seen throughout the duration of the video
disp(TotalPpl-1);

