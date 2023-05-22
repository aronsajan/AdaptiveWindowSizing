from flask import Flask, request, Response
from adaptive_window_size import AdaptiveWindowSizing
import json

app = Flask(__name__)

window_size_mgr = AdaptiveWindowSizing("./window_size_model.pt")

@app.route("/windowsize",methods=['POST'])
def get_window_size():
    print(request.data)
    request_data = request.json
    data_rates = request_data['dataRates']
    prediction = window_size_mgr.get_prediction(data_rates)
    result = dict()
    if(prediction):
        print("Prediction : {}".format(prediction))
        result['predictedWindowSize'] = prediction
    
    return Response(json.dumps(result), mimetype='application/json')
    
    
if __name__=='__main__':
    app.run(port=5000)