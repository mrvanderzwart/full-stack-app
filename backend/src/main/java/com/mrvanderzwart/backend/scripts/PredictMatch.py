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


def create_feature_form(df):
    score_map = {'W' : 3, 'D' : 1, 'L' : 0}
    df['Scores'] = df['Result'].map(score_map)
    df['form'] = df['Scores'].rolling(window=5, min_periods=1).sum()

    return df


def create_feature_rest(df):
    df['Date'] = pd.to_datetime(df['Date'])
    df['rest'] = df['Date'].diff().dt.days
    df['rest'] = df['rest'].fillna(10).astype(int)

    return df


def format_data(df):
    df["hour"] = df["Time"].str.replace(":.+", "", regex=True).astype(int)
    df["day_code"] = df["Day"].astype("category").cat.codes
    df["venue_code"] = df["Venue"].astype("category").cat.codes
    df["result_code"] = df["Result"].astype("category").cat.codes
    df["opponent_code"] = df["Opponent"].astype("category").cat.codes
    create_feature_form(df)
    create_feature_rest(df)


def split_data(df):
    today = datetime.today().date()
    today = today.strftime("%Y-%m-%d")

    index = df.index[df["Date"] > today][0]

    return df.iloc[:index], df.iloc[index]


def get_seasons_data(team):
    all_matches = pd.DataFrame()
    for season in seasons:
        url = "https://fbref.com/en/squads/"+eredivisie_squads[team]+"/"+season+"/"+team+"-Stats"
        data = requests.get(url)
        matches = pd.read_html(data.text, match="Scores & Fixtures")[0]
        matches.to_csv(f'season{season}.csv', index=False)
        all_matches = pd.concat([all_matches, matches], axis=0)

    all_matches.to_csv('output.csv', index=False)

    return all_matches


def predict_result(df):
    rf = RandomForestClassifier(n_estimators=50, min_samples_split=10, random_state=1)
    train, test = split_data(df)
    predictors = ["hour", "day_code", "venue_code", "opponent_code", "form", "rest"]
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
    if len(sys.argv) == 2:
        print(eredivisie_squads[sys.argv[1]])
        return

    team1 = sys.argv[1]
    team2 = sys.argv[2]

    matches = get_seasons_data(team1)

    format_data(matches)
    print(predict_result(matches))
    
    
if __name__ == '__main__':
    main()
