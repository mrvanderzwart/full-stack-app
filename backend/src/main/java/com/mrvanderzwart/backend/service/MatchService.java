package com.mrvanderzwart.backend.service;

import java.io.BufferedReader;
import java.io.IOException;
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

    public String getPrediction() {
        String result = "{}";
        try {
            String pythonInterpreter = "python";
            String pythonScript = "src\\main\\java\\com\\mrvanderzwart\\footballapplication\\script\\PredictMatch.py";

            String[] command = {pythonInterpreter, pythonScript};

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            result = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } 

        return result;
    }
}