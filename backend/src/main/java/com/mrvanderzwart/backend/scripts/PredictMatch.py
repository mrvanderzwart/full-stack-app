import requests
import pandas as pd
import sys

from sklearn.ensemble import RandomForestClassifier
from datetime import datetime


seasons = ['2021-2022', '2022-2023', '2023-2024']
eredivisie_squads = {
    'Ajax' : '19c3f8c4',
    'AZ-Alkmaar' : '3986b791',
    'Almere-City' : '2b41acb5',
    'Excelsior' : '740cb7d4',
    'Feyenoord' : 'fb4ca611',
    'Fortuna-Sittard' : 'bd08295c',
    'Go-Ahead-Eagles' : 'e33d6108',
    'Heerenveen' : '193ff7aa',
    'Heracles' : 'c882b88e',
    'NEC-Nijmegen' : 'fc629994',
    'PSV-Eindhoven' : 'e334d850',
    'RKC-Waalwijk' : 'bb14adb3',
    'Sparta Rotterdam' : '146a68ce',
    'Twente' : 'a1f721d3',
    'Utrecht' : '2a428619',
    'Vitesse' : '209d7fa2',
    'Zwolle' : 'e3db180b'
}


def format_data(df):
    df["hour"] = df["Time"].str.replace(":.+", "", regex=True).astype(int)
    df["day_code"] = df["Day"].astype("category").cat.codes
    df["venue_code"] = df["Venue"].astype("category").cat.codes
    df["result_code"] = df["Result"].astype("category").cat.codes
    df["opponent_code"] = df["Opponent"].astype("category").cat.codes


def split_data(df):
    today = datetime.today().date()
    today = today.strftime("%Y-%m-%d")

    index = df.index[df["Date"] > today][0]

    return df.iloc[:index], df.iloc[index]


def get_seasons_data():
    pass

def predict_result(df):
    rf = RandomForestClassifier(n_estimators=50, min_samples_split=10, random_state=1)
    train, test = split_data(df)
    predictors = ["hour", "day_code", "venue_code", "opponent_code"]
    rf.fit(train[predictors], train["result_code"])
    pred = rf.predict(test[predictors].to_numpy().reshape(1, -1))

    cat = df.Result.astype('category')
    codes_dic = dict(enumerate(cat.cat.categories))
    if codes_dic[pred[0]] == "W":
        return "win"
    elif codes_dic[pred[0]] == "L":
        return "lose"
    elif codes_dic[pred[0]] == "D":
        return "draw"
    

def main():
    if len(sys.argv) >= 2:
        print(eredivisie_squads[sys.argv[1]])
        return

    url = "https://fbref.com/en/squads/19c3f8c4/Ajax-Stats"
    data = requests.get(url)
    matches = pd.read_html(data.text, match="Scores & Fixtures")[0]
    format_data(matches)
    print(predict_result(matches))
    
    
if __name__ == '__main__':
    main()