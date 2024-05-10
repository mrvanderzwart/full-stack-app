import requests
import pandas as pd

from sklearn.ensemble import RandomForestClassifier
from datetime import datetime


def formatData(df):
    df["hour"] = df["Time"].str.replace(":.+", "", regex=True).astype(int)
    df["day_code"] = df["Day"].astype("category").cat.codes
    df["venue_code"] = df["Venue"].astype("category").cat.codes
    df["result_code"] = df["Result"].astype("category").cat.codes
    df["opponent_code"] = df["Opponent"].astype("category").cat.codes


def splitData(df):
    today = datetime.today().date()
    today = today.strftime("%Y-%m-%d")

    index = df.index[df["Date"] > today][0]

    return df.iloc[:index], df.iloc[index]


def predictResult(df):
    rf = RandomForestClassifier(n_estimators=50, min_samples_split=10, random_state=1)
    train, test = splitData(df)
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
    
if __name__ == '__main__':
    url = "https://fbref.com/en/squads/19c3f8c4/Ajax-Stats"
    data = requests.get(url)
    matches = pd.read_html(data.text, match="Scores & Fixtures")[0]
    formatData(matches)
    print(predictResult(matches))