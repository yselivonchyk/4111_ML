clear all; close all; clc

examplesHeader = {'a:b', 'b:b', 'c:n', 'd:c', 'e:t'};

nSamplePerCategory = 300;
wrongClassifiedFraction = 0;    % This switch is untested so far!

shuffleExamples = false;
shuffleAttributes = false;

numSplitValue = 100;
numRandRange = 50;

categories = {'x', 'y', 'z'};

rng(0)

%% Define Prototy Examples which Act as a Model for the Real Examples

protoExamples = {};

% Test for binary and categorical.
protoExamples{end + 1} = {true, true, '>', 'x', true};
protoExamples{end + 1} = {true, true, '>', 'y', true};
protoExamples{end + 1} = {true, true, '>', 'z', false};

% Test for binary and numerical.
protoExamples{end + 1} = {true, false, '<', 'x', false};
protoExamples{end + 1} = {true, false, '>', 'x', true};

% Test for binary only.
protoExamples{end + 1} = {false, false, '>', 'x', true};
protoExamples{end + 1} = {false, true, '>', 'x', false};

%% Build a Set of Examples

examples = {};

% Loop over protoExamples.
for k = 1:length(protoExamples)
    protoExample = protoExamples{k};
    
    % Create nSamplePerCategory examples for each protoExample.
    for j = 1:nSamplePerCategory
        
        example = cell(1, length(protoExample));
        
        % Loop over protoExample entries and generate the data.
        for l = 1:length(protoExample)

            if protoExample{l} == true
                example{l} = 'yes';
            elseif protoExample{l} == false
                example{l} = 'no';
            elseif protoExample{l} == '>'
                randNumber = numSplitValue + ceil(rand()*numRandRange);
                example{l} = num2str(randNumber);
            elseif protoExample{l} == '<'
                randNumber = numSplitValue - ceil(rand()*numRandRange);
                example{l} = num2str(randNumber);
            else
                % Assume categorical here.
                example{l} = protoExample{l};

                if ~ismember(protoExample{l}, categories)
                    disp('WARNING: Unknown categorical item!')
                end
            end

        end
        
        examples{end+1} = example;
    end
end

%% Shuffle Operations

if shuffleExamples
    idx = randperm(length(examples));
    examples = examples(idx);
end

if shuffleAttributes
    % Number of attributes.
    n = length(examples{1}) - 1;
    idx = [randperm(n) n+1];
    
    examplesHeader = examplesHeader(idx);
    
    for k = 1:length(examples)
        examples{k} = examples{k}(idx);
    end
end

%% Randomly Wrong Classified Examples

for k = 1:length(examples)
    if rand() < wrongClassifiedFraction;
        if strcmp(examples{k}(end), 'yes');
            examples{k}(end) = 'no';
        elseif strcmp(examples{k}(end), 'no');
            examples{k}(end) = 'yes';
        else
            disp('WARNING: Something wrong!')
        end

    end
end


%% Write to File

cellData = [{examplesHeader}, examples]';
cellData = vertcat(cellData{:});

% writetable(cellData,'tabledata.txt')
% dlmwrite('data.txt', matData)

% fid = fopen('file.dlm', 'wt');
% fprintf(fid, '"%s"\t"%s"\t%g\n', cellData{:});
% fclose(fid);

T = cell2table(cellData);
writetable(T, '../Source/data_exercise_1_test.csv', ...
    'WriteVariableNames', false)


