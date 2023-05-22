import torch
from torch import nn
from torch import optim
from torch.functional import F
import numpy as np
import pandas as pd
from torch.utils.data import Dataset


class SimpleNet(nn.Module):
    def __init__(self) -> None:
        super().__init__()
        self.fc1 = nn.Linear(in_features=4, out_features=3)
        self.fc2 = nn.Linear(in_features=3, out_features=1)
    
    def forward(self, input_wt):
        x = F.relu(self.fc1(input_wt))
        x = self.fc2(x)
        return x.cpu()
    

class AdaptiveWindowSizing(object):
    def __init__(self, model_file) -> None:
        self.model = SimpleNet()
        self.model.load_state_dict(torch.load(model_file))
    
    def __predict_window_size(self, data_rates):
        self.model.eval()
        input_data = torch.tensor(np.array(data_rates).astype("float32")).unsqueeze(0)
        return self.model(input_data).detach().numpy()[0].item()
        
    def get_prediction(self, data_rates):
        if(len(data_rates)==4):
            prediction = self.__predict_window_size(data_rates)
            return prediction
        return None


        