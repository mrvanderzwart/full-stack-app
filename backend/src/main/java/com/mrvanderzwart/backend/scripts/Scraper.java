
package com.mrvanderzwart.backend.scripts;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mrvanderzwart.backend.model.Matches;
import com.mrvanderzwart.backend.repository.MatchRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.text.ParseException;

public class Scraper {

    public static List<Integer> getResults(List<Matches> matches) {
        List<Integer> results = new ArrayList<>();
        int points = 0;
        for (Matches match : matches) {
            if (!match.getCompetition().equals("Eredivisie")) {
                continue;
            }
            if (match.getResult().equals("W")) {
                points += 3;
            } else if (match.getResult().equals("D")) {
                points += 1;
            }

            results.add(points);
        }

        return results;
    }

    public static Matches getNextMatch(MatchRepository matchRepository) {
        Date today = new Date();
        Date match_date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Matches match : matchRepository.findAll()) {
            try {
                match_date = dateFormat.parse(match.getMatchdate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (today.before(match_date))
                return match;
        }

        return null;
    }

    public static List<Matches> getMatchlog(MatchRepository matchRepository) {
        Date today = new Date();
        Date match_date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<Matches> completed_matches = new ArrayList<>();

        for (Matches match : matchRepository.findAll()) {
            try {
                match_date = dateFormat.parse(match.getMatchdate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (today.before(match_date))
                break;

            completed_matches.add(match);
        }

        return completed_matches;
    }

    public static void parseMatches(MatchRepository matchRepository) {

        String url = "https://fbref.com/en/squads/19c3f8c4/2021-2022/Ajax-Stats";

        try {
            // Make an HTTP GET request to the URL
            Document document = Jsoup.connect(url).get();

            // Find the table containing match results
            Element table = document.select("#all_matchlogs").first();

            // Extract rows from the table
            Elements rows = table.select("tbody tr");

            // Loop through the rows and extract match results
            for (Element row : rows) {
                String date = row.select("th[data-stat=date]").text();
                String opponent = row.select("td[data-stat=opponent]").text();
                String result = row.select("td[data-stat=result]").text();
                String competition = row.select("td[data-stat=comp]").text();

                Matches newMatch = new Matches();

                newMatch.setMatchdate(date);
                newMatch.setOpponent(opponent);
                newMatch.setResult(result);
                newMatch.setCompetition(competition);

                matchRepository.save(newMatch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
}