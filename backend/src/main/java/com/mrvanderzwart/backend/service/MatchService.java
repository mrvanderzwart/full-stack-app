package com.mrvanderzwart.backend.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrvanderzwart.backend.model.Matches;
import com.mrvanderzwart.backend.repository.MatchRepository;
import com.mrvanderzwart.backend.scripts.Scraper;


@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepo;

    public void parseMatches() {
        Scraper.parseMatches(matchRepo);
    }

    public void clearMatches() {
        matchRepo.deleteAll();
    }

    public List<Matches> getMatches() {
        return Scraper.getMatchlog(matchRepo);
    }

    public Matches getNextMatch() {
        return Scraper.getNextMatch(matchRepo);
    }

    public String getPrediction() throws Exception{
        String result = "{}";

        String pythonInterpreter = "python";
        String pythonScript = "backend\\src\\main\\java\\com\\mrvanderzwart\\backend\\scripts\\PredictMatch.py";

        String[] command = {pythonInterpreter, pythonScript};

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        result = reader.readLine();

        return result;
    }

    public String getTeamCode(String team) throws Exception {
        String pythonInterpreter = "python";
        String pythonScript = "backend\\src\\main\\java\\com\\mrvanderzwart\\backend\\scripts\\PredictMatch.py";

        String[] command = {pythonInterpreter, pythonScript, team};

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String result = reader.readLine();

        return result;
    }
}