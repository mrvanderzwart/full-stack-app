package com.mrvanderzwart.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrvanderzwart.backend.model.Matches;
import com.mrvanderzwart.backend.service.MatchService;

@RestController
@CrossOrigin(origins="*")
public class MatchController {

    @Autowired
    private MatchService service;

    @PostMapping("/parseMatches")
    public void parseMatches() {
        service.parseMatches();
    }

    @DeleteMapping("/clearMatches")
    public void clearMatches() {
        service.clearMatches();
    }

    @GetMapping("/getMatches")
    public List<Matches> getMatches() {
        return service.getMatches();
    }

    @GetMapping("/getNextMatch")
    public Matches getNextMatch() {
        return service.getNextMatch();
    }

    @GetMapping("/getPrediction")
    public String getPrediction() throws Exception {
        return service.getPrediction();
    }

    @GetMapping("/getTeamCode")
    public String getTeamCode(String team) throws Exception {
        System.out.println(team);
        return service.getTeamCode(team);
    }

}